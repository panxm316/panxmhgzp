package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * OrderforPreinvoapplyVO
 * 创建人：yanz
 * 类描述：（预开发票申请用）合同信息VO
 * 创建日期：2023/11/30 14:20
 */
@Data
public class OrderforPreinvoapplyVO extends BaseQueryInfo {
    /** 订单id - tworder */
    private Long orderid;
    /** 合同编号 - tworder */
    private String scontractnum;
    /** 广告标题 - tworder */
    private String sadtitle;
    /** 广告分类 - tworder */
    private Long adtypeid;
    private String adtypename;
    /** 欠款金额 - tworderitem */
    private BigDecimal amountarrearage;
    /**
     * 广告行业 - tworder
     */
    private Long adindustryid;
    private String adindustryname;
    /** 经营主体 - tworder  */
    private Long businessentityid;
    private String businessentityname;
    /** 客户id - tworder */
    private Long customerid;
    /** 客户名称 - tworder */
    private String customername;
    /** 代理公司id - tworder */
    private String agencytid;
    /** 代理公司名称 - tworder */
    private String agencyname;
    /** 内容生产方id - tworder */
    private String agentid;
    /** 内容生产方名称 - tworder */
    private String agentname;

}

