package com.hgzp.advertising.pagemodel.schedule.vo;

import lombok.Data;

import java.util.Date;

/**
 * AdvOrderItemVo
 * 创建人：LHL
 * 类描述：广告订单
 * 创建日期：2023/12/5
 */
@Data
public class AdvOrderItemVo {
    /**
     * 订单id
     */
    private Long orderItemId;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 刊出日期
     */
    private Date publishDate;

    /**
     * 刊出日
     */
    private int day;

    /**
     * 刊出状态
     */
    private int publistStatus;

}


