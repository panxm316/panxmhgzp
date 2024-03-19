package com.hgzp.advertising.pagemodel.schedule.vo;

import com.hgzp.core.model.Tworderitemarrange;
import lombok.Data;

/**
 PageOrderIteMarrangeDetail
 创建人：songly
 类描述：版面已安排订单信息
 创建日期：2024/1/30 15:01
 */
@Data
public class PageOrderItemMarrangeDetail extends Tworderitemarrange {
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 主业务员名称
     */
    private String employname;
    /**
     * 代理公司业务员名称
     */
    private String agencyemployname;
    /**
     * 内容生产方
     */
    private String agentname;
    /**
     * 订单审批状态
     */
    private Integer iapprovestatus;
    /**
     * 订单审批状态
     */
    private String iapprovestatusName;

    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 发布状态
     */
    private String ipublishstatusName;
    /**
     * 备注
     */
    private String presremark;

    /**
     * 预定版面名称
     */
    private String prefoldpageplanname;
    /**
     * 关联订单id(用于预约补刊)
     */
    private Long relateorderid;
}