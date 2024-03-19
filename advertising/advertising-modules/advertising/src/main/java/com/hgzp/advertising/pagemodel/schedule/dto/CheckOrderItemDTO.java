package com.hgzp.advertising.pagemodel.schedule.dto;

import lombok.Data;

/**
 * CheckOrderItemDTO
 * 创建人：lhl
 * 类描述：订单明细核查
 * 创建日期：2023/12/18 14:03
 */
@Data
public class CheckOrderItemDTO {
    /**
     * 订单明细id
     */
    private Long id;
    /**
     * 核查状态
     */
    private Integer ipublishcheckstatus;

    /**
     * 核查报告内容报告内容
     */
    private String spublishcheckcontent;


}


