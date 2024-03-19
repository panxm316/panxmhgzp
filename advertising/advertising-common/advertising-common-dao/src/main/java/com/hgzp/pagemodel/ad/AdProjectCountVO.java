package com.hgzp.pagemodel.ad;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AdProjectCountVO
 * 创建人：lhl
 * 类描述：广告项目汇总结果对象
 * 创建日期：2024/1/3 13:04
 */
@Data
public class AdProjectCountVO {
    /**
     * 广告项目id
     */
    private Long id;

    /**
     * 项目名称
     */
    private String sname;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 项目说明
     */
    private String sremark;

    /**
     * 预算
     */
    private BigDecimal nprojectbudget;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

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
     * 业绩金额
     */
    private BigDecimal amountachievement;

    /**
     * 预算成本
     */
    private BigDecimal nprojectcost;

    /**
     * 实际成本
     */
    private BigDecimal costamount;

    /**
     * 历史数据
     */
    private BigDecimal history;

    /**
     * 去年已完成
     */
    private BigDecimal finshedlastyear;

    /**
     * 去年未完成
     */
    private BigDecimal nofinshedlastyear;

    /**
     * 今年已完成
     */
    private BigDecimal finshedthisyear;

    /**
     * 今年未完成
     */
    private BigDecimal nofinshedthisyear;

    /**
     * 明年未完成
     */
    private BigDecimal nofinshednextyear;

}


