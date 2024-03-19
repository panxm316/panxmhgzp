package com.hgzp.advertising.controller.personal;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerQueryVo;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO;
import com.hgzp.advertising.pagemodel.finance.vo.InvoiceVO;
import com.hgzp.advertising.pagemodel.personal.dto.MyTaskDTO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.business.TwtasksServiceI;
import com.hgzp.advertising.service.customer.TwcustomerbelongServiceI;
import com.hgzp.advertising.service.finance.InvoiceServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PersonalStatController
 * 创建人：suny
 * 类描述：个人中心-统计 前端控制器
 * 创建日期：2024/1/22 16:33
 *
 * @folder personal/PersonalStatController
 */
@RestController
@RequestMapping("/personal/personalstat")
public class PersonalStatController extends BaseController {
    @Autowired
    private TworderitembelongServiceI tworderitembelongServiceI;
    @Autowired
    private TwtasksServiceI twtasksServiceI;
    @Autowired
    private TwcustomerbelongServiceI twcustomerbelongServiceI;
    @Autowired
    private TworderitemServiceI tworderitemServiceI;
    @Autowired
    private TwcustomerchargeServiceI twcustomerchargeServiceI;
    @Autowired
    private InvoiceServiceI invoiceServiceI;
    @Autowired
    private CommissionServiceI commissionServiceI;

