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
 * 预开发票申请表
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Getter
@Setter
@LogData(alias = "预开发票申请表")
public class Twpreinvoiceapplication implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门id
     */
    @LogData(alias = "部门id" , mappedBy = "tbdept", mappedByColumn = "sdeptname")
    private Long deptid;

    /**
     * 部门名称
     */
    private String deptname;

    /**
     * 业务员id
     */
    @LogData(alias = "业务员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 业务员名称
     */
    private String employname;

    /**
     * 客户id
     */
    @LogData(alias = "客户id", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @LogData(alias = "客户类型")
    private Integer icusttype;

    /**
     * 申请时间
     */
    @LogData(alias = "申请时间")
    private Date dapplytime;

    /**
     * 申请金额
     */
    @LogData(alias = "申请金额")
    private BigDecimal namountapply;

    /**
     * 开票项目id
     */
    @LogData(alias = "开票项目id", mappedBy = "tbadprintitem", mappedByColumn = "sname")
    private Long printitemid;

    /**
     * 开票项目
     */
    private String printitemname;

    /**
     * 已还金额
     */
    @LogData(alias = "已还金额")
    private BigDecimal namountreceived;

    /**
     * 申请类型(1-预开 2-挂开)
     */
    @LogData(alias = "申请类型(1-预开 2-挂开)")
    private Integer iapplytype;

    /**
     * 开票类型(82-数电普票 81-数电专票)
     */
    @LogData(alias = "申请类型(82-数电普票 81-数电专票)")
    private Integer iinvoicetype;

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
     * 发票编号
     */
    @LogData(alias = "发票编号")
    private String invoicecode;

    /**
     * 开票名称
     */
    @LogData(alias = "开票名称")
    private String sprintname;

    /**
     * 客户识别号
     */
    @LogData(alias = "客户识别号")
    private String spayercreditcode;

    /**
     * 客户地址电话
     */
    @LogData(alias = "客户地址电话")
    private String spayeraddr;

    /**
     * 客户银行账户
     */
    @LogData(alias = "客户银行账户")
    private String spayerbank;

    /**
     * 经营主体ID
     */
    @LogData(alias = "经营主体id", mappedBy = "tbbusinessentity", mappedByColumn = "sname")
    private Long businessentityid;

    /**
     * 经营主体
     */
    private String businessentityname;

    /**
     * 工作流申请表id
     */
    @LogData(alias = "审批流程id")
    private String applicationid;

    /**
     * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
     */
    @LogData(alias = "审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)")
    private Integer iapprovestatus;

    /**
     * 审批意见（最后一次审批意见）
     */
    @LogData(alias = "审批意见")
    private String sapprovalopinions;

    /**
     * 操作员id
     */
    @LogData(alias = "操作人员", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long operatorid;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 广告项目id
     */
    @LogData(alias = "广告项目id", mappedBy = "twadproject", mappedByColumn = "sname")
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
