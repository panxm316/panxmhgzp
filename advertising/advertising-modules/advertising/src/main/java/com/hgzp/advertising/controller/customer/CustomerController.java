package com.hgzp.advertising.controller.customer;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerReq;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerVo;
import com.hgzp.advertising.service.customer.TwcustomerServiceI;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CustomerController
 * 创建人：songly
 * 类描述：客户管理服务接口
 * 创建日期：2023/10/25 10:43
 *
 * @folder customer/CustomerController
 */
@RestController
@RequestMapping("/customer/customer")
@Validated
public class CustomerController extends BaseController {
    @Autowired
    private TwcustomerServiceI customerService;

    /**
     * 获取客户分页数据
     * 方法功能: 获取客户分页数据
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.customer.vo.CustomerVo>>
     * @author songly
     * @date 2023/10/26 9:40
     */
    @GetMapping(value = "/getCustomerPageList")
    public Json<List<CustomerVo>> getCustomerPageList(PageRequest pageRequest, CustomerReq query) {
        Page<Twcustomer> page = getPage(pageRequest);
        IPage<CustomerVo> pages = customerService.getCustomerPageList(page, query);
        return Json.success(pages);
    }

    /**
     * 根据Id获取客户信息
     * 方法功能: 根据Id获取客户信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.customer.vo.CustomerVo>
     * @author hgsongly
     * @date 2023/10/26 9:41
     */
    @GetMapping(value = "/getCustomerById")
    public Json<CustomerVo> getCustomerById(@NotNull(message = "ID不可为空") String id) {
        CustomerVo twcustomer = customerService.getCustomerById(id);
        return ObjectUtil.isNotNull(twcustomer) ? Json.success(twcustomer) : Json.fail();
    }

    /**
     * 根据名称相似的下拉客户信息
     * 方法功能:  根据名称相似的下拉客户信息
     *
     * @param name
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/11/1 9:13
     */
    @GetMapping(value = "/getCustomerCombo")
    public Json<List<DataCombo>> getCustomerCombo(@NotNull(message = "ID不可为空") String name) {
        if ("".equals(name.trim())) {
            return Json.fail("传入的名称不能为空！");
        }
        List<DataCombo> customerData = customerService.getCustomerCombo(name);
        return Json.success(customerData);
    }

    /**
     * 更新 客户信息
     * 方法功能: 更新客户信息
     *
     * @param customer
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/25 14:04
     */
    @PostMapping(value = "/updateCustomer")
    public Json updateCustomer(@RequestBody CustomerVo customer) {
        try {
            customerService.updateCustomer(customer);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("更新失败  " + e.getMessage());
        }
    }

    /**
     * 添加 客户信息
     * 方法功能: 添加客户信息
     *
     * @param customer
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:36
     */
    @PostMapping(value = "/saveCustome")
    public Json saveCustomer(@RequestBody CustomerVo customer) {
        try {
            customerService.saveCustomer(customer);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("保存失败 " + e.getMessage());
        }
    }

    /**
     * 根据id删除
     * 方法功能: 根据id删除
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:34
     */
    @PostMapping(value = "/deleteCustomerById")
    public Json deleteCustomerById(@NotNull(message = "ID不可为空") String ids) {
        return customerService.deleteCustomerByIds(ids);
    }

    /**
     * 获取客户信息最大序号
     * 方法功能: 获取最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author songly
     * @date 2023/10/26 9:49
     */
    @GetMapping("/getCustomerMaxSort")
    public Json<Integer> getCustomerMaxSort() {
        Integer maxSort = customerService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 客户合并
     * 方法功能:
     *
     * @param sMainId
     * @param subIds
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/18 10:54
     */
    @PostMapping(value = "/combineCustomer")
    public Json combineCustomer(String sMainId, String subIds) {
        try {
            customerService.combineCustomer(sMainId, subIds);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("合并失败  " + e.getMessage());
        }
    }
}