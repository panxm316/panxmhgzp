package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twpreinvoiceapplicationdetail;

import java.util.List;

/**
 * <p>
 * 预开发票申请关联合同表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
public interface TwpreinvoiceapplicationdetailServiceI extends IService<Twpreinvoiceapplicationdetail> {

    /**
     * 据订单id获取预开发票申请id的list
     * 方法功能:据订单id获取预开发票申请id的list(由订单id -> 订单对应的所有预开申请id)
     * 注：此处未过滤是否审批通过，默认使用时注意 “启用 & 审批通过” 的限制
     *
     * @author yanz
     * @date 2023/12/5 14:00
     */
    List<Long> getPreInvoiceApplicationIdsByOrderId(Long orderId);

    /**
     * 方法功能:  根据预开发票申请id获取订单id数组(由预开申请id -> 预开申请对应的所有订单id)
     *
     * @param preinvoiceapplicationid
     * @return java.util.List<java.lang.Long>
     * @author suny
     * @date 2023/12/5 14:25
     */
    List<Long> getOrderIdsByApplicationId(Long preinvoiceapplicationid);

    /**
     * 取订单id对应发票申请下所有的订单id
     * 方法功能:取订单id对应发票下所有的订单id(由订单id -> 订单对应的所有预开申请id -> 这些申请下的所有订单id)
     *
     * @param orderId
     * @return java.util.List<java.lang.Long>
     * @author yanz
     * @date 2023/12/8 15:20
     */
    List<Long> getOrderIdsByOrderId(Long orderId);

    /**
     * 按预开发票申请id，删除关联合同记录
     * 方法功能:按预开发票申请id，删除关联合同记录
     *
     * @param applyIds Long型List
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/12/12 15:00
     */
    Boolean deleteByApplyIds(List<Long> applyIds);
}
