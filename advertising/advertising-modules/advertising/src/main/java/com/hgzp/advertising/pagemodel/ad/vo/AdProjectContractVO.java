package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.math.BigDecimal;

/**
 * AdProjectContractVO
 * 创建人：lhl
 * 类描述：项目相关合同
 * 创建日期：2024/3/6 10:28
 */
@Data
public class AdProjectContractVO {
    /**
     * id
     */
    private Long id;

    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 订单编号
     */
    private String sordernum;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 代理公司名称
     */
    private String agencyname;
    /**
     * 内容生产方
     */
    private String agentname;
    /**
     * 行业名称
     */
    private String adindustryname;
    /**
     * 主业务员名称
     */
    private String employname;
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


