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
 * 价格明细表
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Getter
@Setter
@LogData(alias = "价格明细表")
public class Tbpriceitem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 价格表id
     */
    @LogData(alias = "价格表id")
    private Long pricegroupid;

    /**
     * 价格表名称
     */
    @LogData(alias = "价格表名称")
    private String pricegroupname;

    /**
     * 媒体类型id
     */
    @LogData(alias = "媒体类型key")
    private String mediatypekey;

    /**
     * 媒体类型
     */
    @LogData(alias = "媒体类型名称")
    private String mediatypename;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体id")
    private Long mediaid;

    /**
     * 媒体名称
     */
    @LogData(alias = "媒体名称")
    private String medianame;

    /**
     * 叠次id
     */
    @LogData(alias = "叠次id")
    private Long foldid;

    /**
     * 叠次名称
     */
    @LogData(alias = "叠次名称")
    private String foldname;

    /**
     * 叠次版本ID
     */
    @LogData(alias = "叠次版本ID")
    private Long foldareaid;

    /**
     * 叠次版本名称
     */
    @LogData(alias = "叠次版本名称")
    private String foldareaname;

    /**
     * 广告形式id
     */
    @LogData(alias = "广告形式id")
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    @LogData(alias = "广告形式名称")
    private String addisplayformname;

    /**
     * 版面类别id
     */
    @LogData(alias = "版面类别id")
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    @LogData(alias = "版面类别名称")
    private String pagesortname;

    /**
     * 色彩id
     */
    @LogData(alias = "色彩id")
    private Long adcolorid;

    /**
     * 色彩名称
     */
    @LogData(alias = "色彩名称")
    private String adcolorname;

    /**
     * 规格id
     */
    @LogData(alias = "规格id")
    private Long adspecid;

    /**
     * 规格名称
     */
    @LogData(alias = "规格名称")
    private String adspecname;

    /**
     * 星期id
     */
    @LogData(alias = "星期id")
    private Long weeksettingid;

    /**
     * 星期名称
     */
    @LogData(alias = "星期名称")
    private String weeksettingname;

    /**
     * 价格
     */
    @LogData(alias = "价格")
    private BigDecimal baseprice;

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
    @LogData(alias = "备注")
    private String sremark;
}
