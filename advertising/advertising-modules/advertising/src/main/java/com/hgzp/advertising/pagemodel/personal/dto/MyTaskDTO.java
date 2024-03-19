package com.hgzp.advertising.pagemodel.personal.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * MyTaskDTO
 * 创建人：suny
 * 类描述：我的任务统计DTO
 * 创建日期：2024/1/23 14:26
 */
@Data
public class MyTaskDTO extends BaseDTO {
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 签订金额
     */
    private BigDecimal amountreceivable;
    /**
     * 任务额度（万元）
     */
    private BigDecimal ntaskamount;
    /**
     * 完成比例(业绩金额/任务额度)
     */
    private BigDecimal nfinishrate;
    /**
     * 任务日期 年：2023 月2023-01
     */
    private String staskdate;

    /**
     * 任务分类：年度、月度
     */
    private String stasktype;

    /**
     * 人员分类：部门、人员
     */
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
}