    /**
     * 业绩统计
     * 方法功能: 根据归属表中业绩比例与广告明细表中的业绩金额计算业绩
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author suny
     * @date 2024/1/23 8:56
     */
    @GetMapping("/getMyAchievementPageList")
    public Json<List<OrderAndItemDTO>> getMyAchievementPageList(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Tworderitembelong> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = tworderitembelongServiceI.getMyAchievementPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 获取业绩汇总
     * 方法功能:  根据查询条件获取我的业绩总和
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param query
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/23 14:45
     */
    @GetMapping("/getMyAchievementCount")
    public Json getMyAchievementCount(OrderAndItemVO query) throws Exception {
        Tworderitem tworderitem = tworderitembelongServiceI.getMyAchievementCount(query);
        return Json.success(tworderitem);
    }


    /**
     * 我的任务统计
     * 方法功能: 可分为月统计和年度统计，汇总业绩与任务表中月度、年度任务设置对比，显示完成比例
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.personal.dto.MyTaskDTO>>
     * @author suny
     * @date 2024/1/31 17:15
     */
    @GetMapping("/getMyTaskStat")
    public Json<List<MyTaskDTO>> getMyTaskStat(TaskReportsVO query) throws Exception {
        List<MyTaskDTO> pageList = twtasksServiceI.getMyTaskStat(query);
        return Json.success(pageList);
    }

    /**
     * 我的客户统计
     * 方法功能: 查看我的归属客户
     * 时间范围、人员id、是否当前人员（bcurflag=false，根据其他条件查询【人员id有一项必选】，bcurflag=true，只查当前人员信息【人员id不起作用】）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcustomer>>
     * @author suny
     * @date 2024/1/23 8:59
     */
    @GetMapping("/getMyCustomerPageList")
    public Json<List<Twcustomer>> getMyCustomerPageList(PageRequest pageRequest, CustomerQueryVo query) throws Exception {
        Page<Twcustomer> page = getPage(pageRequest);
        IPage<Twcustomer> pageList = twcustomerbelongServiceI.getMyCustomerPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 根据客户id和时间范围获取订单明细列表
     * 方法功能: 根据客户id和时间范围获取订单明细列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author suny
     * @date 2024/1/23 9:05
     */
    @GetMapping("/getOrderItemListByCustomerId")
    public Json<List<OrderAndItemDTO>> getOrderItemListByCustomerId(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<OrderAndItemDTO> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> list = tworderitemServiceI.getOrderItemListByCustomerId(page, query);
        return Json.success(list);
    }


    /**
     * 根据客户id获取客户账户列表
     * 方法功能: 根据客户id获取客户收费表中的客户收费记录  （twcustomeraccounts表作废，全部来源于twcustomercharge表）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcustomercharge>>
     * @author suny
     * @date 2024/1/30 15:11
     */
    @GetMapping("/getCustomerChargeByCustomerid")
    public Json<List<Twcustomercharge>> getCustomerChargeByCustomerid(PageRequest pageRequest, CustomerChargeVO query) throws Exception {
        Page<Twcustomercharge> page = getPage(pageRequest);
        IPage<Twcustomercharge> twcustomerchargeList = twcustomerchargeServiceI.getCustomerChargeByCustomerid(page, query);
        return Json.success(twcustomerchargeList);
    }

    /**
     * 根据客户id获取客户收费表中的客户收费记录汇总、剩余金额汇总
     * 方法功能: 根据客户id获取客户收费表中的客户收费记录汇总、剩余金额汇总
     *
     * @param query
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/30 15:32
     */
    @GetMapping("/getMyCustomerChargeCount")
    public Json getMyCustomerChargeCount(CustomerChargeVO query) throws Exception {
        Twcustomercharge twcustomercharge = twcustomerchargeServiceI.getMyCustomerChargeCount(query);
        return Json.success(twcustomercharge);
    }

    /**
     * 根据客户id获取发票列表
     * 方法功能:  根据客户id获取发票列表，关联预开发票表
     *
     * @param pageRequest
     * @param entity
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>>
     * @author suny
     * @date 2024/1/25 9:56
     */
    @GetMapping("/getInvoiceByCustomerid")
    public Json<List<InvoiceDTO>> getInvoiceByCustomerid(PageRequest pageRequest, InvoiceVO entity) throws Exception {
        Page<Twinvoice> page = getPage(pageRequest);
        IPage<InvoiceDTO> invoice = invoiceServiceI.getInvoiceByCustomerid(page, entity);
        return Json.success(invoice);
    }

    /**
     * 我的佣金统计
     * 方法功能: 查看已确认/已发放的佣金明细
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>>
     * @author suny
     * @date 2024/1/25 10:15
     */
    @GetMapping("/getMyCommissionPageList")
    public Json<List<OrderItemCommissionDTO>> getMyCommissionPageList(PageRequest pageRequest, OrderItemCommissionVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        query.setBstatflag(true);
        if (query.getBcurflag() != null && query.getBcurflag()) {
            query.setEmployid(user.getUserid());
            //如果只查当前人员时，部门条件不生效
            query.setDeptid(null);
        }
        // 人员id不为空时，部门条件不生效
        if (ObjectUtil.isNotEmpty(query.getEmployid())) {
            query.setDeptid(null);
        }
        Page<Twcommission> page = getPage(pageRequest);
        IPage<OrderItemCommissionDTO> pageList = commissionServiceI.getCommissionList(page, query);
        return Json.success(pageList);
    }

    /**
     * 获取佣金汇总
     * 方法功能: 获取佣金汇总
     *
     * @param query
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>
     * @author suny
     * @date 2024/2/1 9:08
     */
    @GetMapping("/getMyCommissionCount")
    public Json<OrderItemCommissionDTO> getMyCommissionCount(OrderItemCommissionVO query) throws Exception {
        OrderItemCommissionDTO orderItemCommissionDTO = commissionServiceI.getMyCommissionCount(query);
        return Json.success(orderItemCommissionDTO);
    }

    /**
     * 我的订单(明细)汇总
     * 方法功能:我的订单(明细)汇总
     *
     * @param query
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/3/6 10:17
     */
    @GetMapping("/getMyOrderItemCount")
    public Json<OrderAndItemDTO> getMyOrderItemCount(OrderAndItemVO query) throws Exception {
        OrderAndItemDTO orderAndItemDTO = tworderitemServiceI.getMyOrderItemCount(query);
        return Json.success(orderAndItemDTO);
    }

    /**
     * 我的订单
     * 方法功能: 按时间范围查看有我归属的广告详情，条件中可选择（仅欠款）（barrearsflag=true，只查询有欠款的数据）
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author suny
     * @date 2024/1/23 9:52
     */
    @GetMapping("/getMyOrderItemPageList")
    public Json<List<OrderAndItemDTO>> getMyOrderItemPageList(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = tworderitemServiceI.getMyOrderItemPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 我的发票
     * 方法功能: 按发票表中开票时间范围查看我的发票，条件中可选择（仅欠款，需要关联预开发票表）（barrearsflag=true，只查询有欠款的数据）
     * 时间范围、人员id、部门id、是否当前人员（bcurflag=false，根据其他条件查询【部门id、人员id有一项必选】，bcurflag=true，只查当前人员信息【部门id、人员id不起作用】）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>>
     * @author suny
     * @date 2024/1/23 9:56
     */
    @GetMapping("/getMyInvoicePageList")
    public Json<List<InvoiceDTO>> getMyInvoicePageList(PageRequest pageRequest, InvoiceVO query) throws Exception {
        Page<Twinvoice> page = getPage(pageRequest);
        IPage<InvoiceDTO> pageList = invoiceServiceI.getMyInvoicePageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 我的实收 列表
     * 方法功能:获取时间范围内与“我”有关的核销数据，可按部门、人员检索，
     * 亦用于展示汇总明细
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author yanz
     * @date 2024/3/14 16:51
     */
    @GetMapping("/getMyReceivedPageList")
    public Json<List<OrderAndItemDTO>> getMyReceivedPageList(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = tworderitemServiceI.getMyReceivedPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 我的实收 汇总
     * 方法功能:获取时间范围内核销数据汇总，按主页面部门、人员检索
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author yanz
     * @date 2024/3/14 17:15
     */
    @GetMapping("/getMyReceivedSummariesPageList")
    public Json<List<OrderAndItemDTO>> getMyReceivedSummariesPageList(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = tworderitemServiceI.getMyReceivedSummariesPageList(page, query);
        return Json.success(pageList);
    }
}

