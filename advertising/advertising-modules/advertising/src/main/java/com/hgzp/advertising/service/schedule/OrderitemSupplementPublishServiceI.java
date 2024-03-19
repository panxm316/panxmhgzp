package com.hgzp.advertising.service.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemSupplementPublishReq;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitemarrange;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author songly
 * @date 2023/12/23 14:38
 * 补刊业务 停刊 改刊业务
 */
public interface OrderitemSupplementPublishServiceI extends IMyService<Tworderitemarrange> {

    /**
     * 订单明细补刊分页数据
     *
     * @param page  分页请求参数
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>
     * @author muyn
     * @since 2023-12-23
     */
    IPage<OrderitemarrangeVO> getOrderitemsupplementpublishPageList(Page<Tworderitem> page,
                                                                    OrderitemSupplementPublishReq query);

    /**
     * 根据ID查询广告明细补刊
     *
     * @param id
     * @return java.util.List<com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>
     * @author muyn
     * @since 2023-12-23
     */
    OrderitemarrangeVO getOrderitemsupplementpublishById(String id);

    /**
     * 根据某些值查询订单明细补刊列表
     * 方法功能:根据某些值查询订单明细补刊列表
     *
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>
     * @author songly
     * @date 2023/12/23 14:50
     */
    List<OrderitemarrangeVO> getOrderitemsupplementpublishlist(OrderitemSupplementPublishReq query);

    /**
     * 批量确认订单明细补刊
     * 方法功能:
     *
     * @param orderItemBatchArrangeDto
     * @return void
     * @author songly
     * @date 2023/12/23 14:49
     */
    void bacthOrderitemsupplementpublish(OrderItemBatchArrangeDTO orderItemBatchArrangeDto) throws Exception;

    /**
     * 撤回补刊确认
     * 方法功能:
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2024/2/20 13:44
     */
    void orderitemsupplementRecall(String ids) throws Exception;

    /**
     * 加刊
     * 方法功能:加刊（新增加一条广告明细，状态为待审， 加刊审批通过后审批状态 为通过）
     *
     * @param id
     * @return void
     * @author songly
     * @date 2024/3/12 10:07
     */
    void addOrderitemByItemId(String id) throws Exception;

    /**
     * 改刊
     * 方法功能:改刊（只能选择一条广告明细记录进行改刊，改刊后原记录设置为无效，新增加一条新的广告明细，提交审批后，两条记录的改刊状态皆为审批流程相应的状态（待审，在审，通过。。。。））
     *
     * @param id
     * @return void
     * @author songly
     * @date 2024/3/12 10:08
     */
    void ChangeOrderitemByItemId(String id) throws Exception;

    /**
     * 停刊
     * 方法功能: 停刊（可以选择多条记录进行停刊，状态为待审 需审批，停刊审批通过后状态 改为无效且停刊审批状态为通过）
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2024/3/12 10:08
     */
    void StopOrderitemByItemId(String ids) throws Exception;

}
