package com.hgzp.advertising.pagemodel.schedule.vo;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.util.List;

/**
 PageOrderItemArrangeVo
 创建人：songly
 类描述：版面已安排订单信息
 创建日期：2024/1/23 14:54
 */
@Data
public class PageOrderItemArrangeVo {
    /**
     * 版面ID
     */
    private Long id;

    /**
     * 宽(mm)
     */
    private Integer ipagewidth;

    /**
     * 高(mm)
     */
    private Integer ipageheight;
    /**
     * 色彩名称
     */
    private String adcolorname;

    /**
     * 已安排订单
     */
    List<PageOrderItemMarrangeDetail> orderitemarrangeList;
}