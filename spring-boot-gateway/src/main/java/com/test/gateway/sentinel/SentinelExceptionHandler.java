package com.test.gateway.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SentinelExceptionHandler {

    public static String handlerA(BlockException ex){
        System.out.println("handlerA");
                String msg = null;
        if (ex instanceof FlowException) {
            msg = "限流了";
        } else if (ex instanceof DegradeException) {
            msg = "降级了";
        } else if (ex instanceof ParamFlowException) {
            msg = "热点参数限流";
        } else if (ex instanceof SystemBlockException) {
            msg = "系统规则（负载/...不满足要求）";
        } else if (ex instanceof AuthorityException) {
            msg = "授权规则不通过";
        }
        return msg;
    }

//    @Override
//    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException ex) throws Exception {
//        String msg = null;
//        if (ex instanceof FlowException) {
//            msg = "限流了";
//        } else if (ex instanceof DegradeException) {
//            msg = "降级了";
//        } else if (ex instanceof ParamFlowException) {
//            msg = "热点参数限流";
//        } else if (ex instanceof SystemBlockException) {
//            msg = "系统规则（负载/...不满足要求）";
//        } else if (ex instanceof AuthorityException) {
//            msg = "授权规则不通过";
//        }
//        // http状态码
//        httpServletResponse.setStatus(500);
//        httpServletResponse.setCharacterEncoding("utf-8");
//        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
//        httpServletResponse.setContentType("application/json;charset=utf-8");
//        httpServletResponse.getWriter().write(msg);
//
//
//    }
}
