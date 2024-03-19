package com.hgzp.advertising.pagemodel.business.dto;

import cn.hutool.core.util.ObjUtil;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Twpreinvoiceapplicationfile;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PreInvoiceApplicationDTO
 * 创建人：yanz
 * 类描述：预开发票申请表DTO
 * 创建日期：2023/11/11 10:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreInvoiceApplicationDTO extends BaseDTO {
    /**
     * 申请表id
     */
    @NotNull(message = "id不能为空", groups = {ValidateParam.edit.class})
    private Long id;

    /**
     * 部门id
     */
//    @NotNull(message = "部门id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long deptid;

    /**
     * 部门名称
     */
//    @NotBlank(message = "部门名称不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String deptname;

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
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @NotNull(message = "客户类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer icusttype;

    /**
     * 合同VO
     */
    @NotNull(message = "合同号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private List<OrderforPreinvoapplyVO> contractVos;

    /**
     * 关联的订单刊期 - 预开发票核销用
     */
    private List<Tworderitem> orderItems;

    @NotNull(message = "合同id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long orderid;


    /**
     * 申请时间
     */
    @NotNull(message = "申请时间不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Date dapplytime;

    /**
     * 申请金额
     */
    @NotNull(message = "申请金额不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private BigDecimal namountapply;

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
     * 申请类型 1-预开 2-挂开
     */
    @NotNull(message = "开票类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer iapplytype;

    /**
     * 开票类型(开票样式)： 2-普通发票 0-专用发票 51-电子发票 81-数电专票 82-数电普票
     */
    @NotNull(message = "开票类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer iinvoicetype;
    /**
     * 已还金额
     */
    @NotNull(message = "已还金额不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private BigDecimal namountreceived;

    /**
     * 申请类型 1-预开 2-挂开
     */
    @NotNull(message = "开票类型不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Integer itype;

    /**
     * 发票号
     */
//    @NotBlank(message = "发票号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String invoice;
    /**
     * 发票id
     */
    private Long invoiceid;

    /**
     * 发票编号
     */
//    @NotBlank(message = "发票编号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String invoicecode;

    /**
     * 开票名称
     */
    @NotBlank(message = "开票名称不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String sprintname;

    /**
     * 客户识别号
     */
    @NotBlank(message = "客户识别号不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String spayercreditcode;

    /**
     * 客户地址电话
     */
    @NotBlank(message = "客户地址电话不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String spayeraddr;

    /**
     * 客户银行账户
     */
    @NotBlank(message = "客户银行账户不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private String spayerbank;

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
     * 申请表id
     */
    @NotNull(message = "申请表id不能为空", groups = {ValidateParam.add.class, ValidateParam.edit.class})
    private Long applicationid;

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
     * 上传附件
     */
    private List<Twpreinvoiceapplicationfile> applyfiles;

    /**
     * 预开发票申请并发标志
     */
    @NotNull(message = "预开发票申请并发标志不能为空", groups = {ValidateParam.edit.class})
    private Long version;

    /**
     * 发票关联欠款订单信息列表
     */
    private List<OrderDebtDTO> orders;

    /**
     * 真实开票日期（关联发票表读取）
     */
    private Date realInvoiceDate;

    public List<Long> getOrderIds() {
        if (ObjUtil.isEmpty(getContractVos())) {
            return null;
        }
        return getContractVos().stream()
                .map(OrderforPreinvoapplyVO::getOrderid)
                .collect(Collectors.toList());
    }
}