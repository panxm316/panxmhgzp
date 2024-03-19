package com.hgzp.advertising.controller.schedule;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderItemBatchArrangeDTO;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemSupplementPublishReq;
import com.hgzp.advertising.pagemodel.schedule.dto.OrderitemarrangeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.flow.TwapplicationrelationsServiceI;
import com.hgzp.advertising.service.schedule.OrderitemSupplementPublishServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.add;

/**
 * OrderitemSupplementPublishController
 * 创建人：muyn
 * 类描述：订单补刊核查 加刊 停刊 前端控制器
 * 创建日期：2023-12-25
 *
 * @folder schedule/OrderitemSupplementPublishController
 */
@Validated
@RestController
@RequestMapping("/schedule/orderitemsupplementpublish")
public class OrderitemSupplementPublishController extends BaseController {
    @Autowired
    private OrderitemSupplementPublishServiceI orderitemPublishServiceI;
    @Autowired
    private TworderServiceI orderService;
    @Autowired
    private TworderitemServiceI orderitemService;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    private TwapplicationrelationsServiceI applicationrelationsServiceI;

    /**
     * 订单明细补刊确认
     * 方法功能: 订单明细补刊确认
     *
     * @param orderItemBatchArrangeDTO
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/25 10:53
     */
    @PostMapping("/saveOrderitemSupplementPublish")
    public Json saveOrderitemSupplementPublish(@RequestBody @Validated(ValidateParam.edit.class) OrderItemBatchArrangeDTO orderItemBatchArrangeDTO) {
        try {
            orderitemPublishServiceI.bacthOrderitemsupplementpublish(orderItemBatchArrangeDTO);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id获取订单明细补刊信息
     * 方法功能: 根据Id获取订单明细补刊信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>
     * @author hgsongly
     * @date 2023/12/25 10:54
     */
    @GetMapping("/getOrderitemSupplementPublishById")
    public Json<OrderitemarrangeVO> getOrderitemSupplementPublishById(@NotNull(message = "ID不可为空") String id) {
        return Json.success(orderitemPublishServiceI.getOrderitemsupplementpublishById(id));
    }

    /**
     * 根据条件获取订单明细补刊列表
     * 方法功能: 根据条件获取订单明细补刊列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>>
     * @author songly
     * @date 2023/12/25 10:54
     */
    @GetMapping("/getOrderitemSupplementPublishList")
    public Json<List<OrderitemarrangeVO>> getSupplementPublishlist(OrderitemSupplementPublishReq query) {
        List<OrderitemarrangeVO> lsResult = orderitemPublishServiceI.getOrderitemsupplementpublishlist(query);
        return Json.success(lsResult);
    }

    /**
     * 获取补刊分页数据
     * 方法功能:获取补刊分页数据
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.schedule.vo.OrderitemarrangeVO>>
     * @author songly
     * @date 2023/12/25 10:55
     */
    @GetMapping("/getOrderitemSupplementPublishPageList")
    public Json<List<OrderitemarrangeVO>> getOrderitemSupplementPublishPageList(PageRequest pageRequest,
                                                                                OrderitemSupplementPublishReq query) {
        Page<Tworderitem> page = getPage(pageRequest);
        return Json.success(orderitemPublishServiceI.getOrderitemsupplementpublishPageList(page, query));
    }

    /**
     * 撤回补刊确认
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/2/20 13:53
     */
    @PostMapping("/orderitemsupplementRecall")
    public Json orderitemsupplementRecall(@NotNull(message = "ID不可为空") String id) {
        try {
            orderitemPublishServiceI.orderitemsupplementRecall(id);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 加（补）刊
     * 方法功能: 加（补）刊
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/12 14:09
     */
    @PostMapping("/addOrderitemByItemId")
    public Json addOrderitemByItemId(@NotNull(message = "ID不可为空") String id,
                                     @NotNull(message = "flowId不可为空") String flowId) {
        try {
            orderitemPublishServiceI.addOrderitemByItemId(id);

            Tworderitem orderItem = orderitemService.getById(id);
            String sOrderId = orderItem.getOrderid().toString();
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDERCHANGE.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                orderService.updateOrderApprovalopinions("", sOrderId, false, "",
                        ApproveType.APPROVE_Add.getKey()
                        , ApproveStatus.APPROVE_PASS.key, id);
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = orderService.approveOrderItemAddChangeStop(id, flowId,
                    ApproveType.APPROVE_Add.getKey());
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                        ApproveType.APPROVE_Add.getKey(), ApproveStatus.APPROVE_EDIT.key, id);
                return Json.fail("停刊申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                    ApproveType.APPROVE_Add.getKey()
                    , ApproveStatus.APPROVE_EDITING.key, id);
            //写关联表
            applicationrelationsServiceI.saveApplicationRelations(applicationid, Long.valueOf(sOrderId),
                    Long.valueOf(id),
                    ApproveStatus.APPROVE_EDITING.key, FlowTypes.FLOW_ORDERCHANGE.getKey(),
                    ApproveType.APPROVE_Add.getKey().toString());

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 改刊
     * 方法功能:  改刊
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/12 14:09
     */
    @PostMapping("/changeOrderitemByItemId")
    public Json ChangeOrderitemByItemId(@NotNull(message = "ID不可为空") String id,
                                        @NotNull(message = "flowId不可为空") String flowId) {
        try {
            orderitemPublishServiceI.ChangeOrderitemByItemId(id);
            Tworderitem orderItem = orderitemService.getById(id);
            String sOrderId = orderItem.getOrderid().toString();
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDERCHANGE.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                orderService.updateOrderApprovalopinions("", sOrderId, false, "",
                        ApproveType.APPROVE_Change.getKey()
                        , ApproveStatus.APPROVE_PASS.key, id);
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = orderService.approveOrderItemAddChangeStop(id, flowId,
                    ApproveType.APPROVE_Change.getKey());
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                        ApproveType.APPROVE_Change.getKey(), ApproveStatus.APPROVE_EDIT.key, id);
                return Json.fail("停刊申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                    ApproveType.APPROVE_Change.getKey()
                    , ApproveStatus.APPROVE_EDITING.key, id);
            //写关联表
            applicationrelationsServiceI.saveApplicationRelations(applicationid, Long.valueOf(sOrderId),
                    Long.valueOf(id),
                    ApproveStatus.APPROVE_EDITING.key, FlowTypes.FLOW_ORDERCHANGE.getKey(),
                    ApproveType.APPROVE_Change.getKey().toString());

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 停刊
     * 方法功能:  停刊
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/12 14:09
     */
    @PostMapping("/stopOrderitemByItemId")
    public Json StopOrderitemByItemId(@NotNull(message = "ID不可为空") String ids,
                                      @NotNull(message = "flowId不可为空") String flowId) {
        try {
            orderitemPublishServiceI.StopOrderitemByItemId(ids);
            List<String> idList = Arrays.asList(ids.split(","));

            if (idList.size() > 0) {
                for (String orderitemId : idList) {
                    Tworderitem orderItem = orderitemService.getById(orderitemId);
                    String sOrderId = orderItem.getOrderid().toString();
                    Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDERCHANGE.getKey());
                    if (flowInfo == null) {
                        return Json.fail("请设置审批流程");
                    }
                    if (!flowInfo.getBactive()) {
                        orderService.updateOrderApprovalopinions("", sOrderId, false, "",
                                ApproveType.APPROVE_Stop.getKey()
                                , ApproveStatus.APPROVE_PASS.key, orderitemId);
                        return Json.success();
                    }
                    //申请审批
                    Json<String> jsonRet = orderService.approveOrderItemAddChangeStop(orderitemId, flowId,
                            ApproveType.APPROVE_Stop.getKey());
                    if (!jsonRet.isSuccess()) {
                        return jsonRet;
                    }
                    String applicationid = jsonRet.getObj();

                    if (StrUtil.isBlank(applicationid)) {
                        orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                                ApproveType.APPROVE_Stop.getKey(), ApproveStatus.APPROVE_EDIT.key, orderitemId);
                        return Json.fail("停刊申请审核失败！请重新申请");
                    }
                    //更新状态及申请Id
                    orderService.updateOrderApprovalopinions(applicationid, sOrderId, false, "",
                            ApproveType.APPROVE_Stop.getKey()
                            , ApproveStatus.APPROVE_EDITING.key, orderitemId);
                    //写关联表
                    applicationrelationsServiceI.saveApplicationRelations(applicationid, Long.valueOf(sOrderId),
                            Long.valueOf(orderitemId),
                            ApproveStatus.APPROVE_EDITING.key, FlowTypes.FLOW_ORDERCHANGE.getKey(),
                            ApproveType.APPROVE_Stop.getKey().toString());
                }
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
}
