package com.hgzp.advertising.controller.ad;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDTO;
import com.hgzp.advertising.pagemodel.ad.dto.PreOrderDTO;
import com.hgzp.advertising.pagemodel.ad.dto.PreOrderQueryDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderReq;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.ad.vo.TworderVO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

/**
 * TworderController
 * 创建人：wwk
 * 类描述：广告订单表 前端控制器
 * 创建日期：2023/11/15
 *
 * @folder ad/TworderController
 */
@RestController
@RequestMapping("/ad/tworder")
@Validated
public class TworderController extends BaseController {
    @Autowired
    private TworderServiceI orderService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TworderitembelongServiceI belongServiceI;
    @Autowired
    private ProduceServiceI produceService;
    @Autowired
    private TbflowServiceI tbflowServiceI;

    /**
     * 获取广告订单分页列表
     * 方法功能: 获取广告订单分页列表
     *
     * @param pageRequest
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.ad.dto.OrderDTO>>
     * @author songly
     * @date 2023/12/1 13:46
     */
    @GetMapping(value = "/getorderPagelist")
    public Json<List<OrderDTO>> getOrderPageList(PageRequest pageRequest, OrderReq queryInfo) {
        try {
            Page<Tworder> page = getPage(pageRequest);
            IPage<OrderDTO> pages = orderService.getOrderPageList(page, queryInfo);
            return Json.success(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id获取广告订单信息
     * 方法功能: 根据Id获取广告订单信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.ad.dto.OrderDTO>
     * @author songly
     * @date 2023/12/1 10:36
     */
    @GetMapping(value = "/getorderById")
    public Json<OrderDTO> getAdorderById(@NotNull(message = "请传入需要查询的id") String id) {
        OrderDTO byId = null;
        try {
            byId = orderService.getOrderInfo(id);
            return Json.success(byId);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage(), byId);
        }
    }

    /**
     * 保存广告订单
     * 方法功能:  保存广告订单
     *
     * @param orderDTO
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/1 11:00
     */
    @PostMapping(value = "/saveorder")
    public Json saveAdorder(@RequestBody OrderDTO orderDTO) {
        try {
            //稿号
            Long customerId = IdUtil.getSnowflakeNextId();
            orderDTO.setId(customerId);
            orderService.saveOrder(orderDTO);
            //折扣申请审批
            if (StrUtil.isNotBlank(orderDTO.getFlowId())) {
                //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
                // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
                Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CONTRACT.getKey());
                if (flowInfo == null) {
                    return Json.fail("请设置审批流程");
                }
                if (flowInfo.getBactive()) {
                    Json<String> jsonRet = orderService.discountApproveOrder(orderDTO.getId().toString(),
                            orderDTO.getFlowId());
                    if (!jsonRet.isSuccess()) {
                        return jsonRet;
                    }
                    String applicationid = jsonRet.getObj();
                    if (StrUtil.isBlank(applicationid)) {
                        orderService.updateOrderApprovalopinions(applicationid, orderDTO.getId().toString(), false, "",
                                ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_EDIT.getKey());
                        return Json.fail("申请审核失败！请重新申请");
                    }
                    //更新状态及申请Id
                    orderService.updateOrderApprovalopinions(applicationid, orderDTO.getId().toString(), false, "",
                            ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_EDITING.getKey());
                } else {
                    orderService.updateOrderApprovalopinions("", orderDTO.getId().toString(), false, "",
                            ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_PASS.key);
                }
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 更新广告订单
     * 方法功能: 更新广告订单
     *
     * @param orderDTO
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/1 11:01
     */
    @PostMapping(value = "/updateorder")
    public Json UpdateAdorder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.updateOrder(orderDTO);
            //申请审批
            if (StrUtil.isNotBlank(orderDTO.getFlowId())) {
                //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
                // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
                Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CONTRACT.getKey());
                if (flowInfo == null) {
                    return Json.fail("请设置审批流程");
                }
                if (flowInfo.getBactive()) {
                    Json<String> jsonRet = orderService.discountApproveOrder(orderDTO.getId().toString(),
                            orderDTO.getFlowId());
                    if (!jsonRet.isSuccess()) {
                        return jsonRet;
                    }
                    String applicationid = jsonRet.getObj();
                    if (StrUtil.isBlank(applicationid)) {
                        orderService.updateOrderApprovalopinions(applicationid, orderDTO.getId().toString(), false, "",
                                orderDTO.getIapproveType(), ApproveStatus.APPROVE_EDIT.key);
                        return Json.fail("申请审核失败！请重新申请");
                    }
                    //更新状态及申请Id
                    orderService.updateOrderApprovalopinions(applicationid, orderDTO.getId().toString(), false, "",
                            orderDTO.getIapproveType(), ApproveStatus.APPROVE_EDITING.key);
                } else {
                    orderService.updateOrderApprovalopinions("", orderDTO.getId().toString(), false, "",
                            ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_PASS.key);
                }
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除广告订单
     * 方法功能:  删除广告订单
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/1 11:02
     */
    @PostMapping(value = "/deleteorder")
    public Json deleteAdorder(@NotNull(message = "请传入需要删除的id") String ids) {
        try {
            return orderService.deleteOrder(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 折扣审批申请
     * 方法功能:
     *
     * @param id
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/6 15:09
     */
    @PostMapping(value = "/discountApproveOrder")
    public Json discountApproveOrder(@NotNull(message = "ID不可为空") String id,
                                     @NotNull(message = "flowId不可为空") String flowId) {
        try {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CONTRACT.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                orderService.updateOrderApprovalopinions("", id, false, "",
                        ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_PASS.key);
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = orderService.discountApproveOrder(id, flowId);
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                orderService.updateOrderApprovalopinions(applicationid, id, false, "",
                        ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_EDIT.key);
                return Json.fail("申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            orderService.updateOrderApprovalopinions(applicationid, id, false, "",
                    ApproveType.APPROVE_Discount.getKey(), ApproveStatus.APPROVE_EDITING.key);

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("折扣申请审核失败  " + e.getMessage());
        }
    }

    /**
     * 订单审批申请
     * 方法功能: 订单审批申请
     *
     * @param id
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/6 15:30
     */
    @PostMapping(value = "/approveOrder")
    public Json doApproveOrder(@NotNull(message = "ID不可为空") String id,
                               @NotNull(message = "flowId不可为空") String flowId) {
        try {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDER.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                orderService.updateOrderApprovalopinions("", id, false, "", ApproveType.APPROVE_Order.getKey()
                        , ApproveStatus.APPROVE_PASS.key);
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = orderService.approveOrder(id, flowId);
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                orderService.updateOrderApprovalopinions(applicationid, id, false, "",
                        ApproveType.APPROVE_Order.getKey(), ApproveStatus.APPROVE_EDIT.key);
                return Json.fail("订单申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            orderService.updateOrderApprovalopinions(applicationid, id, false, "", ApproveType.APPROVE_Order.getKey()
                    , ApproveStatus.APPROVE_EDITING.key);

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("申请审核失败  " + e.getMessage());
        }
    }


    /**
     * 获取指定业务员+客户的所有合同信息(预开用)VO
     * 方法功能:获取指定业务员+客户的所有合同信息(预开用)VO
     *
     * @param employeeid
     * @param customerid
     * @param queryKey   合同号
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO>>
     * @author yanz
     * @date 2023/11/30 15:50
     */
    @GetMapping("/getContractNumByEmployIdAndCustomerId")
    public Json<List<OrderforPreinvoapplyVO>> getContractNumByEmployIdAndCustomerId(Long employeeid, Long customerid, String queryKey) {
        List<OrderforPreinvoapplyVO> voList = belongServiceI
                .getOrderforPreinvoapplyVOByEmployId(employeeid, customerid, queryKey);
        return Json.success(voList);
    }

    /**
     * 删除订单明细归属信息
     * 方法功能: 按订单明细归属id删除订单明细归属信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/12/4 10:08
     */
    @PostMapping("/deleteOrderItemBelong")
    public Json deleteOrderItemBelong(@NotNull(message = "ID不可为空") String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            innerInterceptor.recoredLog();
            if (belongServiceI.removeByIds(idList)) {
                return Json.success();
            } else {
                return Json.fail("传入ID异常，删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取订单合同号
     *
     * @return {@link String}
     * @author wangxk
     * @since 2023-12-13
     */
    @GetMapping("/getOrderNo")
    public Json<String> getOrderNo() {
        String orderNo = produceService.getOrderNo();
        return Json.success("", orderNo);
    }

    /**
     * 保存广告预约 || 快速预约
     *
     * @author wangxk
     * @since 2023-12-22
     */
    @PostMapping("/savePreOrder")
    public Json savePreOrder(@Validated(ValidateParam.savePreOrder.class) @RequestBody PreOrderDTO preOrderDTO) {
        orderService.savePreOrder(preOrderDTO);
        return Json.success();
    }

    /**
     * 广告预约分页
     *
     * @param pageRequest 分页请求参数
     * @param query       查询参数
     * @return {@link Json}
     * @author wangxk
     * @since 2023-12-29
     */
    @GetMapping("/getPreOrderPageList")
    public Json<List<TworderVO>> getPreOrderPageList(PageRequest pageRequest, @Validated PreOrderQueryDTO query) {
        Page<Tworder> page = getPage(pageRequest);
        return Json.success(orderService.getPreOrderPageList(page, query));
    }

    /**
     * 根据订单id获取订单详情,包含订单明细和订单归属
     *
     * @param orderId 订单id
     * @return {@link Json<TworderVO>}
     */
    @GetMapping("/getPreOrder")
    public Json<TworderVO> getPreOrder(@Validated @NotNull(message = "订单id不可为空") Long orderId) {
        return Json.success(orderService.getPreOrder(orderId));
    }

    /**
     * 查询一段时间内的 认刊合同 / 广告订单
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码或合同号（queryKey）、客户名称（可填写可选择customername）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author yanz
     * @date 2024/1/4 14:34
     */
    @GetMapping("/getOrdersInPeriod")
    public Json<List<OrderAndItemDTO>> getOrdersInPeriod(PageRequest pageRequest, OrderAndItemVO query) {
        Page<Tworder> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = orderService.getOrdersInPeriod(page, query);
        return Json.success(pageList);
    }

    /**
     * 查询订单合同详情
     * 方法功能:按订单id，查询订单合同详情（纯List）
     *
     * @param orderId
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO>>
     * @author yanz
     * @date 2024/1/5 13:32
     */
    @GetMapping("/getOrderContractDetail")
    public Json<OrderContractDetailDTO> getOrderContractDetail(Long orderId) {
        return Json.success(orderService.getOrderContractDetail(orderId));
    }

    /**
     * 广告预约订单提交审核,待审->在审
     *
     * @param orderId 订单id
     * @return {@link Json}
     * @auther wangxk
     * @date 2024/1/5 13:58
     */
    @PostMapping("/submitPreOrder")
    public Json submitPreOrder(@Validated @NotBlank(message = "订单id不可为空") String orderId) {
        orderService.submitPreOrder(orderId);
        return Json.success();
    }

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
    @PostMapping("/approvePreOrder")
    public Json approvePreOrder(@Validated @NotBlank(message = "订单id不可为空; ") String orderId,
                                @Validated @Pattern(regexp = "^[23]$", message = "审核状态值只能是2或者3; ") String status,
                                String comment) {
        orderService.approvePreOrder(orderId, status, comment);
        return Json.success();
    }

    /**
     * 根据合同号获取订单详情
     * 方法功能:
     *
     * @param contractNum
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.ad.dto.OrderDTO>
     * @author songly
     * @date 2024/1/18 14:16
     */
    @GetMapping(value = "/getorderByContractNum")
    public Json<OrderDTO> getAdorderByContractNum(@NotNull(message = "请传入需要查询的合同号") String contractNum) {
        try {
            OrderDTO byId = orderService.getOrderInfoByContractNum(contractNum);
            return Json.success(byId);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
}
