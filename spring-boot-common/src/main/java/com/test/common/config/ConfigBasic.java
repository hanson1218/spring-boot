package com.test.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Map;

public abstract class ConfigBasic implements EnvironmentAware {

    //配置上下文
    private Environment evn;
    //参数绑定工具
    private Binder binder;
    //默认配置
    public Map<String, String> defaultMap;

    @Override
    public void setEnvironment(Environment environment) {
        this.evn = environment;
        binder = Binder.get(evn);
    }

    public void putValue(Map<String, String> retMap, String key){
        retMap.put(key, defaultMap.get(key));
    }

    public void putValue(Map<String, String> retMap, String key, String defaultValue){
        retMap.put(key, defaultMap.getOrDefault(key, defaultValue));
    }

    public Map<String, String> getMap(String prefix){
        return (Map<String, String>) binder.bind(prefix, Map.class).get();
    }
    public Boolean getBooleanOrDefault(Map<String, String> myMap, String key){
        String booleanValue = myMap.getOrDefault(key, defaultMap.get(key));
        return StringUtils.isBlank(booleanValue) ? null : Boolean.parseBoolean(booleanValue);
    }

    public Integer getIntValue(Map<String, String> myMap, String key){
        String intValue = myMap.getOrDefault(key, defaultMap.get(key));
        return StringUtils.isBlank(intValue) ? null : Integer.parseInt(intValue);
    }

    public String getStringValue(Map<String, String> myMap, String key){
        String strValue = myMap.getOrDefault(key, defaultMap.get(key));
        return StringUtils.isBlank(strValue) ? null : strValue;
    }

}