package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 报纸版心
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "报纸版心")
public class Tbpagesize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "报纸版心名称")
    private String sname;

    /**
     * 宽度
     */
    @LogData(alias = "宽度")
    private Integer ipagewidth;

    /**
     * 高度
     */
    @LogData(alias = "高度")
    private Integer ipageheight;

    /**
     * 单行显示个数
     */
    @LogData(alias = "单行显示个数")
    private Integer isinglescale;

    /**
     * 标准显示比例
     */
    @LogData(alias = "标准显示比例")
    private Integer iscale;

    /**
     * 版位安排版面规则
     */
    @LogData(alias = "版位安排版面规则")
    private String smoveflag;

    /**
     * 栏宽宽度
     */
    @LogData(alias = "栏宽宽度")
    private Integer icolwidth;

    /**
     * 字号
     */
    @LogData(alias = "字号")
    private BigDecimal nfontsize;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;

    /**
     * 是否默认
     */
    @LogData(alias = "默认")
    private Boolean bdefault;
}
