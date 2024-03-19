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
 * 我的菜单
 * </p>
 *
 * @author muyn
 * @since 2024-01-22
 */
@Getter
@Setter
@LogData(alias = "我的菜单")
public class Twmymenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
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
    private Date dcreatetime;

    /**
     * 人员类型(未定，默认1 -业务员)
     */
    @LogData(alias = "人员类型(未定，默认1 -业务员)")
    private Integer employtype;

    /**
     * 上级菜单 id
     */
    @LogData(alias = "上级菜单 id")
    private Long parentmenuid;

    /**
     * 菜单表id
     */
    @LogData(alias = "菜单表id")
    private Long menuid;

    /**
     * 是否内置（固定死的）
     */
    @LogData(alias = "是否内置（固定死的）")
    private Boolean bbuiltin;

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
