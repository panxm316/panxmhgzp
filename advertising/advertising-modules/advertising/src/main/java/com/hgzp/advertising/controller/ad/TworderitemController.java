package com.hgzp.advertising.controller.ad;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * TworderitemController
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/12/5 12:35
 *
 * @folder ad/TworderitemController
 */
@RestController
@RequestMapping("/ad/tworderitem")
@Validated
public class TworderitemController extends BaseController {
    @Autowired
    private TworderitemServiceI orderitemServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取广告订单明细
     * 方法功能: 获取广告订单明细
     *
     * @param sOrderId
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tworderitem>>
     * @author songly
     * @date 2023/12/5 12:43
     */
    @GetMapping(value = "/getorderItemlist")
    public Json<List<Tworderitem>> getOrderItemList(String sOrderId) {
        try {
            List<Tworderitem> lsItems = orderitemServiceI.getOrderItemByOrderId(sOrderId);
            return Json.success(lsItems);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除订单明细
     * 方法功能: 删除订单明细
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deleteorderItem")
    public Json deleteOrderItem(String ids) {
        try {
            orderitemServiceI.deleteOrderItem(ids);
            //更新订单状态
            List<String> idList = Arrays.asList(ids.split(","));
            for (String id : idList) {
                orderitemServiceI.updateOrderStatus(id);
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id删除订单明细
     * 方法功能: 根据Id删除订单明细
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deleteorderItemById")
    public Json deleteOrderItemById(String ids) {
        try {
            orderitemServiceI.deleteOrderItemById(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 保存订单明细
     * 方法功能: 保存订单明细
     *
     * @param orderitem
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/11 15:23
     */
    @PostMapping(value = "/saveorderItem")
    public Json saveOrderItem(Tworderitem orderitem) {
        try {
            innerInterceptor.recoredLog();
            return orderitemServiceI.saveOrderItem(orderitem);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改订单明细
     * 方法功能: 修改订单明细
     *
     * @param orderitem
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/updateorderItem")
    public Json updateOrderItem(Tworderitem orderitem) {
        try {
            innerInterceptor.recoredLog();
            return orderitemServiceI.updateOrderItem(orderitem);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 订单明细审批申请
     * 方法功能: 订单明细审批申请
     *
     * @param id
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/6 15:30
     */
    @PostMapping(value = "/approveOrder")
    public Json doApproveOrder(@NotNull(message = "ID不可为空") String id,
                               @NotNull(message = "flowId不可为空") String flowId, String approveType) {
        try {
            //申请审批
            Integer iApproveType = Integer.valueOf(approveType);
            Json<String> jsonRet = orderitemServiceI.approveOrderItem(id, flowId, iApproveType);
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                orderitemServiceI.updateOrderItemApprovalopinions(applicationid, id, false, "", iApproveType,
                        ApproveStatus.APPROVE_EDIT.key);
                return Json.fail("订单申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            orderitemServiceI.updateOrderItemApprovalopinions(applicationid, id, false, "", iApproveType,
                    ApproveStatus.APPROVE_EDITING.key);

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("申请审核失败  " + e.getMessage());
        }
    }

    /**
     * 据客户id，查询所有有欠款的订单的广告明细
     * 方法功能:据客户id，查询所有有欠款的订单的广告明细。限制：有欠款、“审核状态”通过；可选限制：刊发时间(预见报开始日期)或合同号
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/25 14:24
     */
    @GetMapping("/getOrderAndItemByCustomerid")
    public Json getOrderAndItemByCustomerid(PageRequest pageRequest, CustomerChargeBankVO query) {
        Page<OrderAndItemDTO> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = orderitemServiceI.getOrderAndItemByCustomerid(page, query);
        return Json.success(pageList);
    }


    /**
     * 查询业绩列表（业绩金额）
     * 方法功能:列表查询tworderitem表。
     * 条件：时间范围、媒体、刊发状态（iapprovestatus=2通过且buse=1有效）
     * 返回：订单明细相关综合对象（OrderAndItemDTO）
     * 注：以orderitem 订单刊期/订单详情 数据为主
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author yanz
     * @date 2024/1/10 16:11
     */
    @GetMapping("/getPerformanceRatio")
    public Json<List<OrderAndItemDTO>> getPerformanceRatio(PageRequest pageRequest, OrderAndItemVO query) {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = orderitemServiceI.getPerformanceRatio(page, query);
        return Json.success(pageList);
    }
}