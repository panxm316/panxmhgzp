package com.hgzp.advertising.pagemodel.schedule.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;
import lombok.Data;

import java.util.List;

/**
 * PageOrdVO
 * 创建人：lhl
 * 类描述：页面广告安排
 * 创建日期：2024/1/11 10:28
 */
@Data
public class PageOrderVO {
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
    List<PageOrderItemVo>  pageOrderItemVoList;
}


