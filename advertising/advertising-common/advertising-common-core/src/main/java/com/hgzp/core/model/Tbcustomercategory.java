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
 * 客户分类表
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
@Getter
@Setter
@LogData(alias = "客户分类")
public class Tbcustomercategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类名称
     */
    @LogData(alias = "分类名称")
    private String categoryname;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 创建id
     */
    @LogData(alias = "创建id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
    private Date createtime;
    /**
     * 是否需要审核
     */
    @LogData(alias = "是否需要审核")
    private Boolean bapprove;
}
