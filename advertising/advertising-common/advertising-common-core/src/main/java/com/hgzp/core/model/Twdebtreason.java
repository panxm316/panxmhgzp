package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 欠款原因表
 * </p>
 *
 * @author wwk
 * @since 2023-11-17
 */
@Getter
@Setter
@LogData(alias = "欠款原因表")
public class Twdebtreason implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门id
     */
    @LogData(alias = "部门id")
    private Long deptid;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 主业务员id
     */
    @LogData(alias = "主业务员id")
    private Long employid;

    /**
     * 主业务员名称
     */
    @LogData(alias = "主业务员名称")
    private String employname;

    /**
     * 代理公司业务员id
     */
    @LogData(alias = "代理公司业务员id")
    private Long agencyemployid;

    /**
     * 代理公司业务员名称
     */
    @LogData(alias = "代理公司业务员名称")
    private String agencyemployname;

    /**
     * 内容生产方业务员id
     */
    @LogData(alias = "内容生产方业务员id")
    private Long agentemployid;

    /**
     * 内空生产方业务员名称
     */
    @LogData(alias = "内空生产方业务员名称")
    private String agentemployname;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date createdate;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 代理公司
     */
    @LogData(alias = "代理公司")
    private String agencyname;

    /**
     * 内容生产方名称
     */
    @LogData(alias = "内容生产方名称")
    private String agentname;

    /**
     * 广告表id
     */
    @LogData(alias = "广告表id")
    private Long orderid;

    /**
     * 刊期表id
     */
    @LogData(alias = "刊期表id")
    private Long orderitemid;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 刊发日期
     */
    @LogData(alias = "刊发日期")
    private Date dpublishdate;

    /**
     * 广告标题
     */
    @LogData(alias = "广告标题")
    private String sadtitle;

    /**
     * 应收金额
     */
    @LogData(alias = "应收金额")
    private BigDecimal amountreceivable;

    /**
     * 已收金额
     */
    @LogData(alias = "已收金额")
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    @LogData(alias = "欠款金额")
    private BigDecimal amountarrearage;

    /**
     * 发票金额
     */
    @LogData(alias = "发票金额")
    private BigDecimal namountinvoice;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 催告情况
     */
    @LogData(alias = "催告情况")
    private String snoticecontent;

    /**
     * 欠款原因
     */
    @LogData(alias = "欠款原因")
    private String sdebtreason;

    /**
     * 归类原因
     */
    @LogData(alias = "归类原因")
    private String scategory;

    /**
     * 计划回款时间
     */
    @LogData(alias = "计划回款时间")
    private String srepaymentdate;

    /**
     * 风险等级
     */
    @LogData(alias = "风险等级")
    private String srisklevel;

    /**
     * 法务介入 默认0 否 1是
     */
    @LogData(alias = "法务介入")
    private Boolean blegal;

    /**
     * 填报日期
     */
    @LogData(alias = "填报日期")
    private Date dreportdate;

    /**
     * 是否确认 默认0 否 1是
     */
    @LogData(alias = "是否确认")
    private Boolean bconfirm;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
