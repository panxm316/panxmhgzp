package com.hgzp.core.exception;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.core.exception.DataNotSupportException
 * 创建人：wangwk
 * 类描述：数据不支持
 * 创建日期：2022/1/12 11:21
 */
public class DataNotSupportException extends RuntimeException{
    public DataNotSupportException() {
        super();
    }
    public DataNotSupportException(String message) {
        super(message);
    }
}
