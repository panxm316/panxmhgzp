package com.hgzp.pagemodel.business;

import lombok.Data;

import java.math.BigDecimal;

/**
 * TaskCountVo
 * 创建人：lhl
 * 类描述：任务额度汇总结果对象
 * 创建日期：2023/12/26 13:20
 */
@Data
public class TaskCountVo {
    /**
     * id
     */
    private String id;

    /**
     * 订单ID
     */
    private Long orderid;

    /**
     * 部门deptid
     */
    private Long deptid;

    /**
     * 主业务员ID
     */
    private Long employid;

    /**
     * 上级部门/部门
     */
    private String superiorname;

    /**
     * 部门/人员
     */
    private String departmentname;
    /**
     * 人员名称
     */
    private String employename;

    /**
     * 媒体
     */
    private String medianame;

    /**
     * 汇总时间 例如(年度：2023 月度：2023-12)
     */
    private String countdate;
    /**
     * 签订金额
     */
    private BigDecimal amountreceivable;
    /**
     * 已收金额
     */
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 任务额度（万元）
     */
    private BigDecimal ntaskamount;
    /**
     * 任务描述
     */
    private String sremark;
    /**
     * 完成情况（比例）
     * （业绩金额*归属比例）/ 任务额度
     */
    private String ratio;
    /**
     * 任务额度记录ID
     *
     */
    private Long taskid;
    /**
     * 媒体ID
     *
     */
    private Long mediaid;

}


