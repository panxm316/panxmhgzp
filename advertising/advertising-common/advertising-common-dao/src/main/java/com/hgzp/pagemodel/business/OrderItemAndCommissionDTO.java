package com.hgzp.pagemodel.business;

import lombok.Data;

import java.math.BigDecimal;

/**
 * OrderItemAndCommission
 * 创建人：suny
 * 类描述：佣金相关总汇数据使用对象
 * 创建日期：2024/2/1 8:51
 */
@Data
public class OrderItemAndCommissionDTO {
    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;
    /**
     * 已收金额
     */
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;
    /**
     * 成本
     */
    private BigDecimal namountcost;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 计提金额
     */
    private BigDecimal ncommission;
}

