package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDTO;
import com.hgzp.advertising.pagemodel.ad.dto.PreOrderDTO;
import com.hgzp.advertising.pagemodel.ad.dto.PreOrderQueryDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderReq;
import com.hgzp.advertising.pagemodel.ad.vo.TworderVO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.Json;
import com.hgzp.pagemodel.api.CJAdPrinDTO;
import com.hgzp.service.common.IMyService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TworderServiceI extends IMyService<Tworder> {
    /**
     * 获取广告订单分页列表
     * 方法功能:
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.ad.dto.OrderDTO>
     * @author songly
     * @date 2023/12/1 13:03
     */
    IPage<OrderDTO> getOrderPageList(IPage<Tworder> page, OrderReq query) throws Exception;

    /**
     * 根据订单id查询订单信息
     * 方法功能:根据订单id查询订单信息
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.ad.dto.OrderDTO
     * @author songly
     * @date 2023/11/30 10:25
     */
    OrderDTO getOrderInfo(String id) throws Exception;

    /**
     * 保存订单信息
     * 方法功能: 保存订单信息
     *
     * @param orderDTO
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/30 10:31
     */
    Json saveOrder(OrderDTO orderDTO) throws Exception;

    /**
     * 更新订单信息
     * 方法功能: 更新订单信息
     *
     * @param orderDTO
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    Json updateOrder(OrderDTO orderDTO) throws Exception;

    /**
     * 删除订单信息
     * 方法功能: 删除订单信息
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    Json deleteOrder(String id) throws Exception;

    /**
     * 订单审批申请
     * 方法功能: 订单审批申请  订单审批只更新订单审批状态
     *
     * @param orderId
     * @param flowId
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author songly
     * @date 2023/11/30 16:17
     */
    Json<String> approveOrder(String orderId, String flowId);

    /**
     * 折扣审批申请
     * 方法功能: 折扣审批申请  订单先审折扣，通过后再审订单；且折扣申请更新折扣申请状态和订单状态
     *
     * @param orderId
     * @param flowId
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author songly
     * @date 2023/12/4 13:51
     */
    Json<String> discountApproveOrder(String orderId, String flowId);

    /**
     * 更新审批状态
     * 方法功能:
     *
     * @param applicationid
     * @param sOrderId
     * @param bUpdateopinion
     * @param approveDesc
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/30 16:17
     */
    Json updateOrderApprovalopinions(String applicationid, String sOrderId, boolean bUpdateopinion,
                                     String approveDesc, Integer iapproveType, Integer iapproveStatus);

    /**
     * 更新订单及明细状态
     * 方法功能:
     *
     * @param applicationid
     * @param sOrderId
     * @param bUpdateopinion
     * @param approveDesc
     * @param iapproveType
     * @param iapproveStatus
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/12 17:38
     */
    Json updateOrderApprovalopinions(String applicationid, String sOrderId, boolean bUpdateopinion,
                                     String approveDesc, Integer iapproveType, Integer iapproveStatus,
                                     String sOrderitemId);
    /**
     * 修改订单审批状态
     * 方法功能:
     * @author songly
     * @date 2023/11/30 17:15
     * @param applicationid
     * @param sOrderId
     * @param iapproveType
     * @param iApprovestatus
     * @return com.hgzp.core.page.Json
     */
    // Json updateOrderApprovalStatus(String applicationid, String sOrderId,Integer iapproveType,Integer
    // iApprovestatus) throws Exception;

    /**
     * 据合同号获取订单Map
     * 方法功能:据合同号获取订单Map，key是合同号，value是Tworder
     *
     * @param contractNums
     * @return java.util.Map<java.lang.String, com.hgzp.core.model.Tworder>
     * @author yanz
     * @date 2023/12/1 10:13
     */
    Map<String, Tworder> getOrdersByContractNums(List<String> contractNums);

    /**
     * 保存广告预约 || 快速预约
     *
     * @author wangxk
     * @since 2023-12-22
     */
    void savePreOrder(PreOrderDTO preOrderDTO);

    /**
     * 广告预约分页
     *
     * @param page  分页
     * @param query 查询参数
     * @return {@link Json}
     * @author wangxk
     * @since 2023-12-29
     */
    IPage<TworderVO> getPreOrderPageList(IPage<Tworder> page, PreOrderQueryDTO query);

    /**
     * 根据订单id获取订单详情,包含订单明细和订单归属
     *
     * @param orderId 订单id
     * @return {@link TworderVO}
     */
    TworderVO getPreOrder(Long orderId);

    /**
     * 查询一段时间内的认刊合同
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码或合同号（queryKey）、客户名称（可填写可选择customername）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/4 14:29
     */
    IPage<OrderAndItemDTO> getOrdersInPeriod(Page<Tworder> page, OrderAndItemVO query);

    /**
     * 查询订单合同详情
     * 方法功能:按订单id，查询订单合同详情（纯List）
     *
     * @param orderId
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO>
     * @author yanz
     * @date 2024/1/5 13:41
     */
    OrderContractDetailDTO getOrderContractDetail(Long orderId);

    /**
     * 广告预约订单提交审核,待审->在审
     *
     * @param orderId 订单id
     * @return {@link Json}
     * @auther wangxk
     * @date 2024/1/5 13:58
     */
    void submitPreOrder(String orderId);

    /**
     * 审核答复预约广告订单
     *
     * @param orderId 订单id
     * @param status  审核状态 2-通过 3-否决
     * @param comment 审核意见
     * @return {@link Json}
     * @auther wangxk
     * @date 2024/1/5 15:18
     */
    void approvePreOrder(String orderId, String status, String comment);

    /**
     * 更新订单客户信息
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/17 9:41
     */
    void updateOrderCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据合同号获取订单信息
     * 方法功能:
     *
     * @param contractNum
     * @return com.hgzp.advertising.pagemodel.ad.dto.OrderDTO
     * @author songly
     * @date 2024/1/18 14:07
     */
    OrderDTO getOrderInfoByContractNum(String contractNum) throws Exception;

    /**
     * 获取打印排版表
     * 方法功能: 获取打印排版表
     *
     * @param startTime
     * @param endTime
     * @param FoldId
     * @param FoldVer
     * @param FoldPage
     * @return java.util.List<com.hgzp.pagemodel.api.CJAdPrinDTO>
     * @author suny
     * @date 2024/2/29 15:31
     */
    List<CJAdPrinDTO> getCJAdPrinListForCJ(String startTime, String endTime, String FoldId, String FoldVer,
                                           String FoldPage);

    /**
     * 根据订单编号获取订单信息
     * 方法功能: 根据订单编号获取订单信息
     *
     * @param sordernum
     * @return java.util.List<com.hgzp.core.model.Tworder>
     * @author suny
     * @date 2024/3/6 10:05
     */
    List<Tworder> getOrdersBySordernumForCJ(String sordernum);

    /**
     * 订单明细停刊审批申请
     * 方法功能:
     *
     * @param orderItemIds
     * @param flowId
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author songly
     * @date 2024/3/12 18:08
     */
    Json<String> approveOrderItemAddChangeStop(String orderItemIds, String flowId, Integer sApproveType);
}
