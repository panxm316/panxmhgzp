package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.constant.ValidateParam;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 广告预约 || 快速预约
 */
@Data
public class PreOrderDTO {
    /**
     * 订单基础信息
     */
    @NotNull(message = "预约订单不能为空", groups = {ValidateParam.savePreOrder.class})
    private TworderDTO order;
    /**
     * 订单明细
     */
    @Valid
    @NotEmpty(message = "预约订单明细不能为空", groups = {ValidateParam.savePreOrder.class})
    private List<TworderitemDTO> orderItems;
    /**
     * 新建或者编辑快速预约使用的 广告资源申请表id
     */
    private Long adResourceApplicationId;
    /**
     * 新建或者编辑快速预约使用的 已经审批通过的广告资源申请表id
     */
    private Long approvedResourceApplicationId;
}
