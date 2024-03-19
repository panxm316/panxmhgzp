package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告行业信息
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "广告行业")
public class Tbadindustry implements Serializable {

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
    @LogData(alias = "父部门", mappedBy = "Tbadindustry", mappedByColumn = "sname")
    private Long parentid;

    /**
     * 行业名称
     */
    @LogData(alias = "行业名称")
    private String sname;

    /**
     * 本级编号
     */
    private String slocalid;

    /**
     * 行业级别
     */
    @LogData(alias = "级别")
    private Integer idepth;

    /**
     * 行业解释
     */
    private String sdesc;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 文件路径
     */
    private String sfilepath;

    /**
     * 文件名称
     */
    private String sfilename;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否分类广告
     */
    @LogData(alias = "是否分类广告")
    private Boolean bclassified;

    /**
     * 文件名称
     */
    private String picturepath;
    /**
     * 节点禁用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
