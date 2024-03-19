package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitemcost;

import java.util.List;

/**
 * <p>
 * 广告成本表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
public interface TworderitemcostServiceI extends IService<Tworderitemcost> {
    /**
     * 方法功能:  查询订单和订单明细进行成本核对
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author suny
     * @date 2023/12/14 8:40
     */
    IPage<OrderAndItemDTO> getOrderAndItemPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception;


    /**
     * 广告成本POJO转DTO
     * 方法功能:有查询操作，涉及Tworderitemcosts广告成本、Tworderitemcostfiles证明文件表
     *
     * @param Tworderitemcosts
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO>
     * @author yanz
     * @date 2024/1/5 15:01
     */
    List<OrderItemCostDTO> convertToOrderItemCostDTOs(List<Tworderitemcost> Tworderitemcosts);

    /**
     * 方法功能: 新增保存广告成本
     * 提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/14 9:19
     */
    void saveOrderItemCost(OrderItemCostDTO entity) throws Exception;

    /**
     * 方法功能: 修改保存广告成本
     * 提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/14 9:19
     */
    void updateOrderItemCost(OrderItemCostDTO entity) throws Exception;

    /**
     * 方法功能: 删除广告成本
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/12/14 9:47
     */
    void deleteOrderItemCost(String ids) throws Exception;

    /**
     * 方法功能: 更新广告成本状态，提交时成本金额加到订单明细成本金额，删除或者退回时成本金额减去订单明细成本金额
     *
     * @param id
     * @param status
     * @return void
     * @author suny
     * @date 2023/12/14 9:50
     */
    void updateOrderItemCostStatus(String id, Integer status) throws Exception;
}
