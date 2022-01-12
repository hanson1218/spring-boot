package com.test.gateway.filter;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.test.common.utils.WebUtil;
import com.test.gateway.common.constant.FilterOrder;
import com.test.gateway.common.dto.AccLog;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AccLogFilter   implements Ordered, GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccLogFilter.class);
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        AccLog accLog = new AccLog();
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        accLog.setReqId(request.getId());
        accLog.setClientIp(WebUtil.getIpAddress(request));
        accLog.setApi(request.getPath().value());
        accLog.setUrl(request.getURI().toString());
        accLog.setReqMethod(request.getMethodValue());
        accLog.setUserAgent(headers.getFirst(HttpHeaders.USER_AGENT));
        accLog.setReqContentLength(headers.getContentLength());
        accLog.setReqTime(new Date().getTime());
        //设置参数，耗时，响应状态，响应数据
        MediaType mediaType = headers.getContentType();
        if(MediaType.APPLICATION_FORM_URLENCODED.equals(mediaType)
        || MediaType.APPLICATION_JSON.equals(mediaType)){
            setBody(accLog,exchange,chain);
        }else{
            setBasic(accLog,exchange,chain);
        }
        return null;
    }

    private Mono<Object> setBody(AccLog accLog, ServerWebExchange exchange, GatewayFilterChain chain){
        ServerRequest serverRequest = ServerRequest.create(exchange,messageReaders);

        Mono modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body ->{
                    accLog.setResBody(body);
                    return Mono.just(body);
                });
        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage,new BodyInserterContext())
                .then(Mono.defer(() -> {
                // 重新封装请求
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);
                    // 记录响应日志
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accLog);
                    // 记录普通的
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // 打印日志
                                writeAccessLog(accLog);
                            }));
                }));
    }

    /**
     * Get请求记录
     * @param accLog
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Object> setBasic(AccLog accLog, ServerWebExchange exchange, GatewayFilterChain chain){
        accLog.setParams(JSONObject.toJSONString(exchange.getRequest().getQueryParams()));
        //获取响应体
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accLog);

        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // 打印日志
                    writeAccessLog(accLog);
                }));
    }

    /**
     记录响应日志
     通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(final ServerWebExchange exchange, final AccLog accLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtil.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);
                            // 释放掉内存
                            DataBufferUtils.release(join);
                            String responseResult = new String(content, StandardCharsets.UTF_8);
                            accLog.setHttpCode(getStatusCode().value());
                            accLog.setResBody(responseResult);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                return super.writeWith(body);
            }
        };
    }

    /**
     * 请求装饰器，重新计算 headers
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers,
                                                       CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }
            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }};
    }

    private void writeAccessLog(AccLog accLog){
        accLog.setResCost(System.currentTimeMillis()-accLog.getReqTime());
        logger.info(JSONObject.toJSONString(accLog));
        //TODO 数据库记录日志
    }





    @Override
    public int getOrder() {
        return FilterOrder.ACC_ORDER;
    }
}
