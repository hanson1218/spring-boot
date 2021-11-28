package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.test.*.dao")
@EnableDiscoveryClient //向注册中心暴露服务
@EnableFeignClients // 使 @feignClient 注解可以生效
public class App 
{
    public static void main(String[] args) {
       SpringApplication.run(App.class, args);


    }
}
