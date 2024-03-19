package com.hgzp.core.exception;


import com.hgzp.core.emnus.ResultDefines;

/**
 * 项目名：hgcb-parent
 * 类全名：cpsn.exception.HttpConnectExcetion
 * 创建人：wangwk
 * 类描述：http 异常
 * 创建日期：2021/7/6 17:09
 */
public class HttpConnectException extends RuntimeException{

    private Integer code;

    private String errorMessage;

    public HttpConnectException(String message){
        super(message);
    }

    public HttpConnectException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public HttpConnectException(ResultDefines exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }


    public Integer getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
