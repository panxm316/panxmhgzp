package com.hgzp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @description: 日志记录
 * @author: wangwk
 * @create：2023/8/2 14:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogRecord {
    //模块名
    String title();

    //操作内容
    String option() default "日志";

}