package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 工作报告
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
@Getter
@Setter
@LogData(alias = "工作报告")
public class Twworkreports implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "employname")
    private Long id;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 人员ID
     */
    private Long employid;

    /**
     * 人员名称
     */
    @LogData(alias = "人员名称")
    private String employname;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "结束日期")
    private Date denddate;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatedate;

    /**
     * 工作方式(电话、微信、上门、综合、其他)
     */
    @LogData(alias = "工作方式")
    private String sworkmode;

    /**
     * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
     */
    @LogData(alias = "报告类型")
    private Integer iworktype;

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
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;

    /**
     * 审批人id
     */
    private Long icheckerid;

    /**
     * 审批人名称
     */
    @LogData(alias = "审批人名称")
    private String scheckername;

    /**
     * 审批日期
     */
    @LogData(alias = "审批日期")
    private Date dcheckdate;

    /**
     * 审批意见
     */
    @LogData(alias = "审批意见")
    private String scheckopinions;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
