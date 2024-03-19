package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告来源信息表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "广告来源")
public class Tbadfrom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 父id
     */
    @LogData(alias = "父部门", mappedBy = "Tbadfrom", mappedByColumn = "sname")
    private Long parentid;

    /**
     * 来源名称
     */
    @LogData(alias = "来源名称")
    private String sname;

    /**
     * 来源解释
     */
    private String sdesc;

    /**
     * 本级id号
     */
    private String slocalid;

    /**
     * 来源级别
     */
    @LogData(alias = "级别")
    private Integer idepth;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否有效
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
