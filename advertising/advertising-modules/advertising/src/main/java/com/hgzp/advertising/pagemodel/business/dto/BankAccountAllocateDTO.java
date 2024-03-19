package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * BankAccountAllocateDTO
 * 创建人：suny
 * 类描述：银行流水分配表DTO
 * 创建日期：2023/12/7 10:44
 */
@Data
public class BankAccountAllocateDTO extends BaseDTO {
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
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 流水表id
     */
    private Long bankaccountid;

    /**
     * 发票表id
     */
    private Long invoiceid;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 分配金额
     */
    private BigDecimal namountallcate;

    /**
     * 原有分配金额（分配修改使用）
     */
    private BigDecimal oldnamountallcate;

    /**
     * 是否指定订单
     */
    private Boolean bassignorder;

    /**
     * 指定订单id
     */
    private Long orderid;

    /**
     * 合同号
     */
    private String scontractnum;

    /**
     * 说明
     */
    private String sdescription;

    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    private Integer istatus;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 最后操作员id
     */
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    private String lastoperator;

    /**
     * 最后修改日期
     */
    private Date dlastmodifydate;

    /**
     * 并发标记
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
}

