package com.hgzp.pagemodel.schedule;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PageOrderItemVo
 * 创建人：lhl
 * 类描述：页面订单
 * 创建日期：2024/1/11 10:37
 */
@Data
public class PageOrderItemVo {
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 刊发日期
     */
    private Date dpublishdate;
    /**
     * 宽(cm)
     */
    private BigDecimal nwidth;

    /**
     * 高(cm)
     */
    private BigDecimal nheight;

    /**
     * x坐标
     */
    private Integer nleftx;

    /**
     * y坐标
     */
    private Integer ntopy;

    /**
     * 主业务员名称
     */
    private String employname;

    /**
     * 代理公司业务员名称
     */
    private String agencyemployname;

    /**
     * 内空生产方业务员名称
     */
    private String agentemployname;

    /**
     * 色彩名称
     */
    private String adcolorname;

    /**
     * 规格名称
     */
    private String adspecname;
    /**
     * 刊出日
     */
    private int day;

    /**
     * 发布状态
     */
    private Integer ipublishstatus;

}


