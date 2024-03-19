package com.hgzp.advertising.controller.customer;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.advertising.service.customer.TwcustomerFilingServiceI;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CustomerFilingController
 * 创建人：songly
 * 类描述：客户备案备案服务接口
 * 创建日期：2023/11/7 8:52
 *
 * @folder customer/CustomerFilingController
 */
@RestController
@RequestMapping("/customer/customerfiling")
@Validated
public class CustomerFilingController extends BaseController {
    @Autowired
    private TwcustomerFilingServiceI customerFilingServiceI;
    @Autowired
    private TbflowServiceI tbflowServiceI;

    /**
     * 获取客户备案分页数据
     * 方法功能: 获取客户备案分页数据
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.customer.vo.CustomerVo>>
     * @author songly
     * @date 2023/11/09 8:58
     */
    @GetMapping(value = "/getCustomerPageList")
    public Json<List<CustomerVo>> getCustomerPageList(PageRequest pageRequest, CustomerReq query) {
        Page<Twcustomer> page = getPage(pageRequest);
        IPage<CustomerVo> pages = null;
        try {
            pages = customerFilingServiceI.getMyCustomerPageList(page, query);
            return Json.success(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 更新 客户备案信息
     * 方法功能: 更新客户备案信息
     *
     * @param customer
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/09 8:58
     */
    @PostMapping(value = "/updateCustomer")
    public Json updateCustomer(@RequestBody CustomerVo customer) {
        try {
            customerFilingServiceI.updateCustomer(customer);
            //申请审批
            if (StrUtil.isNotBlank(customer.getFlowId())) {
                //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
                // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
                Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CUSTOMER.getKey());
                if (flowInfo == null) {
                    return Json.fail("请设置审批流程");
                }
                if (flowInfo.getBactive()) {
                    Json<String> jsonRet = customerFilingServiceI.approveCustomer(customer.getId().toString(),
                            customer.getFlowId());
                    if (!jsonRet.isSuccess()) {
                        return jsonRet;
                    }
                    String applicationid = jsonRet.getObj();
                    if (StrUtil.isBlank(applicationid)) {
                        //  customerFilingServiceI.updateCustomerStatus(applicationid,  customer.getId().toString(),
                        //  ApproveStatus.APPROVE_EDIT.key);
                        customerFilingServiceI.updateCustomerApprovalopinions(applicationid,
                                customer.getId().toString(), false, "", ApproveStatus.APPROVE_EDIT.getKey());
                        return Json.fail("申请审核失败！请重新申请");
                    }
                    //更新状态及申请Id
                    //customerFilingServiceI.updateCustomerStatus(applicationid, customer.getId().toString(),
                    // ApproveStatus.APPROVE_EDITING.key);
                    customerFilingServiceI.updateCustomerApprovalopinions(applicationid, customer.getId().toString(),
                            false, "", ApproveStatus.APPROVE_EDITING.getKey());
                } else {
                    customerFilingServiceI.updateCustomerApprovalopinions("", customer.getId().toString(), false, "",
                            ApproveStatus.APPROVE_PASS.getKey());
                }
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("更新失败  " + e.getMessage());
        }
    }

    /**
     * 添加 客户备案信息
     * 方法功能: 添加客户备案信息
     *
     * @param customer
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/09 8:58
     */
    @PostMapping(value = "/saveCustome")
    public Json saveCustomer(@RequestBody CustomerVo customer) {
        try {
            //稿号
            Long customerId = IdUtil.getSnowflakeNextId();
            customer.setId(customerId);
            customerFilingServiceI.saveCustomer(customer);
            //申请审批
            if (StrUtil.isNotBlank(customer.getFlowId())) {
                //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
                // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
                Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CUSTOMER.getKey());
                if (flowInfo == null) {
                    return Json.fail("请设置审批流程");
                }
                if (flowInfo.getBactive()) {
                    Json<String> jsonRet = customerFilingServiceI.approveCustomer(customer.getId().toString(),
                            customer.getFlowId());
                    if (!jsonRet.isSuccess()) {
                        return jsonRet;
                    }
                    String applicationid = jsonRet.getObj();
                    if (StrUtil.isBlank(applicationid)) {
                        //customerFilingServiceI.updateCustomerStatus(applicationid,  customer.getId().toString(),
                        // ApproveStatus.APPROVE_EDIT.key);
                        customerFilingServiceI.updateCustomerApprovalopinions(applicationid,
                                customer.getId().toString(), false, "", ApproveStatus.APPROVE_EDIT.getKey());
                        return Json.fail("申请审核失败！请重新申请");
                    }
                    //更新状态及申请Id
                    //   customerFilingServiceI.updateCustomerStatus(applicationid, customer.getId().toString(),
                    //   ApproveStatus.APPROVE_EDITING.key);
                    customerFilingServiceI.updateCustomerApprovalopinions(applicationid, customer.getId().toString(),
                            false, "", ApproveStatus.APPROVE_EDITING.getKey());
                } else {
                    customerFilingServiceI.updateCustomerApprovalopinions("", customer.getId().toString(), false, "",
                            ApproveStatus.APPROVE_PASS.getKey());
                }
            }
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("保存失败 " + e.getMessage());
        }
    }

    /**
     * 申请审批
     * 方法功能:   申请审批
     *
     * @param id
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/8 15:15
     */
    @PostMapping(value = "/approveCustomer")
    public Json approveCustomer(@NotNull(message = "ID不可为空") String id,
                                @NotNull(message = "flowId不可为空") String flowId) {
        try {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_CUSTOMER.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                customerFilingServiceI.updateCustomerApprovalopinions("", id, false, "",
                        ApproveStatus.APPROVE_PASS.getKey());
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = customerFilingServiceI.approveCustomer(id, flowId);
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                // customerFilingServiceI.updateCustomerStatus(applicationid, id,ApproveStatus.APPROVE_EDIT.key);
                customerFilingServiceI.updateCustomerApprovalopinions(applicationid, id, false, "",
                        ApproveStatus.APPROVE_EDIT.getKey());
                return Json.fail("申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            //customerFilingServiceI.updateCustomerStatus(applicationid, id,ApproveStatus.APPROVE_EDITING.key);
            customerFilingServiceI.updateCustomerApprovalopinions(applicationid, id, false, "",
                    ApproveStatus.APPROVE_EDITING.getKey());

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("申请审核失败  " + e.getMessage());
        }
    }

}