package com.test.common.exception;

public class BizException extends RuntimeException {

    private String code;

    public BizException(String msg){
        super(msg);
    }

    public BizException(String msg,String code){
        super(code);
        this.code=code;
    }

    public BizException(Exception e){
        super(e.getMessage());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
