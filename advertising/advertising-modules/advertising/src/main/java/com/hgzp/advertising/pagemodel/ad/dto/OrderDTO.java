package com.hgzp.advertising.pagemodel.ad.dto;
import com.hgzp.advertising.pagemodel.ad.vo.OrderItemVO;
import com.hgzp.advertising.pagemodel.customer.vo.ProcessInstanceVo;
import com.hgzp.core.model.Tworder;
import lombok.Data;

import java.util.List;

/**
 OrderDTO
 创建人：songly
 类描述：TODO
 创建日期：2023/11/30 9:56
 */
@Data
public class OrderDTO extends Tworder {
    /**资源Id*/
    private List<Long> adresourceapplicationid;
    /**状态标志 预约审批状态:preapproveStatus,
     * 加刊审批状态:addapproveStatus,
     * 改刊审批状态:changeapproveStatus,
     * 停刊审批状态:stopapproveStatus,
     * 折扣审批状态:discountapproveStatus
     */

    private Integer iapproveType;
    /**
     * 流程Id
     */
    private String flowId;
    /**订单明细*/
    private List<OrderItemVO> orderitem;
    /**
     * 客户Vip
     */
    private boolean bcustomeVip;

    /**
     * 代理公司Vip
     */
    private boolean bagencyVip;
    /**
     * 内容生产方Vip
     */
    private boolean bagentVip;
    /**
     * 折扣审批历史Id
     */
    private List<ProcessInstanceVo> discountProcessInstanceid;

    /**
     * 订单审批历史Id
     */
    private List<ProcessInstanceVo> orderProcessInstanceid;
}