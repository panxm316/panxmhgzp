package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 版面色彩结构
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Getter
@Setter
@LogData(alias = "版面色彩")
public class Tbpagecolor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @LogData(showColumn = "sname")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 色彩结构名称
     */
    @LogData(alias = "色彩结构名称")
    private String sname;

    /**
     * 色彩串
     */
    @LogData(alias = "色彩串")
    private String colorlist;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用标志")
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
