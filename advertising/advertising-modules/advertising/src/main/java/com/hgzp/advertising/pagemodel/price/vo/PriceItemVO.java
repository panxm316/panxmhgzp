package com.hgzp.advertising.pagemodel.price.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PriceItemVO
 * 创建人：CGD
 * 类描述：价格详情vo
 * 创建日期：2023/11/10 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceItemVO extends BaseQueryInfo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 年份
     */
    private String syear;

    /**
     * 价格表id
     */
    private Long pricegroupid;

    /**
     * 价格表名称
     */
    private String pricegroupname;

    /**
     * 媒体类型id
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
    private String mediatypename;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 叠次id
     */
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 叠次版本ID
     */
    private Long foldareaid;

    /**
     * 叠次版本名称
     */
    private String foldareaname;

    /**
     * 广告形式id
     */
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    private String addisplayformname;

    /**
     * 版面类别id
     */
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    private String pagesortname;

    /**
     * 色彩id
     */
    private Long adcolorid;

    /**
     * 色彩名称
     */
    private String adcolorname;

    /**
     * 规格id
     */
    private Long adspecid;

    /**
     * 规格名称
     */
    private String adspecname;

    /**
     * 星期id
     */
    private Long weeksettingid;

    /**
     * 星期名称
     */
    private String weeksettingname;

    /**
     * 价格
     */
    private BigDecimal baseprice;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
