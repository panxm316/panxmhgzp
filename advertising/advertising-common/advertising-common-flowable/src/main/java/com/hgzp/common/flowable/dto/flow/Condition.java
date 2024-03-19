package com.hgzp.common.flowable.dto.flow;

import lombok.Data;

/**
 * 条件
 */
@Data
public class Condition {
    /**
     * 条件相关表单组件的 key
     */
    private String key;
    /**
     * 条件用的关系表达式 != 、 contain 、 notempty 之类的
     */
    private String expression;
    /**
     * 条件的值，如具体数字等
     */
    private Object value;
    /**
     * 条件相关组件的 类型，如：Input（单行文本）、 Textarea（多行文本）
     */
    private String keyType;

}
