package com.hgzp.advertising.pagemodel.finance.vo;

import cn.hutool.core.bean.BeanUtil;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Twcustomercharge;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * CustomerChargeVO
 * 创建人：wangwk
 * 类描述：客户收款明细查询、展示vo
 * 创建日期：2023/10/28 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChargeVO extends BaseQueryInfo {


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
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 业务员id
     */
    private Long employid;

    /**
     * 业务员名称
     */
    private String employname;
    /**
     * 入账金额
     */
    private BigDecimal namountreceived;

    /**
     * 已用金额
     */
    private BigDecimal namounspent;

    /**
     * 剩余金额
     */
    private BigDecimal namountbalance;

    /**
     * 付款时间
     */
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
    private Long paymethodid;

    /**
     * 付款方式
     */
    private String paymethodname;
    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    @LogData(alias = "状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）")
    private Integer istatus;

    /**
     * 收款明细并发标志
     */
    private Long version;

    /**
     * 经营主体id
     */
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    private String businessentityname;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 发票编码
     */
    private String invoicecode;

    /**
     * 发票id
     */
    private Long invoiceid;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    private Integer icusttype;

    /**
     * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
     */
    private Integer itypeinvoice;

    /**
     * 发票并发标志
     */
    private Long invoiceversion;

    /**
     * 开票项目id
     */
    private Long printitemid;

    /**
     * 开票项目
     */
    private String printitemname;

    //=====================================
    /**
     * 客户类型名称
     */
    private String custtypeName;

    /**
     * 创建时间
     */
    private Date dcreatetime;

    /**
     * 金额范围大于等于
     */
    private BigDecimal namountreceivedge;

    /**
     * 金额范围小于等于
     */
    private BigDecimal namountreceivedle;

    public CustomerChargeVO(Twcustomercharge twcustomercharge) {
        BeanUtil.copyProperties(twcustomercharge, this);
    }

}
