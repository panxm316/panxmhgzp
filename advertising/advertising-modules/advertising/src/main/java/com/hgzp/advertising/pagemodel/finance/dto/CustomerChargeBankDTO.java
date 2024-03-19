package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * CustomerChargeBankDTO
 * 创建人：suny
 * 类描述：银行流水分配使用的客户收款DTO
 * 创建日期：2023/12/20 13:07
 */
@Data
public class CustomerChargeBankDTO extends BaseDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建者id
     */
    private Long createempid;

    /**
     * 创建者名称
     */
    private String createempname;

    /**
     * 创建时间
     */
    private Date dcreatetime;

    /**
     * 业务员id
     */
    private Long employid;

    /**
     * 业务员名称
     */
    private String employname;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 入账金额
     */
    private BigDecimal namountreceived;
    /**
     * 原有分配金额（分配修改使用）
     */
    private BigDecimal oldnamountreceived;

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
    private Integer itype;

    /**
     * 直接收款、预收款、银行流水
     */
    private String stype;

    /**
     * 银行流水id
     */
    private Long bankaccountid;

    /**
     * 预开发票申请表id
     */
    private Long preinvoiceapplicationid;

    /**
     * 是否指定订单
     */
    private Boolean bassignorder;

    /**
     * 指定订单id
     */
    private Long orderid;

    /**
     * 指定订单合同号
     */
    private String scontractnum;

    /**
     * 描述
     */
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
     * 发票id
     */
    private Long invoiceid;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    private Integer istatus;

    /**
     * 最后修改日期
     */
    private Date dlastmodifydate;

    /**
     * 最后操作员id
     */
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    private String lastoperator;

    /**
     * 客户收费表并发标志
     */
    private Long version;

    /**
     * 欠款订单列表
     */
    private List<OrderDebtDTO> orders;
    /**
     * 借方名称
     */
    private String sborrowername;
    /**
     * 贷方名称
     */
    private String slendername;
    /**
     * 发票申请金额
     */
    private BigDecimal namountapply;

    /**
     * 经营主体名称（关联发票表找到）
     */
    private String businessentityname;
    /**
     * 开票时间（关联发票创建时间）
     */
    private Date dinvoicecreatetime;
    /**
     * 开票项目（关联发票表找到）
     */
    private String printitemname;
    /**
     * 编号(自增列，关联客户表获取)
     */
    private Integer icode;
    /**
     * 类型(0-直接客户、1-代理公司、2-内容生产方，关联客户表获取)
     */
    private Integer customeritype;
    /**
     * 银行流水单表并发标志
     */
    private Long versionBankAccount;

    /**
     * 订单刊期ids（财务人员直接核销时使用）
     */
    private String itemids;
    /**
     * 核销日期（财务人员直接核销时使用）
     */
    private String dateString;
}

