package com.hgzp.advertising.pagemodel.ad.vo;

import lombok.Data;

/**
 OrderItemPosReq
 创建人：songly
 类描述：订单明细安排位置请求参数
 创建日期：2024/1/30 9:39
 */
@Data
public class OrderItemMarrangePosReq {
    /**
     * 安排Id
     */
    private Long id;
    /**
     * x坐标
     */
    private Integer nleftx;

    /**
     * y坐标
     */
    private Integer ntopy;
}