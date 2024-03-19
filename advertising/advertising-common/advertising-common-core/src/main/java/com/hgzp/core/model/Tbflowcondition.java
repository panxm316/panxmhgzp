package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 工作流条件表
 * </p>
 *
 * @author wwk
 * @since 2023-10-12
 */
@Getter
@Setter
@LogData(alias = "工作流条件")
public class Tbflowcondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 条件名称
     */
    @LogData(alias = "条件名称")
    private String sname;

    /**
     * 条件key(流程申请DTO对象列名)
     */
    @LogData(alias = "条件key")
    private String sconditionkey;

    /**
     * 条件类型(Money、SelectMultiDept、SelectMultiUser)
     */
    @LogData(alias = "条件类型")
    private String sconditiontype;

    /**
     * 条件数据表名
     */
    @LogData(alias = "条件数据表名")
    private String sconditiontable;
    /**
     * 流程类型
     */
    @LogData(alias = "流程类型")
    private String sflowtype;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
