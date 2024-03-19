package com.hgzp.advertising.pagemodel.ad.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * OrderDetails
 * 创建人：lenovo
 * 类描述：TODO
 * 创建日期：2024/1/8 9:05
 */
@Data
public class OrderDetails {
    /**
     * 明细订单id
     */
    private String id;
    /**
     * 合同号
     */
    private String scontractnum;
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
     * 广告标题
     */
    private String sadtitle;
    /**
     * 预见报开始日期
     */
    private Date prestartdate;
    /**
     * 录入日期
     */
    private Date createtime;
    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 叠次版本名称
     */
    private String foldareavername;
    /**
     * 广告形式名称
     */
    private String addisplayformname;
    /**
     * 版面类别名称
     */
    private String pagesortname;
    /**
     * 色彩名称
     */
    private String adcolorname;
    /**
     * 规格名称
     */
    private String adspecname;

}


