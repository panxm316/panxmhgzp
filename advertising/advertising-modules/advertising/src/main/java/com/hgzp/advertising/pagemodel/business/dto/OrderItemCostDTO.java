package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OrderItemCostDTO
 * 创建人：suny
 * 类描述：TODO 广告成本DTO
 * 创建日期：2023/12/13 15:07
 */
@Data
public class OrderItemCostDTO extends BaseDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单明细表
     */
    private Long orderitemid;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 创建人名称
     */
    private String createempname;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 成本金额
     */
    private BigDecimal namountcost;

    /**
     * 说明
     */
    private String sdescription;

    /**
     * 状态 0-待提交 1-已提交 2-已确认 3-已退回
     */
    private Integer istatus;
    /**
     * 证明文件列表
     */
    private List<OrderItemCostFilesDTO> fileList;
}

