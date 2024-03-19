package com.hgzp.advertising.controller.customer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.customer.TbcustomerstatusServiceI;
import com.hgzp.core.model.Tbcustomerstatus;
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
 * CustomerStatusController
 * 创建人：songly
 * 类描述：客户状态服务接口
 * 创建日期：2023/11/24 13:33
 *
 * @folder customer/CustomerStatusController
 */
@RestController
@RequestMapping("/customer/customerstatus")
public class CustomerStatusController extends BaseController {
    @Autowired
    private TbcustomerstatusServiceI customerstatusService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取客户状态列表
     * 方法功能: 获取客户状态列表
     * @author songly
     * @date 2023/11/24 13:57
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbcustomerstatus>>
     */
    @GetMapping(value = "/getcustomerStatusList")
    public Json<List<Tbcustomerstatus>> getCustomerStatusList() {
        List<Tbcustomerstatus> customerstatuss = customerstatusService.getCustomerStatusList();
        return Json.success(customerstatuss);
    }
    /**
     * 获取客户状态下拉列表
     * 方法功能:获取客户状态下拉列表
     * @author songly
     * @date 2023/11/24 13:58
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping(value = "/getcustomerStatusCombo")
    public Json<List<DataCombo>> getcustomerStatusCombo() {
        List<DataCombo> customerData = customerstatusService.getCustomerStatusCombo();
        return Json.success(customerData) ;
    }
    /**
     * 保存客户状态
     * 方法功能: 保存客户状态
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomerstatus
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/savecustomerStatus")
    public Json saveCustomerStatus(@RequestBody Tbcustomerstatus tbcustomerstatus, HttpServletRequest request) throws Exception {
        try{
            if (customerstatusService.isDuplicateSname(tbcustomerstatus)) {
                return Json.fail("客户状态名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomerstatus.setCreateempid(user.getUserid());
            tbcustomerstatus.setCreateempname(user.getUsername());
            tbcustomerstatus.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customerstatusService.save(tbcustomerstatus);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改客户状态
     * 方法功能: 修改客户状态
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomerstatus
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/updatecustomerStatus")
    public Json updateCustomerStatus(@RequestBody Tbcustomerstatus tbcustomerstatus, HttpServletRequest request) throws Exception {
        try{
            if (customerstatusService.isDuplicateSname(tbcustomerstatus)) {
                return Json.fail("客户状态名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomerstatus.setCreateempid(user.getUserid());
            tbcustomerstatus.setCreateempname(user.getUsername());
            tbcustomerstatus.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customerstatusService.updateById(tbcustomerstatus);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }


    /**
     * 根据Id获取客户状态
     * 方法功能:根据Id获取客户状态
     * @author songly
     * @date 2023/11/24 14:00
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbcustomerstatus>
     */
    @GetMapping(value = "/getcustomerStatusById")
    public Json<Tbcustomerstatus> getcustomerStatusById(@NotNull(message = "请传入需要查询的id") Long id) {
        Tbcustomerstatus byId = customerstatusService.getById(id);
        return Json.success(byId);
    }

    /**
     * 删除客户状态
     * 方法功能:删除客户状态
     * @author songly
     * @date 2023/11/24 14:00
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deletecustomerStatus")
    public Json deletecustomerStatus(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        customerstatusService.removeByIds(CollUtil.newArrayList(ids.split(",")));
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
        Integer maxSort = customerstatusService.getMaxSort();
        return Json.success(maxSort);
    }
}
