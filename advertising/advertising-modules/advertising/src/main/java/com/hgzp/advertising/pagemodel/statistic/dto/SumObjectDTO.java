package com.hgzp.advertising.pagemodel.statistic.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * SumObjectDTO
 * 创建人：lhl
 * 类描述：汇总对象
 * 创建日期：2024/1/20 9:11
 */
@Data
public class SumObjectDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 汇总项
     */
    private String reportGroupItemId;
    /**
     * 汇总类型(dept,media,all)
     */
    private String reporttype;
    /**
     * 部门类型(0-总部 1-区域)
     */
    private Integer idepttype;
    /**
     * 数据范围id串
     */
    private String sids;
    /**
     * 数据范围名称，用于业务类型标识 0：媒体 1：叠次 2：版本
     */
    private String snames;

}


