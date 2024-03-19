package com.hgzp.core.exception;

/**
 * 项目名：yuqing
 * 类全名：com.hgzp.exception.sys.DataNotExistException
 * 创建人：suny
 * 类描述： 数据不存在
 * 创建日期：2019/5/21 8:40
 */
public class DataNotExistException extends RuntimeException {
    public DataNotExistException() {
        super();
    }
    public DataNotExistException(String message) {
        super(message);
    }
}
