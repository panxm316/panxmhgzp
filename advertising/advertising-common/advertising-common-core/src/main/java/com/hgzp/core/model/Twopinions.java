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
 * 常用审批意见
 * </p>
 *
 * @author muyn
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("twopinions")
public class Twopinions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "createempname")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date createdate;

    /**
     * 意见内容
     */
    @LogData(alias = "意见内容")
    private String sopinion;

    /**
     * 审批流程类型
     */
    @LogData(alias = "审批流程类型")
    private String sflowtype;

    /**
     * 是否通用
     */
    @LogData(alias = "是否通用")
    private Boolean bcommon;

    /**
     * 是否用于同意 0-否决 1 -同意 
     */
    @LogData(alias = "是否用于同意 0-否决 1 -同意 ")
    private Integer ipasstype;

    /**
     * 排序
     */
    @LogData(alias = "排序")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "是否启用")
    private Boolean buse;
}
