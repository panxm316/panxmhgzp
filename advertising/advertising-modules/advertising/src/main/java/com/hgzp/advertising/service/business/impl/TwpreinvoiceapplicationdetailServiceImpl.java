package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationdetailServiceI;
import com.hgzp.core.model.Twpreinvoiceapplicationdetail;
import com.hgzp.mapper.business.TwpreinvoiceapplicationdetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 预开发票申请关联合同表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwpreinvoiceapplicationdetailServiceImpl extends ServiceImpl<TwpreinvoiceapplicationdetailMapper, Twpreinvoiceapplicationdetail> implements TwpreinvoiceapplicationdetailServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据订单id获取预开发票申请id的list
     * 方法功能:据订单id获取预开发票申请id的list(由订单id -> 订单对应的所有预开申请id)
     * 注：此处未过滤是否审批通过，默认使用时注意 “启用 & 审批通过” 的限制
     *
     * @param orderId
     * @return java.util.List<java.lang.Long>
     * @author yanz
     * @date 2023/12/5 14:00
     */
    @Override
    public List<Long> getPreInvoiceApplicationIdsByOrderId(Long orderId) {
        List<Long> ids = this.lambdaQuery()
                .eq(Twpreinvoiceapplicationdetail::getOrderid, orderId)
                .list()
                .stream()
                .filter(Objects::nonNull)
                .map(Twpreinvoiceapplicationdetail::getPreinvoiceapplicationid)
                .collect(Collectors.toList());
        return ids;
    }


    /**
     * 方法功能:  根据预开发票申请id获取订单id数组(由预开申请id -> 预开申请对应的所有订单id)
     *
     * @param preinvoiceapplicationid
     * @return java.util.List<java.lang.Long>
     * @author suny
     * @date 2023/12/5 14:25
     */
    @Override
    public List<Long> getOrderIdsByApplicationId(Long preinvoiceapplicationid) {
        return this.lambdaQuery().eq(Twpreinvoiceapplicationdetail::getPreinvoiceapplicationid, preinvoiceapplicationid).list().stream()
                .map(Twpreinvoiceapplicationdetail::getOrderid).collect(Collectors.toList());
    }

    /**
     * 取订单id对应发票申请下所有的订单id
     * 方法功能:取订单id对应发票下所有的订单id(由订单id -> 订单对应的所有预开申请id -> 这些申请下的所有订单id)
     *
     * @param orderId
     * @return java.util.List<java.lang.Long>
     * @author yanz
     * @date 2023/12/8 15:20
     */
    @Override
    public List<Long> getOrderIdsByOrderId(Long orderId) {
        List<Long> applyIds = getPreInvoiceApplicationIdsByOrderId(orderId);
        List<Long> orderIds = applyIds.stream()
                .filter(Objects::nonNull)
                .map(this::getOrderIdsByApplicationId)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        return ObjUtil.isEmpty(orderIds) ? Collections.singletonList(orderId) : orderIds;
    }

    /**
     * 按预开发票申请id，删除关联合同记录
     * 方法功能:按预开发票申请id，删除关联合同记录
     *
     * @param applyIds Long型List
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/12/12 15:00
     */
    @Override
    public Boolean deleteByApplyIds(List<Long> applyIds) {
        Long count = this.lambdaQuery().in(Twpreinvoiceapplicationdetail::getPreinvoiceapplicationid, applyIds).count();
        if (count == 0) {
            return true;
        }
        innerInterceptor.recoredLog();
        return this.lambdaUpdate().in(Twpreinvoiceapplicationdetail::getPreinvoiceapplicationid, applyIds).remove();
    }
}
