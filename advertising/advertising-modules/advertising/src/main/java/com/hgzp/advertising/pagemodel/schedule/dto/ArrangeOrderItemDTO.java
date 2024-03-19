package com.hgzp.advertising.pagemodel.schedule.dto;

import lombok.Data;

/**
 * ArrangeOrderItemDTO
 * 创建人：lhl
 * 类描述：安排广告订单
 * 创建日期：2024/3/7 13:58
 */
@Data
public class ArrangeOrderItemDTO {
    /**
     * 订单明细id
     */
    private Long id;
    /**
     * 媒体类型  0：报刊  1：南方+，南方网  2：微信，微博
     */
    private String mediaType;
    /**
     *  安排数据
     *  mediaType
     *           0：版面名称
     *           1：栏目ID（叠次版本）
     */
    private String arrangeData;
    /**
     * 备注
     */
    private String remark;
    /**
     * 媒体广告URL地址
     */
    private String adURL;

}


