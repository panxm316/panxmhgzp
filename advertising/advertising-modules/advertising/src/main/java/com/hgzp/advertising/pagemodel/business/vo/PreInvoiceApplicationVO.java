package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.customer.vo.ProcessInstanceVo;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PreInvoiceApplicationVO
 * 创建人：yanz
 * 类描述：预开发票申请表 展示vo
 * 创建日期：2023/11/8 9:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreInvoiceApplicationVO extends BaseQueryInfo {
    private Long id;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 客户名称
     */
    private String customername;

//    /**
//     * 合同号
//     */
//    private String contractNum;
    /**
     * 合同VO
     */
    @NotNull(message = "合同号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private List<OrderforPreinvoapplyVO> contractVos;

//    /**
//     * 订单id
//     */
//    @NotNull(message = "orderid不能为空", groups = {ValidateParam.edit.class})
//    private Long orderid;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门名称
     */
    private String deptname;

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
     * 客户类型
     */
    private Integer icusttype;

    /**
     * 客户识别号
     */
    private String spayercreditcode;

    /**
     * 客户地址电话
     */
    private String spayeraddr;

    /**
     * 客户银行及账户
     */
    private String spayerbank;

    /**
     * 申请日期
     */
    private Date dapplytime;

    /**
     * 申请金额
     */
    private BigDecimal namountapply;

    /**
     * 开票项目id
     */
    private Long printitemid;

    /**
     * 开票项目名称
     */
    private String printitemname;

    /**
     * 已还金额
     */
    private BigDecimal namountreceived;

    /**
     * 开票名称
     */
    private String sprintname;

    /**
     * 经营主体ID
     */
    private Long businessentityid;
    /**
     * 经营主体名称
     */
    private String businessentityname;

    /**
     * 申请表id
     */
    private Long applicationid;

    /**
     * 审批状态
     */
    private Integer iapprovestatus;

    /**
     * 申请类型：1-预开、2-挂开
     */
    private Integer iapplytype;

    /**
     * 开票类型(82-数电普票 81-数电专票)
     */
    private Integer iinvoicetype;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 广告项目id
     */
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 申请表相关文件
     */
    private List<PreinvoiceApplicationFilesVO> applyfiles;

    /**
     * 仅欠款
     */
    private Boolean debtOnly;

    /**
     * 历史流程
     */
    private  List<ProcessInstanceVo> customerProcessInstance;

    /**
     * 审批意见
     */
    private String sapprovalopinions;
}