package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * TaskQuotaAddDTO
 * 创建人：lhl
 * 类描述：批量添加任务额度请求对象
 * 创建日期：2023/11/10 9:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskQuotaAddDTO extends BaseDTO {

    /**
     * 任务类型  0：部门  1：人员
     */
    @NotNull(message = "任务类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sTaskType;

    /**
     * 任务分类：0： 年度 1：月度
     */
    @NotNull(message = "任务分类不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sCategoryType;

    /**
     * 起始年度 如“2023”
     */
    @NotNull(message = "起始年度不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sStartYear;

    /**
     * 终止年度 如“2026”
     */
    @NotNull(message = "终止年度不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sEndYear;

    /**
     * 配额（单位万元） 如 35
     */
    @NotNull(message = "配额不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private BigDecimal dQuota;

    /**
     * 备注
     */
    private String  remarks;

    /**
     * 媒体ID
     */
    private String  mediaId;

    /**
     * 实体对象id列表 sStartType=‘0’ ： 部门ID  sStartType=‘1’ ： 人员ID
     */
    List<Long>  idList;

}


