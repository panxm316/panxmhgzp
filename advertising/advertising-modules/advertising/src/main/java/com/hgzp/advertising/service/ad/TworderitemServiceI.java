package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.api.CJOrderItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.DebtReasonVO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单刊期表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TworderitemServiceI extends IService<Tworderitem> {
    /**
     * 方法功能: 根据条件查询欠款统计分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO>
     * @author suny
     * @date 2023/11/22 10:00
     */
    IPage<OrderDebtDTO> getOrderDebtPageList(Page<Tworderitem> page, DebtReasonVO query) throws Exception;

    /**
     * 将欠款信息推送到欠款原因表中
     * 方法功能: 将欠款信息推送到欠款原因表中，同时更新订单刊期表中的欠款状态为已推送欠款统计表，并向业务员发送消息
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/22 10:01
     */
    void pushOrderDebt(String ids) throws Exception;

    /**
     * 方法功能: 根据订单id查询订单明细信息
     *
     * @param orderId
     * @return java.util.List<com.hgzp.core.model.Tworderitem>
     * @author songly
     * @date 2023/11/30 14:48
     */
    List<Tworderitem> getOrderItemByOrderId(String orderId) throws Exception;

    /**
     * 保存订单明细
     * 方法功能:
     *
     * @param orderItem
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/11 14:36
     */
    Json saveOrderItem(Tworderitem orderItem) throws Exception;

    /**
     * 更新订单明细
     * 方法功能:
     *
     * @param orderItem
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/11 14:37
     */
    Json updateOrderItem(Tworderitem orderItem) throws Exception;

    /**
     * 删除订单的所有明细
     * 方法功能: 删除订单明细信息
     *
     * @param orderids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/1 9:56
     */
    Json deleteOrderItem(String orderids) throws Exception;

    /**
     * 根据Id删除订单明细信息
     * 方法功能:  删除订单明细信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @authorgsongly
     * @date 2023/12/5 12:54
     */
    Json deleteOrderItemById(String ids) throws Exception;

    /**
     * 订单明细审批申请
     * 方法功能:
     *
     * @param orderItemId
     * @param flowId
     * @param iapproveType
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author songly
     * @date 2023/12/6 12:34
     */
    Json<String> approveOrderItem(String orderItemId, String flowId, Integer iapproveType);

    /**
     * 审批结束后更新订单明细审批意见
     * 方法功能:
     *
     * @param applicationid
     * @param sOrderItemId
     * @param bUpdateopinion
     * @param approveDesc
     * @param iapproveType
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/6 12:34
     */
    Json updateOrderItemApprovalopinions(String applicationid, String sOrderItemId, boolean bUpdateopinion,
                                         String approveDesc, Integer iapproveType, Integer iapproveStatus);

    /**
     * 根据明细状态调整订单状态
     * 方法功能:
     *
     * @param sOrderId
     * @return void
     * @author songly
     * @date 2023/12/6 14:24
     */
    void updateOrderStatus(String sOrderId);

    /**
     * 取得所有订单id对应的总欠款金额
     * 方法功能:对所有订单id，取得关联订单刊期（orderItem），计算总欠款金额
     *
     * @param orderIds
     * @return java.math.BigDecimal
     * @author yanz
     * @date 2023/12/8 15:55
     */
    BigDecimal getArrearagesSumByOrderIds(List<Long> orderIds);

    /**
     * 取得订单id对应的总欠款金额Map
     * 方法功能:取得订单id对应的总欠款金额Map，key是订单id，value是总欠款金额；订单id与合同号1对1
     *
     * @param orderIds
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @author yanz
     * @date 2023/11/30 16:22
     */
    Map<Long, BigDecimal> getArrearagesSumMapByOrderIds(List<Long> orderIds);

    /**
     * 订单（order）对象转订单VO（预开发票用）
     * 方法功能:订单（order）对象转订单VO（预开发票用）
     *
     * @param orders
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO>
     * @author yanz
     * @date 2023/12/9 9:09
     */
    List<OrderforPreinvoapplyVO> getOrderforPreinvoapplyVOS(List<Tworder> orders);


    /**
     * 方法功能: 根据订单id查询订单信息和欠款总和信息
     *
     * @param orderid
     * @return com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO
     * @author suny
     * @date 2023/12/6 13:55
     */
    OrderDebtDTO getOrderDebtInfo(String orderid) throws Exception;

    /**
     * 据客户id，查询所有有欠款的订单的广告明细
     * 方法功能:据客户id，查询所有有欠款的订单的广告明细。限制：有欠款、“审核状态”通过；可选限制：刊发时间(预见报开始日期)或合同号
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2023/12/25 14:23
     */
    IPage<OrderAndItemDTO> getOrderAndItemByCustomerid(Page<OrderAndItemDTO> page, CustomerChargeBankVO query);

    /**
     * 获取最大刊期编码
     * 方法功能:
     *
     * @param
     * @return java.lang.Long
     * @author hgsongly
     * @date 2023/12/25 17:21
     */
    Long getMaxItemCode();

    /**
     * 查询业绩列表（业绩金额）
     * 方法功能:列表查询tworderitem表。
     * 条件：时间范围、媒体、刊发状态（iapprovestatus=2通过且buse=1有效）
     * 返回：订单明细相关综合对象（OrderAndItemDTO）
     * 注：以orderitem 订单刊期/订单详情 数据为主
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/10 16:13
     */
    IPage<OrderAndItemDTO> getPerformanceRatio(Page<Tworderitem> page, OrderAndItemVO query);

    /**
     * 根据客户id和时间范围获取订单明细列表
     * 方法功能: 根据客户id和时间范围获取订单明细列表
     *
     * @param query
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author suny
     * @date 2024/1/23 9:09
     */
    IPage<OrderAndItemDTO> getOrderItemListByCustomerId(Page<OrderAndItemDTO> page, OrderAndItemVO query) throws Exception;

    /**
     * 我的订单明细列表
     * 方法功能: 按时间范围查看有我归属的广告详情，条件中可选择（仅欠款）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author suny
     * @date 2024/1/23 9:54
     */
    IPage<OrderAndItemDTO> getMyOrderItemPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception;

    /**
     * 根据超捷提供信息更新订单明细的宽高
     * 方法功能: 根据超捷提供信息更新订单明细的宽高
     *
     * @param id
     * @param width
     * @param height
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/2/26 14:33
     */
    Json updateItemByCJ(String id, String path, String filename, String width, String height);

    /**
     * 获取订单明细列表（只取分类广告）
     * 方法功能:  获取订单明细列表（只取分类广告）
     *
     * @param entiry
     * @return
     * @throws Exception
     */
    List<CJOrderItemDTO> getOrderItemForCJ(Tworderitem entiry);

    /**
     * 我的订单(明细)汇总
     * 方法功能:我的订单(明细)汇总
     *
     * @param query
     * @return com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO
     * @author yanz
     * @date 2024/3/6 9:26
     */
    OrderAndItemDTO getMyOrderItemCount(OrderAndItemVO query) throws Exception;


    /**
     * 根据订单id获取相关明细列表
     * 方法功能:
     *
     * @param orderId
     * @return java.util.List<com.hgzp.core.model.Tworderitem>
     * @author suny
     * @date 2024/3/5 16:52
     */
    List<Tworderitem> getListByOrderIdForCJ(String orderId);

    /**
     * 根据订单编号获取文件路径
     * 方法功能:根据订单编号获取文件路径
     *
     * @param sordernum
     * @param strdate
     * @return java.util.List<com.hgzp.core.model.Tworderitem>
     * @author suny
     * @date 2024/3/13 19:23
     */
    List<Tworderitem> getFilePathBySordernum(String sordernum, String strdate);

    /**
     * 获取项目已用成本（订单明细之amountreceivable之和）
     * 方法功能:
     *
     * @param adprojectid
     * @return java.math.BigDecimal
     * @author songly
     * @date 2024/3/18 16:23
     */
    BigDecimal getAmountreceivableCountByprojectId(String adprojectid);

    /**
     * 核销数据汇总成 “人员-已收总额”
     * 方法功能:获取时间范围内核销数据汇总，按主页面部门、人员检索
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/3/14 17:18
     */
    IPage<OrderAndItemDTO> getMyReceivedSummariesPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception;

    /**
     * 我的实收 列表
     * 方法功能:获取时间范围内与“我”有关的核销数据，可按部门、人员检索，
     * 亦用于展示汇总明细
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/3/15 10:48
     */
    IPage<OrderAndItemDTO> getMyReceivedPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception;
}
