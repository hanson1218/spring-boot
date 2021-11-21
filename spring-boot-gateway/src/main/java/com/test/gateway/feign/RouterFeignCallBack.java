package com.test.gateway.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 调用远程接口失败时，调用此处方法
 */
@Component
public class RouterFeignCallBack implements RouterFeign {
    private static Logger logger = LoggerFactory.getLogger(RouterFeign.class);
    @Override
    public String feign() {
        System.out.println("feign call back");
        logger.error("feign call back");
        return null;
    }
}
