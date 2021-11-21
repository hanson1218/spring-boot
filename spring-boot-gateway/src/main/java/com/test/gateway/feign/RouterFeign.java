package com.test.gateway.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 网关拦截后，由feign做远程调用
 */
@FeignClient(value = "nacos-test")
public interface RouterFeign {

    //和application应用的接口路径保持一致
    @GetMapping("/test/feign")
    String feign();
}
