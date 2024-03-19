package com.hgzp.advertising.pagemodel.statistic.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ColumnSummary
 * 创建人：lhl
 * 类描述：报表列汇总项
 * 创建日期：2024/1/25 10:16
 */
@Data
public class ColumnSummary {
    /**
     * 列名称
     */
    private String name;
    /**
     * 汇总金额
     */
    private BigDecimal summary;
    /**
     * 汇总金额
     */
    private String strsummary;

}


