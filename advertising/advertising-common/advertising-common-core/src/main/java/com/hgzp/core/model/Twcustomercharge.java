package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户收费表
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Getter
@Setter
@LogData(alias = "客户收费")
public class Twcustomercharge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "customername")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
    private Date dcreatetime;

    /**
     * 业务员id
     */
    @LogData(alias = "业务员id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 业务员名称
     */
    @LogData(alias = "业务员名称")
    private String employname;

    /**
     * 客户id
     */
    @LogData(alias = "客户id", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 入账金额
     */
    @LogData(alias = "入账金额")
    private BigDecimal namountreceived;

    /**
     * 已用金额
     */
    @LogData(alias = "已用金额")
    private BigDecimal namounspent;

    /**
     * 剩余金额
     */
    @LogData(alias = "剩余金额")
    private BigDecimal namountbalance;

    /**
     * 付款时间
     */
    @LogData(alias = "付款时间")
    private Date dpaydate;

    /**
     * 0-预收款 1-直接收款 2-银行流水
     */
    @LogData(alias = "0-预收款 1-直接收款 2-银行流水")
    private Integer itype;

    /**
     * 直接收款、预收款、银行流水
     */
    @LogData(alias = "直接收款、预收款、银行流水")
    private String stype;

    /**
     * 银行流水id
     */
    @LogData(alias = "银行流水id")
    private Long bankaccountid;

    /**
     * 预开发票申请表id
     */
    @LogData(alias = "预开发票申请表id")
    private Long preinvoiceapplicationid;

    /**
     * 是否指定订单
     */
    @LogData(alias = "是否指定订单")
    private Boolean bassignorder;

    /**
     * 指定订单id
     */
    @LogData(alias = "指定订单id")
    private Long orderid;

    /**
     * 指定订单合同号
     */
    @LogData(alias = "指定订单合同号")
    private String scontractnum;

    /**
     * 描述
     */
    @LogData(alias = "描述")
    private String sdescription;

    /**
     * 付款方式ID
     */
    @LogData(alias = "付款方式ID", mappedBy = "tbpaymethod", mappedByColumn = "sname")
    private Long paymethodid;

    /**
     * 付款方式
     */
    @LogData(alias = "付款方式")
    private String paymethodname;

    /**
     * 发票id
     */
    @LogData(alias = "发票id")
    private Long invoiceid;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    @LogData(alias = "状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）")
    private Integer istatus;

    /**
     * 最后修改日期
     */
    @LogData(alias = "最后修改日期")
    private Date dlastmodifydate;

    /**
     * 最后操作员id
     */
    @LogData(alias = "最后操作员id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    @LogData(alias = "最后操作员")
    private String lastoperator;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
