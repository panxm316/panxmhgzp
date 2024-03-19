package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告资源申请表
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
@Getter
@Setter
@LogData(alias = "广告资源申请表")
public class Twadresourceapplication implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sadtitle")
    private Long id;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 业务员id
     */
    private Long employid;

    /**
     * 业务员名称
     */
    @LogData(alias = "业务员名称")
    private String employname;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 0-直接客户、1-代理公司、2-内容生产方
     */
    @LogData(alias = "客户类型")
    private Integer icusttype;

    /**
     * 申请时间
     */
    private Date dapplytime;

    /**
     * 开始使用日期
     */
    @LogData(alias = "开始使用日期")
    private Date dstartdate;

    /**
     * 停止使用日期
     */
    private Date denddate;

    /**
     * 广告标题
     */
    @LogData(alias = "广告标题")
    private String sadtitle;

    /**
     * 广告内容
     */
    @LogData(alias = "广告内容")
    private String sadcontent;

    /**
     * 工作流申请表id
     */
    @LogData(alias = "工作流申请表id")
    private String applicationid;

    /**
     * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
     */
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;

    /**
     * 最后一次审批意见
     */
    @LogData(alias = "最后一次审批意见")
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
    @LogData(alias = "是否快速上版")
    private Boolean bquickly;

    /**
     * 一审意见
     */
    @LogData(alias = "一审意见")
    private String sfirstopinion;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
