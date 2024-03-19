package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.advertising.pagemodel.common.UpFileModel;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twworkreports;
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
 * WorkReportsDTO
 * 创建人：suny
 * 类描述：工作报告实体对象
 * 创建日期：2023/10/28 16:46
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkReportsDTO extends BaseDTO {
    @NotNull(message = "工作报告id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long deptid;

    /**
     * 部门
     */
    @NotBlank(message = "部门不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String deptname;

    /**
     * 人员ID
     */
    @NotNull(message = "人员id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long employid;

    /**
     * 人员名称
     */
    @NotBlank(message = "人员名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String employname;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 工作方式(电话、微信、上门、综合、其他)
     */
    @NotBlank(message = "工作方式不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sworkmode;

    /**
     * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
     */
    @NotNull(message = "报告类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Integer iworktype;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 工作内容
     */
    private String sworkcontent;

    /**
     * 服务反馈
     */
    private String sfeedback;

    /**
     * 工作计划
     */
    private String splan;

    /**
     * 工作难点
     */
    private String squestions;

    /**
     * 审批状态
     */
    private Integer iapprovestatus;

    /**
     * 审批人id
     */
    private Long icheckerid;

    /**
     * 审批人名称
     */
    private String scheckername;

    /**
     * 审批日期
     */
    private Date dcheckdate;

    /**
     * 审批意见
     */
    private String scheckopinions;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
    /**
     * 上传的附件
     */
    private List<WorkReportFilesDTO> filelist;

    public WorkReportsDTO(Twworkreports twworkreports){
        BeanUtils.copyProperties(twworkreports, this);
    }
}
