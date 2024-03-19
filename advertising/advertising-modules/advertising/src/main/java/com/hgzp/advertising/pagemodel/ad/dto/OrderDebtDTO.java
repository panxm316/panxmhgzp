package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * OrderDebtDTO
 * 创建人：suny
 * 类描述：欠款统计对象DTO
 * 创建日期：2023/11/21 10:54
 */
@Data
public class OrderDebtDTO extends BaseDTO {
    /**
     * orderitem主键
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderid;

    /**
     * 订单编号
     */
    private String sordernum;

    /**
     * 广告项目id
     */
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 合同号
     */
    private String scontractnum;

    /**
     * 创建日期
     */
    private Date createtime;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 主业务员名称
     */
    private String employname;

    /**
     * 代理公司id
     */
    private Long agencytid;

    /**
     * 代理公司名称
     */
    private String agencyname;

    /**
     * 代理公司业务员id
     */
    private Long agencyemployid;

    /**
     * 代理公司业务员名称
     */
    private String agencyemployname;

    /**
     * 内容生产方id
     */
    private Long agentid;

    /**
     * 内容生产方
     */
    private String agentname;

    /**
     * 内容生产方业务员id
     */
    private Long agentemployid;

    /**
     * 内空生产方业务员名称
     */
    private String agentemployname;


    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 预见报开始日期
     */
    private Date prestartdate;

    /**
     * 预见报结束日期
     */
    private Date preenddate;
    /**
     * 规格id
     */
    private Long adspecid;

    /**
     * 规格名称
     */
    private String adspecname;
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
     * 发票号
     */
    private String invoice;
    /**
     * 申请金额
     */
    private BigDecimal napplyamount;
    /**
     * 催告情况
     */
    private String snoticecontent;

    /**
     * 欠款原因
     */
    private String sdebtreason;

    /**
     * 归类原因
     */
    private String scategory;

    /**
     * 计划回款时间
     */
    private String srepaymentdate;

    /**
     * 风险等级
     */
    private String srisklevel;

    /**
     * 法务介入 默认0 否 1是
     */
    private Boolean blegal;
    /**
     * 是否填报欠款原因(是否推送) 默认0 否 1是
     */
    private Boolean breportreason;

    /**
     * 部门名称
     */
    private String deptname;
    /**
     * 是否确认 默认0 否 1是
     */
    private Boolean bconfirm;

    /**
     * 并发标志
     */
    private Long version;
    /**
     * 欠款原因表id
     */
    private Long debtreasonid;
    /**
     * 刊期表id
     */
    private Long orderitemid;
}
