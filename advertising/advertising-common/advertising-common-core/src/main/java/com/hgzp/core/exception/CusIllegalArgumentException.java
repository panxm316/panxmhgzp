package com.hgzp.core.exception;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.core.exception.CusIllegalArgumentException
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/5/24 13:30
 */
public class CusIllegalArgumentException extends RuntimeException{
    public CusIllegalArgumentException(){
        super();
    }

    public CusIllegalArgumentException(String message){
        super(message);
    }
}
