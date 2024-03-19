package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 报表模板明细设置
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@Getter
@Setter
@LogData(alias = "报表模板明细设置")
public class Tbreportmodelitem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 报表模板组id
     */
    @LogData(alias = "报表模板组id")
    private Long reportmodelgroupid;

    /**
     * 类型(dept,media)
     */
    @LogData(alias = "类型(dept,media)")
    private String reporttype;

    /**
     * 表头名称
     */
    @LogData(alias = "表头名称")
    private String sheadername;

    /**
     * 部门类型(0-总部 1-区域)
     */
    @LogData(alias = "部门类型(0-总部 1-区域)")
    private Integer idepttype;

    /**
     * 字段名
     */
    @LogData(alias = "字段名")
    private String scolumnname;

    /**
     * 级别
     */
    @LogData(alias = "级别")
    private Integer ilevel;

    /**
     * 数据范围名称
     */
    @LogData(alias = "数据范围名称")
    private String snames;

    /**
     * 数据范围id串
     */
    @LogData(alias = "数据范围id串")
    private String sids;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;
}
