package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * AdProjectCountOrderDetailsVO
 * 创建人：lhl
 * 类描述：广告项目汇总订单明细
 * 创建日期：2024/1/8 8:48
 */
@Data
public class AdProjectCountOrderDetailsVO {
    /**
     * 明细数量
     */
    private long  orderCount;
    /**
     * 订单总额
     */
    private BigDecimal total;
    /**
     * 已完成
     */
    private BigDecimal completed;
    /**
     * 待完成
     */
    private BigDecimal noCompleted;
    /**
     * 订单列表
     */
    List<OrderPublishQueryResultVo> orderDetailsList;



}


