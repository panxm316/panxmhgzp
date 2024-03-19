package com.hgzp.advertising.pagemodel.business.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.model.Twworkreports;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TaskReportsDTO
 * 创建人：lhl
 * 类描述：任务额度实体对象
 * 创建日期：2023/11/9 9:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskQuotaDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "任务设置id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 操作员id
     */
    @NotNull(message = "操作员id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long operatorid;

    /**
     * 操作员
     */
    @NotNull(message = "操作员名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String operatorname;

    /**
     * 任务分类：年度、月度
     */
    @NotNull(message = "任务分类不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String stasktype;

    /**
     * 人员分类：部门、人员
     */
    @NotNull(message = "任务类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String spersonaltype;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门
     */
    private String deptname;

    /**
     * 人员id
     */
    private Long employid;

    /**
     * 人员
     */
    private String employname;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体
     */
    private String medianame;

    /**
     * 任务日期 年：2023 月2023-01
     */
    @NotNull(message = "任务日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String staskdate;

    /**
     * 任务额度（万元）
     */
    private BigDecimal ntaskamount;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 启用标记
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;

    public TaskQuotaDTO(Twtasks twtasks){
        BeanUtils.copyProperties(twtasks, this);
    }

}


