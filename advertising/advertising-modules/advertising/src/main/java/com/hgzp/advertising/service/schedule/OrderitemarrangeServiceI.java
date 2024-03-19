package com.hgzp.advertising.service.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.OrderItemMarrangePosReq;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitemarrange;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 广告安排表 服务类
 * </p>
 *
 * @author muyn
 * @since 2023-12-06
 */
public interface OrderitemarrangeServiceI extends IMyService<Tworderitemarrange> {
    /**
     * 更新订单安排信息
     * 方法功能:  更新订单安排信息
     *
     * @param tworderitemarrange
     * @return void
     * @author songly
     * @date 2023/12/23 16:39
     */
    void updateOrderitemarrange(Tworderitemarrange tworderitemarrange) throws Exception;

    /**
     * 根据订单明细Id获取安排信息
     * 方法功能:
     *
     * @param orderId
     * @param orderItemid
     * @return com.hgzp.core.model.Tworderitemarrange
     * @author songly
     * @date 2023/12/23 16:43
     */
    List<Tworderitemarrange> getOrderitemarrangeByItemId(Long orderId, Long orderItemid) throws Exception;

    /**
     * 批量安排订单明细
     * 方法功能:
     *
     * @param orderItemBatchArrangeDto
     * @return void
     * @author songly
     * @date 2023/12/18 14:49
     */
    void bacthOrderitemarrange(OrderItemBatchArrangeDTO orderItemBatchArrangeDto) throws Exception;

    /**
     * 根据ID查询广告明细安排
     *
     * @param id
     * @return {@link Tworderitemarrange}
     * @author muyn
     * @since 2023-12-06
     */
    OrderitemarrangeVO getOrderitemarrangeById(String id);

    /**
     * 根据某些值查询订单明细安排列表
     *
     * @param query
     * @return {@link List<Tworderitemarrange>}
     * @author muyn
     * @since 2023-12-06
     */
    List<OrderitemarrangeVO> getOrderitemarrangelist(OrderitemarrangeReq query);

    /**
     * 订单明细安排分页数据
     *
     * @param page  分页请求参数
     * @param query
     * @return {@link IPage<Tworderitemarrange>}
     * @author muyn
     * @since 2023-12-06
     */
    IPage<OrderitemarrangeVO> getOrderitemarrangePageList(Page<Tworderitem> page, OrderitemarrangeReq query);

    /**
     * 获取版面已安排订单信息
     * 方法功能:
     *
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.schedule.vo.PageOrderItemArrangeVo>
     * @author songly
     * @date 2024/1/23 15:06
     */
    List<PageOrderItemArrangeVo> getPageOrderItemArrangeVoList(OrderitemarrangeReq query);

    /**
     * 取消安排（更改状态及恢复预安排版面）
     * 方法功能:
     *
     * @param id
     * @return void
     * @author hgsongly
     * @date 2024/1/29 13:03
     */
    void updateOrderitemarrangeStatus(String id) throws Exception;

    /**
     * 修改订单明细安排坐标
     * 方法功能:
     *
     * @param marrangePosReq
     * @return void
     * @author songly
     * @date 2024/1/30 9:43
     */
    void updateOrderitemarrangePos(OrderItemMarrangePosReq marrangePosReq) throws Exception;

    /**
     * 修改订单明细发布状态
     * 方法功能:
     *
     * @param ids
     * @param bPublish
     * @return void
     * @author songly
     * @date 2024/1/30 16:01
     */
    void updateOrderitemPublishStatus(String ids, Boolean bPublish) throws Exception;

    /**
     * 方法功能:
     *
     * @param id
     * @param pubDate
     * @param pubStatus
     * @param issueDate
     * @param issueFolder
     * @param foldVersion
     * @param issuePage
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/2/27 10:07
     */
    Json updateItemArrangeByCJ(String id, String pubDate, String pubStatus, String issueDate, String issueFolder, String foldVersion, String issuePage);

    /**
     * 方法功能: 删除广告排版信息
     *
     * @param inforid
     * @param pubDate
     * @return com.hgzp.core.page.Json
     */
    Json DelAdTypeSetForCJ(String inforid, String pubDate);

}
