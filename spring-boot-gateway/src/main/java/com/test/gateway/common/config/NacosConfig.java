package com.test.gateway.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 路由转发配置
 * 读取nacos的配置文件时，本地的配置最好用bootstrap.properties
 */
@RefreshScope
@Configuration
public class NacosConfig {

    @Value("${nacos.router.service}")
    private String routerService;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosServerAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String nacosNamespace;

    public String getNacosServerAddr() {
        return nacosServerAddr;
    }

    public void setNacosServerAddr(String nacosServerAddr) {
        this.nacosServerAddr = nacosServerAddr;
    }

    public String getNacosNamespace() {
        return nacosNamespace;
    }

    public void setNacosNamespace(String nacosNamespace) {
        this.nacosNamespace = nacosNamespace;
    }

    public String getRouterService() {
        return routerService;
    }

    public void setRouterService(String routerService) {
        this.routerService = routerService;
    }
}
