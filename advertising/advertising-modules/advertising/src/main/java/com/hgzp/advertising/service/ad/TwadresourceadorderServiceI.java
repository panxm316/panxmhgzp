package com.hgzp.advertising.service.ad;

import com.hgzp.core.model.Twadresourceadorder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 广告资源广告订单关联表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
public interface TwadresourceadorderServiceI extends IMyService<Twadresourceadorder> {
    /**
     * 根据资源Id获取订单Id
     * 方法功能:
     *
     * @param ids
     * @return java.util.List<java.lang.Long>
     * @author songly
     * @date 2023/12/9 12:39
     */
    List<Long> getOrderIdByresourceIds(String ids);

    /**
     * 根据订单Id获取资源Id
     * 方法功能:
     *
     * @param sOrderIds
     * @return java.util.List<java.lang.Long>
     * @author songly
     * @date 2023/12/9 12:41
     */
    List<Long> getResourceaIdbyOrderId(String sOrderIds);
    /**
     * 重置广告订单资源关联表
     * 方法功能:
     * @author songly
     * @date 2024/1/5 15:15
     * @param adOrderId
     * @param resourceIds
     * @return void
     */
    void doReSetResourceAdOrder(Long adOrderId, List<Long> resourceIds) throws Exception;

}
