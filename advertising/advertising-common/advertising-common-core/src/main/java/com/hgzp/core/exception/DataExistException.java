package com.hgzp.core.exception;

/**
 * 项目名：hgyuqing-parent
 * 类全名：com.hgzp.controller.exception.sys.DataExistException
 * 创建人：wangwk
 * 类描述：数据已存在异常
 * 创建日期：2019/4/24 14:53
 */
public class DataExistException extends RuntimeException{


    public DataExistException() {
        super();
    }

    public DataExistException(String message) {
        super(message);
    }
}
