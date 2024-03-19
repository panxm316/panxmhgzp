package com.hgzp.core.annotation;

import java.lang.annotation.*;

/**
 * 创建人：wangwk
 * 类描述：需要记录数据变化
 * 创建日期：2023/8/7 13:37
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface LogData {

    public enum EncryptType{
        /**
         * 无加密
         */
        NONE,
        /**
         * SM4加密
         */
        SM4
    }

    /**
     * 列别名
     */
    String alias() default "";


    /**
     * 关联显示的列名，放在id上显示列名
     */
    String showColumn() default "";

    /**
     *  需要关联显示的表(配合mappedByColumn使用)
     */
    String mappedBy() default "";

    /**
     *  需要关联的表的列(配合mappedBy使用)
     */
    String mappedByColumn() default "";


    /**
     * 字段加密方式
     */
    EncryptType encrypt() default EncryptType.NONE;


}
