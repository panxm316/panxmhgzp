package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CustomerChargeDTO
 * 创建人：wangwk
 * 类描述：客户预收款dto
 * 创建日期：2023/10/28 12:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChargeDTO {

    /**
     * 收费表id
     */
    @NotNull(message = "id不能为空", groups = {ValidateParam.edit.class})
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
     * 客户id
     */
    @NotNull(message = "客户id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long customerid;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String customername;

    /**
     * 入账金额
     */
    @NotNull(message = "入账金额不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private BigDecimal namountreceived;

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
    @NotNull(message = "付款方式id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long paymethodid;

    /**
     * 付款方式
     */
    @NotBlank(message = "付款方式不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String paymethodname;


    /**
     * 业务员id
     */
    @NotNull(message = "业务员id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long employid;

    /**
     * 业务员名称
     */
    @NotBlank(message = "业务员名称不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String employname;
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
    private String sremark;
    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    @LogData(alias = "状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）")
    private Integer istatus;

    /**
     * 收款明细并发标志
     */
    @NotNull(message = "收款明细并发标志不能为空", groups = {ValidateParam.edit.class})
    private Long version;


    //============发票相关=========

    //region 发票相关
    /**
     * 开票项目id
     */
    @NotNull(message = "开票项目id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long printitemid;

    /**
     * 开票项目
     */
    @NotBlank(message = "开票项目不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String printitemname;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @NotNull(message = "客户类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer icusttype;

    /**
     * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
     */
    @NotNull(message = "发票类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer itypeinvoice;

    /**
     * 经营主体id
     */
    @NotNull(message = "经营主体id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    @NotBlank(message = "经营主体名称不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String businessentityname;

    /**
     * 发票并发标志
     */
    @NotNull(message = "发票并发标志不能为空", groups = {ValidateParam.edit.class})
    private Long invoiceversion;
    //endregion

}
