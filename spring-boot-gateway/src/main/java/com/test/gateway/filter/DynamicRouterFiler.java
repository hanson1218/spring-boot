package com.test.gateway.filter;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.test.gateway.common.config.NacosConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * 定义路由转发策略
 * Spring-Cloud-Gateway通过RouteDefinition来构建起Route实例信息
 * PredicateDefinition : 断言条件(谓语)定义，构建 Route 时，PredicateDefinition 转换成 Predicate
 * FilterDefinition : 过滤条件的定义，构建Route 时，FilterDefinition 转换成 GatewayFilter
 *
 */
@Component
public class DynamicRouterFiler implements ApplicationEventPublisherAware,InitializingBean {

    @Autowired
    private NacosConfig nacosConfig;
    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    private Set<String> ROUTER_SET = new HashSet<>();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher=applicationEventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addRouter(nacosConfig.getRouterService());
        //添加监听
        addNacosListener();
    }

    public void addRouter(String routerService){
        clearRoute();
        resetRouter(routerService);
        pulish();
    }

    private void addNacosListener() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR,nacosConfig.getNacosServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE,nacosConfig.getNacosNamespace());
        String dataId="nacos-gateway-filter.properties";
        String groupName="DEFAULT_GROUP";
        NacosFactory.createConfigService(properties).addListener(dataId, groupName, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void receiveConfigInfo(String s) {
                addRouter(nacosConfig.getRouterService());
            }
        });
    }

    public void resetRouter(String service){
        String[] services = service.split(",");
        for(String routerName:services){
            if(ROUTER_SET.contains(routerName)){
                continue;
            }
            //
            RouteDefinition definition = new RouteDefinition();
            definition.setId(routerName);
            //routerName是注册到nacos的服务名称，这个位置还支持http形式的配置Uri
            //UriComponentsBuilder.fromHttpRequest("http://127.0.0.1").build().toUri()
            //如果使用Load Balance。springcloud2020弃用了Ribbon，所以要单独引入
            definition.setUri(URI.create("lb://"+routerName+"/"));

            //名称是固定的, 路径去掉2层前缀
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName("StripPrefix");
            Map<String,String> param = new HashMap<>();
            param.put("_genkey_0","2");
            filterDefinition.setArgs(param);
            definition.setFilters(Arrays.asList(filterDefinition));

            //配置转发
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setName("Path");
            Map<String,String> predicateMap = new HashMap<>();
            predicateMap.put("pattern","/gateway/" + routerName + "/**");
            predicateDefinition.setArgs(predicateMap);
            definition.setPredicates(Arrays.asList(predicateDefinition));

            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            ROUTER_SET.add(definition.getId());
        }
    }

    private void clearRoute(){
        for(String serviceName:ROUTER_SET){
            routeDefinitionWriter.delete(Mono.just(serviceName)).subscribe();
        }
        ROUTER_SET.clear();
    }

    public void pulish(){
        this.publisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }
}
