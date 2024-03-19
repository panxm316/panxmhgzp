package com.hgzp.core.exception;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.core.exception.AuthorizationException
 * 创建人：wangwk
 * 类描述：权限认证失败
 * 创建日期：2022/11/12 14:11
 */
public class AuthorizationException extends RuntimeException{

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }

}
