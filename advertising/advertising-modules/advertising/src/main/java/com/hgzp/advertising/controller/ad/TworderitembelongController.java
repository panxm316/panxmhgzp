package com.hgzp.advertising.controller.ad;

import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.core.model.Tworderitembelong;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * TworderitembelongController
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/12/5 12:35
 *
 * @folder ad/TworderitembelongController
 */
@RestController
@RequestMapping("/ad/tworderitembelong")
@Validated
public class TworderitembelongController extends BaseController {
    @Autowired
    private TworderitembelongServiceI orderitembelongServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取广告订单明细归属
     * 方法功能: 获取广告订单明细归属
     *
     * @param sOrderItemIds
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tworderitembelong>>
     * @author songly
     * @date 2023/12/5 12:43
     */
    @GetMapping(value = "/getorderItemBelonglist")
    public Json<List<Tworderitembelong>> getOrderItemList(String sOrderItemIds) {
        try {
            List<Tworderitembelong> lsItems = orderitembelongServiceI.getOrderItemBelongByOrderItemId(sOrderItemIds);
            return Json.success(lsItems);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 保存订单明细归属
     * 方法功能: 保存订单明细归属
     *
     * @param orderitem
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/saveorderItemBelong")
    public Json saveOrderItem(Tworderitembelong orderitem) {
        try {
            innerInterceptor.recoredLog();
            orderitembelongServiceI.save(orderitem);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除订单明细归属
     * 方法功能: 删除订单明细归属
     *
     * @param orderItemIds
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deleteorderItemBelong")
    public Json deleteOrderItem(String orderItemIds) {
        try {
            orderitembelongServiceI.deleteOrderItemBelong(orderItemIds);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id删除订单明细归属
     * 方法功能: 根据Id删除订单明细归属
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deleteorderItemBelongById")
    public Json deleteOrderItemById(String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            innerInterceptor.recoredLog();
            orderitembelongServiceI.removeBatchByIds(idList);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改订单明细归属
     * 方法功能: 修改订单明细归属
     *
     * @param orderitem
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/updateorderItemBelong")
    public Json updateOrderItem(Tworderitembelong orderitem) {
        try {
            innerInterceptor.recoredLog();
            orderitembelongServiceI.updateById(orderitem);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取刊期归属列表（编辑业绩对象）
     * 方法功能: 据刊期id，获取归属列表
     *
     * @param orderitemId
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tworderitembelong>>
     * @author yanz
     * @date 2024/1/11 12:31
     */
    @GetMapping("/getOrderBelongListByOrderitemId")
    public Json<List<Tworderitembelong>> getOrderBelongListByOrderitemId(Long orderitemId) {
        try {
            List<Tworderitembelong> orderitembelongList = orderitembelongServiceI.getOrderBelongListByOrderitemId(orderitemId);
            return Json.success(orderitembelongList);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 保存业绩对象
     * 方法功能: 将入参DTO中的orderitem业绩金额和日期更新至对应刊期详情，更新修改后的刊期归属信息（List<Tworderitembelong>）
     *
     * @param commissionData
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2024/1/11 12:43
     */
    @PostMapping("/saveCommisstionData")
    public Json saveCommisstionData(@RequestBody OrderAndItemDTO commissionData) {
        return orderitembelongServiceI.saveCommisstionData(commissionData) ? Json.success() : Json.fail("保存失败");
    }

}