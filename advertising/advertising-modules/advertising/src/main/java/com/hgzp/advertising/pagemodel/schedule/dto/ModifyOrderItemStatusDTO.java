package com.hgzp.advertising.pagemodel.schedule.dto;

import lombok.Data;

/**
 * ModifyOrderItemStatusDTO
 * 创建人：lhl
 * 类描述：修改订单状态
 * 创建日期：2023/12/18 9:37
 */
@Data
public class ModifyOrderItemStatusDTO {
    /**
     * 订单明细id
     */
    private Long id;
    /**
     * 核查状态
     */
    private Integer ipublishstatus;

}


