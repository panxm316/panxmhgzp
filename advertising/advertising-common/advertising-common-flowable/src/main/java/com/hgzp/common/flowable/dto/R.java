package com.hgzp.common.flowable.dto;

import java.io.Serializable;

/**
 * 响应信息主体
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success = false;

    private int code;

    private String msg;

    private T obj;

    private long total;

    public static <T> R<T> fail(boolean success, String msg) {
        return restResult(null, false, msg);
    }

    private static <T> R<T> restResult(T data, boolean success, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setSuccess(success);
        apiResult.setObj(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static R success(){
        R json = new R();
        json.setSuccess(true);
        return json;
    }
    public static <T> R<T> success(T obj){
        R<T> json = new R<>();
        json.setSuccess(true);
        json.setObj(obj);
        return json;
    }

    public static <T> R<T> fail(String msg) {
        return restResult(null, false, msg);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
