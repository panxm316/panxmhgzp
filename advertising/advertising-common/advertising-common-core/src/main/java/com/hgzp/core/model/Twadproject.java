package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告项目
 * </p>
 *
 * @author wwk
 * @since 2023-09-21
 */
@Getter
@Setter
@LogData(alias = "广告项目")
public class Twadproject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目编号
     */
    @LogData(alias = "项目编号")
    private String projectcode;

    /**
     * 创建人id
     */
    @LogData(alias = "创建人id")
    private Long createempid;

    /**
     * 创建人名称
     */
    @LogData(alias = "创建人名称")
    private String screatename;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
    private Date dcreatedate;

    /**
     * 名称(标题)
     */
    @LogData(alias = "名称(标题)")
    private String sname;

    /**
     * 申请人id
     */
    @LogData(alias = "申请人id")
    private Long employid;

    /**
     * 申请人名称
     */
    @LogData(alias = "申请人名称")
    private String employname;

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
     * 项目成本
     */
    @LogData(alias = "项目成本")
    private BigDecimal nprojectcost;

    /**
     * 结项标记
     */
    @LogData(alias = "结项标记")
    private Boolean bprojectcomplete;

    /**
     *  经营主体id
     */
    @LogData(alias = " 经营主体id")
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    @LogData(alias = "经营主体名称")
    private String businessentityname;

    /**
     *  是否付款开票
     */
    @LogData(alias = " 是否付款开票")
    private Boolean bpayed;

    /**
     * 合同类型(销售合同、采购合同、互换合同、框架合同)
     */
    @LogData(alias = "合同类型(销售合同、采购合同、互换合同、框架合同)")
    private Integer icontracttype;

    /**
     * 销售合同类型(常规合同、认刊书)
     */
    @LogData(alias = "销售合同类型(常规合同、认刊书)")
    private Integer isalecontracttype;

    /**
     * 合同编号
     */
    @LogData(alias = "合同编号")
    private String scontractnum;

    /**
     * 业绩归属人员id
     */
    @LogData(alias = "业绩归属人员id")
    private Long newbelongid;

    /**
     * 业绩归属人员名称
     */
    @LogData(alias = "业绩归属人员名称")
    private String newbelongname;

    /**
     * 合同名称
     */
    @LogData(alias = "合同名称")
    private String scontractname;

    /**
     * 用章类型(多选)(公章、合同专用章、经营合同专用章、法人章等)
     */
    @LogData(alias = "用章类型(多选)(公章、合同专用章、经营合同专用章、法人章等)")
    private Integer istamptype;

    /**
     * 是否委托授权人
     */
    @LogData(alias = "是否委托授权人")
    private Boolean bauthorizer;

    /**
     * 授权人名称
     */
    @LogData(alias = "授权人名称")
    private String authorizername;

    /**
     * 合同总金额(项目预算)
     */
    @LogData(alias = "合同总金额(项目预算)")
    private BigDecimal nprojectbudget;

    /**
     * 开票总金额
     */
    @LogData(alias = "开票总金额")
    private BigDecimal invoiceamount;

    /**
     * 支付总金额
     */
    @LogData(alias = "支付总金额")
    private BigDecimal payedamount;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "结束日期")
    private Date denddate;

    /**
     * 我方交易资源
     */
    @LogData(alias = "我方交易资源")
    private String myreource;

    /**
     * 我方物品估值
     */
    @LogData(alias = "我方物品估值")
    private String myresourceworth;

    /**
     * 对方交易资源
     */
    @LogData(alias = "对方交易资源")
    private String sideresource;

    /**
     * 对方物品估值
     */
    @LogData(alias = "对方物品估值")
    private String sireresourceworth;

    /**
     * 配备资源
     */
    @LogData(alias = "配备资源")
    private String equipresource;

    /**
     * 资源赠送或返点赠送
     */
    @LogData(alias = "资源赠送或返点赠送")
    private String giveresource;

    /**
     * 重点注意条款及备注
     */
    @LogData(alias = "重点注意条款及备注")
    private String sprojectcontent;

    /**
     * 审批流程id
     */
    @LogData(alias = "审批流程id")
    private String applicationid;

    /**
     * 审核状态(0待审1在审2通过3否决4撤销5无效)
     */
    @LogData(alias = "审核状态(0待审1在审2通过3否决4撤销5无效)")
    private Integer iapprovestatus;

    /**
     * 最后一次审批意见
     */
    @LogData(alias = "最后一次审批意见")
    private String sapprovalopinions;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;
}
