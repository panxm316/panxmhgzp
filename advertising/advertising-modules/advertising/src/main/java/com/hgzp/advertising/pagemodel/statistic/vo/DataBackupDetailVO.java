package com.hgzp.advertising.pagemodel.statistic.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * DataBackupDetailVO
 * 创建人：suny
 * 类描述：数据轧账明细 前端查询对象
 * 创建日期：2024/1/19 16:22
 */
@Data
public class DataBackupDetailVO extends BaseQueryInfo {
    /**
     * 数据备份组表id
     */
    private Long databackupgroupid;
    /**
     * 订单号
     */
    @LogData(alias = "订单号")
    private String sordernum;
    /**
     * 合同号
     */
    private String scontractnum;

    /**
     * 订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer orderibooktype;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 行业id
     */
    private Long adindustyid;
    /**
     * 广告类型id
     */
    private Long adtypeid;
    /**
     * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    private Integer iorderapprovestatus;
    /**
     * 是否特殊订单(0 否 1-是)
     */
    private Boolean bspecial;
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 主业务员id
     */
    private Long employid;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 叠次id
     */
    private Long foldid;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 广告形式id
     */
    private Long addisplayformid;
    /**
     * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
     */
    private Integer iorderkind;
}

