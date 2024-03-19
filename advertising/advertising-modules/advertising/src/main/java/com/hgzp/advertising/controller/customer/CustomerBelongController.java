package com.hgzp.advertising.controller.customer;

import cn.hutool.core.util.ObjectUtil;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo;
import com.hgzp.advertising.service.customer.TwcustomerbelongServiceI;
import com.hgzp.core.model.Twcustomerbelong;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CustomerBelongController
 * 创建人：songly
 * 类描述：客户归属业务员
 * 创建日期：2023/10/26 13:00
 *
 * @folder customer/CustomerBelongController
 */
@RestController
@RequestMapping("/customer/customerbelong")
@Validated
public class CustomerBelongController extends BaseController {
    @Autowired
    private TwcustomerbelongServiceI belongService;

    /**
     * 获取客户的附件资料
     * 方法功能: 获取客户的附件资料
     *
     * @param customerid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcustomerbelong>>
     * @author songly
     * @date 2023/10/25 14:04
     */
    @GetMapping(value = "/getCustomerbelongList")
    public Json<List<CustomerBelongVo>> getCustomerbelongList(@NotBlank(message = "客户Id不能为空！") String customerid) {
        List<CustomerBelongVo> lsbelong = belongService.getCustomerbelong(customerid);
        return Json.success(lsbelong);
    }
    /**
     * 根据Id获取文件信息
     * 方法功能: 根据Id获取客户信息
     * @author songly
     * @date 2023/10/26 9:21
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twcustomerbelong>
     */
    @GetMapping(value = "/getCustomerbelongById")
    public Json<CustomerBelongVo> getCustomerById(@NotNull(message = "ID不可为空") String id) {
        CustomerBelongVo twcustomer = belongService.getCustomerBelongById(id);
        return ObjectUtil.isNotNull(twcustomer) ? Json.success(twcustomer) : Json.fail();
    }
    /**
     * 更新 客户归属
     * 方法功能: 更新客户归属
     *
     * @param twCustomerbelong
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/25 14:04
     */
    @PostMapping(value = "/updateCustomerbelong")
    public Json updateCustomerbelong(@RequestBody Twcustomerbelong twCustomerbelong) {
        if(twCustomerbelong==null){
            return Json.fail("参数为空");
        }
        try {
             belongService.updateCustomerBelong(twCustomerbelong);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 添加 客户归属
     * 方法功能: 添加客户归属
     *
     * @param twCustomerbelong
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:36
     */
    @PostMapping(value = "/saveCustomerbelong")
    public Json saveCustomerbelong(@RequestBody Twcustomerbelong twCustomerbelong) {
        if(twCustomerbelong==null){
            return Json.fail("参数为空");
        }
        return belongService.saveCustomerBelong(twCustomerbelong);
    }

    /**
     * 根据id删除归属
     * 方法功能: 根据id删除归属
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:34
     */
    @PostMapping(value = "/deleteCustomerbelongById")
    public Json deleteCustomerbelongById(@NotNull(message = "ID不可为空") String ids) {
        try {
            belongService.deleteCustomerBelongByIds(ids, false);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 根据客户Id删除归属
     * 方法功能: 根据客户Id删除归属     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/26 13:05
     */
    @PostMapping(value = "/deleteCustomerbelongByCustomerId")
    public Json deleteCustomerbelongBCustomerId(@NotNull(message = "ID不可为空") String ids) {

        // innerInterceptor.recoredLog();
        try {
            belongService.deleteCustomerBelongByIds(ids,true);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("删除失败");
        }
    }
}