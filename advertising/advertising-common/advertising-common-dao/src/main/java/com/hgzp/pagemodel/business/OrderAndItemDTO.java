package com.hgzp.pagemodel.business;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * orderAndItemDTO
 * 创建人：suny
 * 类描述：TODO 成本核对相关订单明细DTO
 * 创建日期：2023/12/13 14:52
 */
@Data
public class OrderAndItemDTO {
    // <editor-fold desc="order字段">
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 广告项目名称
     */
    private String adprojectname;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 业务员id
     */
    private Long employid;

    /**
     * 业务员名称
     */
    private String employname;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 客户id
     */
    private String customerid;

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
    // </editor-fold>
    // <editor-fold desc="orderitem字段">
    /**
     * 订单明细id
     */
    private Long orderitemid;
    /**
     * 媒体类型名称
     */
    private String mediatypename;
    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 媒体id
     */
    private String mediaid;
    /**
     * 预见报开始日期（刊发日期）
     */
    private Date prestartdate;
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
     * 成本金额
     */
    private BigDecimal namountcost;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 业绩时间(用于统计)
     */
    private Date dachievementdate;
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
    /**
     * 宽
     */
    private BigDecimal nwidth;

    /**
     * 高
     */
    private BigDecimal nheight;

    /**
     * 星期名称
     */
    private String weeksettingname;
    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 版面名称
     */
    private String foldpageplanname;
    // </editor-fold>

    /**
     * 经营主体名称（关联发票表找到）
     */
    private String businessentityname;
    /**
     * 关联发票号(英文逗号分隔)
     */
    private String invoices;
    /**
     * 刊期编号(自增列,广告号)
     */
    private Long iitemcode;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 是否可编辑
     */
    private Boolean bEditFlag;


}

