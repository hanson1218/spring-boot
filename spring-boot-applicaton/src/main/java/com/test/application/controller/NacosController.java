package com.test.application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope   //动态刷新
@RequestMapping("nacos")
public class NacosController {
    @Value("${test.name}")
    private String name;
    @GetMapping("hello")
    public String hell(){
        return name;
    }
}
