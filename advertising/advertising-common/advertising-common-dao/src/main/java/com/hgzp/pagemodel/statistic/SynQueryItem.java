package com.hgzp.pagemodel.statistic;

import lombok.Data;

/**
 * SynQueryItem
 * 创建人：综合查询字段明细
 * 类描述：lhl
 * 创建日期：2024/2/27 8:56
 */
@Data
public class SynQueryItem {
    /**
     * 字段中文名称
     */
    private String fieldname;
    /**
     * 字段英文名称
     */
    private String enName;
    /**
     * 条件操作符
     */
    private String conditionOp;
    /**
     * 英文条件操作符
     */
    private String enConditionOp;
    /**
     * 值
     */
    private String value;
    /**
     * 值
     */
    private String othervalue;
    /**
     * 条件
     */
    private String condition;

    /**
     * 字段表前缀
     */
    private String enPreName;

}


