package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 审批流程关联表
 * </p>
 *
 * @author muyn
 * @since 2023-12-05
 */
@Getter
@Setter
@LogData(alias = "审批流程关联表")
public class Twapplicationrelations implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者
     */
    @LogData(alias = "创建者")
    private String createempname;

    /**
     * 流程类型
     */
    @LogData(alias = "流程类型")
    private String flowtype;
    /**
     * 审批类型
     */
    @LogData(alias = "审批类型")
    private String approvetype;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 业务主id(比如orderid)
     */
    @LogData(alias = "业务主id(比如orderid)")
    private Long mainid;

    /**
     * 业务从id(比如orderitemid)
     */
    @LogData(alias = "业务从id(比如orderitemid)")
    private Long childid;

    /**
     * 审批状态
     */
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;

    /**
     * 审批流程id
     */
    @LogData(alias = "审批流程id")
    private String applicationid;

    /**
     * 最后审批意见
     */
    @LogData(alias = "最后审批意见")
    private String sapprovalopinions;

    /**
     * 最后修改时间
     */
    @LogData(alias = "最后修改时间")
    private Date dlastmodifydate;

    /**
     * 新客户id
     */
    @LogData(alias = "新客户id")
    private Long newcustomerid;

    /**
     * 新经营主体id
     */
    @LogData(alias = "新经营主体id")
    private Long newbusinessentityid;

    /**
     * 是否调整归属
     */
    @LogData(alias = "是否调整归属")
    private Boolean bchangebelong;

    /**
     * 是否改加刊
     */
    @LogData(alias = "是否改加刊")
    private String bchangeitem;

    /**
     * 是否停刊
     */
    @LogData(alias = "是否停刊")
    private String bstopitem;

    /**
     * 调整要求
     */
    @LogData(alias = "调整要求")
    private String schangecontent;

    /**
     * 调整原因
     */
    @LogData(alias = "调整原因")
    private String schangereason;
}
