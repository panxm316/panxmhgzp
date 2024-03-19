package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 范围表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbscope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String sname;

    /**
     * 范围表名(设置)
     */
    private String stablename;

    /**
     * 范围列名(设置)
     */
    private String scolumn;

    /**
     * 范围表名(查询sql)
     */
    private String squeryname;

    /**
     * 范围列名(查询sql)
     */
    private String squerycolumn;

    /**
     * 部门深度
     */
    private Integer idepth;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;
}
