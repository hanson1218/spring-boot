package com.test.common.utils;

import com.google.common.base.Joiner;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static RequestConfig requestConfig = null;
    private static final String UTF_8_CHARA="UTF-8";

    static{
        requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
    }

    public static void main(String[] args) {
        Map<String,String> param = new HashMap<>();
        param.put("test","123");
        param.put("wrer","456");
        System.out.println(sendHttpGet("https://vimsky.com/zh-tw/examples/detail/java-method-org.apache.http.impl.client.HttpClientBuilder.setSSLHostnameVerifier.html",param));
    }

    public static String sendHttpGet(String url){
        return sendHttp(new HttpGet(url));
    }

    public static String sendHttpGet(String url, Header[] headers){
        HttpGet httpGet =new HttpGet(url);
        httpGet.setHeaders(headers);
        return sendHttp(httpGet);
    }

    public static String sendHttpGet(String url, Map<String,String> param){
        String paramStr =  Joiner.on("&")
                // 用指定符号代替空值,key 或者value 为null都会被替换
                .useForNull("")
                .withKeyValueSeparator("=")
                .join(param);
        HttpGet httpGet =new HttpGet(url+"?"+paramStr);
        return sendHttp(httpGet);
    }

    public static String sendHttpPost(String url, Map<String,String> param) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> list = new ArrayList<>();
        for(String key:param.keySet()){
            list.add(new BasicNameValuePair(key,param.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(list,UTF_8_CHARA));
        return sendHttp(httpPost);
    }

    public static String sendHttpPost(String url, String paramJson) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(paramJson,ContentType.create("application/json",UTF_8_CHARA));
        httpPost.setEntity(stringEntity);
        return sendHttp(httpPost);
    }

    /**
     * 发送http/https 请求核心方法
     * @param httpRequestBase
     * @return
     */
    private static String sendHttp(HttpRequestBase httpRequestBase){
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String responseStr = null;
        try{
            String scheme = httpRequestBase.getURI().getScheme();
            if("https".equalsIgnoreCase(scheme)){
                PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpRequestBase.getURI().toString()));
                DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
                httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            }else if("http".equalsIgnoreCase(scheme)){
                httpClient = HttpClients.createDefault();
            }else{
                throw  new IllegalArgumentException("Url Error");
            }
            httpRequestBase.setConfig(requestConfig);
            response =httpClient.execute(httpRequestBase);
            responseStr = EntityUtils.toString(response.getEntity(),UTF_8_CHARA);
        }catch (Exception e){
            logger.error("Send http error",e);
        }finally {
            try{
                if(response != null){
                    response.close();;
                }
                if(httpRequestBase != null){
                    httpRequestBase.clone();
                }
                if(httpClient !=null){
                    httpClient.close();
                }
            }catch (Exception e){
                logger.error("Close httpResource error",e);
            }
        }
        return responseStr;
    }
}
