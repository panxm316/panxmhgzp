package com.hgzp.utils.exception;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.exception.UfileException
 * 创建人：wangwk
 * 类描述：统一文件相关异常
 * 创建日期：2022/1/8 14:50
 */
public class UfileException extends RuntimeException{
    public UfileException() {
        super();
    }
    public UfileException(String message) {
        super(message);
    }
}
