package com.hgzp.pagemodel.ad;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AdOrderItemCostVO
 * 创建人：lhl
 * 类描述：明细订单成本汇总对象
 * 创建日期：2024/1/10 10:34
 */
@Data
public class AdOrderItemCostVO {
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 订单编号
     */
    private String sordernum;

    /**
     * 广告项目名称
     */
    private String adprojectname;
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
     * 媒体
     */
    private String medianame;

    /**
     * 预见报开始日期
     */
    private Date prestartdate;

    /**
     * 广告标题
     */
    private String sadtitle;

    /**
     * 成本
     */
    private BigDecimal namountcost;

    /**
     * 说明
     */
    private String sdescription;


    /**
     * 业务员名称
     */
    private String createempname;

    /**
     * 报送时间
     */
    private Date dcreatedate;



}


