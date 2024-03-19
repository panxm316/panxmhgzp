package com.hgzp.pagemodel.ad;

import lombok.Data;

import java.math.BigDecimal;

/**
 * AdSummaryAllVO
 * 创建人：lhl
 * 类描述：广告明细订单应收金额、欠款金额、
 * 创建日期：2024/3/6 12:41
 */
@Data
public class AdSummaryAllVO {
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
}


