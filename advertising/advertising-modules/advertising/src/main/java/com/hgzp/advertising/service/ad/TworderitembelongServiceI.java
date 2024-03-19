package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitembelong;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 订单刊期归属表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TworderitembelongServiceI extends IMyService<Tworderitembelong> {
    /**
     * 根据订单明细ids查询订单明细归属信息
     * 方法功能:
     *
     * @param orderItemIds
     * @return java.util.List<com.hgzp.core.model.Tworderitembelong>
     * @author songly
     * @date 2023/11/30 14:52
     */
    List<Tworderitembelong> getOrderItemBelongByOrderItemId(String orderItemIds) throws Exception;

    /**
     * 据业务员id、客户id获取所有合同VO
     * 方法功能:据业务员id、客户id获取所有合同VO
     *
     * @param employeeid
     * @param customerid
     * @param queryKey 合同号
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO>
     * @author yanz
     * @date 2023/12/1 10:12
     */
    List<OrderforPreinvoapplyVO> getOrderforPreinvoapplyVOByEmployId(Long employeeid, Long customerid, String queryKey);

    /**
     * 根据订单明细ids删除订单明细归属信息
     * 方法功能:
     *
     * @param orderItemIds
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/4 9:52
     */
    Json deleteOrderItemBelong(String orderItemIds) throws Exception;

    /**
     * 获取主业务员信息
     * 方法功能:
     *
     * @param orderItemId
     * @return com.hgzp.core.page.DataCombo
     * @author songly
     * @date 2023/12/13 15:29
     */
    List<DataCombo> getMainEmpinfo(Long orderItemId);

    /**
     * 获取刊期归属列表（编辑业绩对象）
     * 方法功能: 据刊期id，获取归属列表
     *
     * @param orderitemId
     * @return java.util.List<com.hgzp.core.model.Tworderitembelong>
     * @author yanz
     * @date 2024/1/11 12:33
     */
    List<Tworderitembelong> getOrderBelongListByOrderitemId(Long orderitemId);

    /**
     * 保存业绩对象
     * 方法功能: 将入参DTO中的orderitem业绩金额和日期更新至对应刊期详情，更新修改后的刊期归属信息（List<Tworderitembelong>）
     *
     * @param commissionData
     * @return boolean
     * @author yanz
     * @date 2024/1/11 12:46
     */
    boolean saveCommisstionData(OrderAndItemDTO commissionData);

    /**
     * 查询订单明细相关综合对象列表(佣金计提)
     * 方法功能: 查询 佣金计提 用到的 订单明细相关综合对象列表
     *
     * @param page
     * @param vo
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/12 13:21
     */
    IPage<OrderItemCommissionDTO> getOrderAndItemDTOListForCommission(Page<OrderItemCommissionDTO> page, OrderItemCommissionVO vo);

    /**
     * 查询个人\部门业绩订单明细相关综合对象列表(业绩对象)
     * 方法功能: 根据归属表中业绩比例与广告明细表中的业绩金额计算业绩
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param page
     * @param vo
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @date 2024/1/12 13:21
     */
    IPage<OrderAndItemDTO> getMyAchievementPageList(Page<Tworderitembelong> page, OrderAndItemVO vo) throws Exception;

    /**
     * 获取业绩总和
     * 方法功能: 获取业绩总和
     * @author suny
     * @date 2024/1/29 16:54
     * @param vo
     * @return java.math.BigDecimal
     */
    Tworderitem getMyAchievementCount(OrderAndItemVO vo) throws Exception;
}
