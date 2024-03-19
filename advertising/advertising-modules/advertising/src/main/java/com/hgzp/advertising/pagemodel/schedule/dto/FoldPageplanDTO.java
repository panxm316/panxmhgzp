package com.hgzp.advertising.pagemodel.schedule.dto;

import com.hgzp.core.model.Tbfoldpageplan;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 FoldPageplanDTO
 创建人：songly
 类描述：TODO
 创建日期：2023/11/20 16:45
 */
@Data
public class FoldPageplanDTO extends Tbfoldpageplan {
    /**
     * 添加类型：PageNum: 按版数批量增加（版数为1是单个版添加）
     *    PageColor:按广告色彩批量增加、
     *    Duplicate: 按时间段复制（是按天复制或按周复制）
     */
    private String addType;
    /**
     * 版面色彩id
     */
    private Long pagecolorid;
    /**
     * 开始时间
     */
    @NotNull(message = "开始日期不能为空")
    private Date startTime;
    /**
     * 查询结束时间
     */
    @NotNull(message = "结束日期不能为空")
    private Date endTime;
    /**
     * 复制开始时间
     */
    @NotNull(message = "复制开始日期不能为空")
    private Date duplicateStartTime;
    /**
     * 复制结束时间
     */
    @NotNull(message = "复制结束日期不能为空")
    private Date duplicateEndTime;
    /**
     * 按周复制标志
     */
    private  Boolean bworkDup;
}