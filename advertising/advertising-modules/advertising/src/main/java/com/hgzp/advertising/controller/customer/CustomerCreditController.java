package com.hgzp.advertising.controller.customer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.customer.TbcustomercreditServiceI;
import com.hgzp.core.model.Tbcustomercredit;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CustomerCreditController
 * 创建人：songly
 * 类描述：客户信誉度服务接口
 * 创建日期：2023/11/24 13:33
 *
 * @folder customer/CustomerCreditController
 */
@RestController
@RequestMapping("/customer/customercredit")
public class CustomerCreditController extends BaseController {
    @Autowired
    private TbcustomercreditServiceI customercreditService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取客户信誉度列表
     * 方法功能: 获取客户信誉度列表
     * @author songly
     * @date 2023/11/24 13:57
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbcustomercredit>>
     */
    @GetMapping(value = "/getcustomerCreditList")
    public Json<List<Tbcustomercredit>> getCustomerCreditList() {
        List<Tbcustomercredit> customercredits = customercreditService.getCustomerCreditList();
        return Json.success(customercredits);
    }
    /**
     * 获取客户信誉度下拉列表
     * 方法功能:获取客户信誉度下拉列表
     * @author songly
     * @date 2023/11/24 13:58
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping(value = "/getcustomerCreditCombo")
    public Json<List<DataCombo>> getcustomerCreditCombo() {
        List<DataCombo> customerData = customercreditService.getCustomerCreditCombo();
        return Json.success(customerData) ;
    }
    /**
     * 保存客户信誉度
     * 方法功能: 保存客户信誉度
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomercredit
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/savecustomerCredit")
    public Json saveCustomerCredit(@RequestBody Tbcustomercredit tbcustomercredit, HttpServletRequest request) throws Exception {
        try{
            if (customercreditService.isDuplicateSname(tbcustomercredit)) {
                return Json.fail("客户信誉度名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomercredit.setCreateempid(user.getUserid());
            tbcustomercredit.setCreateempname(user.getUsername());
            tbcustomercredit.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customercreditService.save(tbcustomercredit);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改客户信誉度
     * 方法功能: 修改客户信誉度
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomercredit
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/updatecustomerCredit")
    public Json updateCustomerCredit(@RequestBody Tbcustomercredit tbcustomercredit, HttpServletRequest request) throws Exception {
        try{
            if (customercreditService.isDuplicateSname(tbcustomercredit)) {
                return Json.fail("客户信誉度名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomercredit.setCreateempid(user.getUserid());
            tbcustomercredit.setCreateempname(user.getUsername());
            tbcustomercredit.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customercreditService.updateById(tbcustomercredit);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }


    /**
     * 根据Id获取客户信誉度
     * 方法功能:根据Id获取客户信誉度
     * @author songly
     * @date 2023/11/24 14:00
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbcustomercredit>
     */
    @GetMapping(value = "/getcustomerCreditById")
    public Json<Tbcustomercredit> getcustomerCreditById(@NotNull(message = "请传入需要查询的id") Long id) {
        Tbcustomercredit byId = customercreditService.getById(id);
        return Json.success(byId);
    }

    /**
     * 删除客户信誉度
     * 方法功能:删除客户信誉度
     * @author songly
     * @date 2023/11/24 14:00
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deletecustomerCredit")
    public Json deletecustomerCredit(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        customercreditService.removeByIds(CollUtil.newArrayList(ids.split(",")));
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能:获取最大序号
     * @author songly
     * @date 2023/11/24 14:00
     * @param
     * @return com.hgzp.core.page.Json
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = customercreditService.getMaxSort();
        return Json.success(maxSort);
    }
}
