package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告规格
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "广告规格")
public class Tbadspec implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体名称", mappedBy = "tbmedia", mappedByColumn = "sname")
    private Long mediaid;

    /**
     * 类型
     */
    @LogData(alias = "规格类型")
    private String stype;

    /**
     * 规格名称
     */
    @LogData(alias = "规格名称")
    private String sname;

    /**
     * 宽
     */
    @LogData(alias = "宽")
    private BigDecimal nwidth;

    /**
     * 高
     */
    @LogData(alias = "高")
    private BigDecimal nheight;

    /**
     * 面积
     */
    @LogData(alias = "面积")
    private BigDecimal narea;

    /**
     * 格子数
     */
    @LogData(alias = "格子数")
    private Integer ipknum;

    /**
     * 是否分类广告
     */
    private Boolean bclassified;

    /**
     * 是否大分类广告
     */
    private Boolean bbigclassified;

    /**
     * 显示设置
     */
    private String sspecdisplay;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "结束日期")
    private Date denddate;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
