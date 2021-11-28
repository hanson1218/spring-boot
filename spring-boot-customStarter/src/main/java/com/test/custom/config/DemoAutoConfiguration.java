package com.test.custom.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = DemoProperties.class)
public class DemoAutoConfiguration {
    private final DemoProperties demoProperties;

    public DemoAutoConfiguration(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @Bean
    // 当前项目是否包含 DemoService Class
//    @ConditionalOnMissingBean(DemoService.class)
    public DemoService demoService() {
        return new DemoService(demoProperties);
    }
}
