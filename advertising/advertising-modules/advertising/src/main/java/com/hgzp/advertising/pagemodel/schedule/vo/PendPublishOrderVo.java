package com.hgzp.advertising.pagemodel.schedule.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.mapper.ad.TworderitemMapper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PendPublishOrderVo
 * 创建人：lhl
 * 类描述：待处理刊发的广告订单明细
 * 创建日期：2023/12/14 10:58
 */
@Data
public class PendPublishOrderVo {
    /**
     * id
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


