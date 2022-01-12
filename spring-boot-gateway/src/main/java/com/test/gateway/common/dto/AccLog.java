package com.test.gateway.common.dto;

import lombok.Data;

@Data
public class AccLog {

    private String reqId;

    private String clientIp;
    /**
     *  请求path
     */
    private String api;
    /**
     * 完整路径包括get请求参数
     *
     **/
    private String url;
    /**
     * POST/GET
     */
    private String reqMethod;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 请求长度
     */
    private Long reqContentLength;

    /**
     * 请求时间
     */
    private long reqTime;

    /**
     * 代理请求类型
     */
    private String userAgent;
    /**
     * HttpCode
     */
    private Integer httpCode;
    /**
     * 响应耗时
     */
    private long resCost;

    /**
     * 响应数据
     */
    private String resBody;


}
