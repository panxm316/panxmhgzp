package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twadresourceapplication;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * AdResourceApplicationDTO
 * 创建人：suny
 * 类描述：广告资源申请表DTO
 * 创建日期：2023/11/11 13:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResourceApplicationDTO extends BaseDTO {
    /**
     * 流程id
     */
    private String flowid;
    /**
     * 主键
     */
    @NotNull(message = "广告资源申请表id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long deptid;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String deptname;

    /**
     * 业务员id
     */
    @NotNull(message = "业务员id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long employid;

    /**
     * 业务员名称
     */
    @NotBlank(message = "业务员名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String employname;

    /**
     * 客户id
     */
    @NotNull(message = "客户id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long customerid;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String customername;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @NotNull(message = "客户类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Integer icusttype;

    /**
     * 申请时间
     */
    private Date dapplytime;

    /**
     * 开始使用日期
     */
    @NotNull(message = "开始使用日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Date dstartdate;

    /**
     * 停止使用日期
     */
    private Date denddate;

    /**
     * 广告标题
     */
    @NotBlank(message = "广告标题不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sadtitle;

    /**
     * 广告内容
     */
//    @NotBlank(message = "广告内容不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sadcontent;

    /**
     * 申请表id
     */
    private String applicationid;

    /**
     * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
     */
    @NotNull(message = "审批状态不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Integer iapprovestatus;

    /**
     * 最后一次审批意见
     */
    private String sapprovalopinions;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 第三方合同ID(预留)
     */
    private String contractid;

    /**
     * 是否快速上版标记
     */
    @NotNull(message = "是否快速上版标记不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Boolean bquickly;

    /**
     * 一审意见
     */
    @LogData(alias = "一审意见")
    private String sfirstopinion;

    /**
     * 并发标志
     */
    private Long version;
    /**
     * 广告资源文件列表
     */
    private List<AdResourceFilesDTO> filelist;

    public AdResourceApplicationDTO(Twadresourceapplication twadresourceapplication) {
        BeanUtils.copyProperties(twadresourceapplication, this);
    }
}
