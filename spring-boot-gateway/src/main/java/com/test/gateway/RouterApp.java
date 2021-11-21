package com.test.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient //向注册中心暴露服务
@EnableFeignClients // 使 @feignClient 注解可以生效
public class RouterApp
{
    public static void main(String[] args) {
        SpringApplication.run(RouterApp.class, args);
    }
}
