package com.hgzp.utils.exception;

/**
 * FileUploadUnFinshException
 * 创建人：wangwk
 * 类描述：文件上传未完成异常
 * 创建日期：2023/9/2 16:54
 */
public class FileUploadUnFinshException extends RuntimeException{

    public FileUploadUnFinshException() {
        super();
    }

    public FileUploadUnFinshException(String message) {
        super(message);
    }
}
