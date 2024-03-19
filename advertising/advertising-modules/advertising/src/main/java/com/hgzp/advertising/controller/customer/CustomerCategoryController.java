package com.hgzp.advertising.controller.customer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.customer.TbcustomercategoryServiceI;
import com.hgzp.core.model.Tbcustomercategory;
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
 * CustomerCategoryController
 * 创建人：songly
 * 类描述：客户分类服务接口
 * 创建日期：2023/11/24 13:33
 *
 * @folder customer/CustomerCategoryController
 */
@RestController
@RequestMapping("/customer/customercategory")
public class CustomerCategoryController extends BaseController {
    @Autowired
    private TbcustomercategoryServiceI customercategoryService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取客户分类列表
     * 方法功能: 获取客户分类列表
     * @author songly
     * @date 2023/11/24 13:57
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbcustomercategory>>
     */
    @GetMapping(value = "/getcustomerCategoryList")
    public Json<List<Tbcustomercategory>> getCustomerCategoryList() {
        List<Tbcustomercategory> customercategorys = customercategoryService.getCustomerCategoryList();
        return Json.success(customercategorys);
    }
    /**
     * 获取客户分类下拉列表
     * 方法功能:获取客户分类下拉列表
     * @author songly
     * @date 2023/11/24 13:58
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping(value = "/getcustomerCategoryCombo")
    public Json<List<DataCombo>> getcustomerCategoryCombo() {
        List<DataCombo> customerData = customercategoryService.getCustomerCategoryCombo();
        return Json.success(customerData) ;
    }
    /**
     * 保存客户分类
     * 方法功能: 保存客户分类
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomercategory
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/savecustomerCategory")
    public Json saveCustomerCategory(@RequestBody Tbcustomercategory tbcustomercategory, HttpServletRequest request) throws Exception {
        try{
            if (customercategoryService.isDuplicateSname(tbcustomercategory)) {
                return Json.fail("客户分类名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomercategory.setCreateempid(user.getUserid());
            tbcustomercategory.setCreateempname(user.getUsername());
            tbcustomercategory.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customercategoryService.save(tbcustomercategory);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改客户分类
     * 方法功能: 修改客户分类
     * @author songly
     * @date 2023/11/24 13:59
     * @param tbcustomercategory
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/updatecustomerCategory")
    public Json updateCustomerCategory(@RequestBody Tbcustomercategory tbcustomercategory, HttpServletRequest request) throws Exception {
        try{
            if (customercategoryService.isDuplicateSname(tbcustomercategory)) {
                return Json.fail("客户分类名称重复");
            }
            LoginUser user = WebUtil.getLoginUser(request);
            tbcustomercategory.setCreateempid(user.getUserid());
            tbcustomercategory.setCreateempname(user.getUsername());
            tbcustomercategory.setCreatetime(DateUtil.date());

            innerInterceptor.recoredLog();
            customercategoryService.updateById(tbcustomercategory);
            return Json.success();
        }catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }


    /**
     * 根据Id获取客户分类
     * 方法功能:根据Id获取客户分类
     * @author songly
     * @date 2023/11/24 14:00
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbcustomercategory>
     */
    @GetMapping(value = "/getcustomerCategoryById")
    public Json<Tbcustomercategory> getcustomerCategoryById(@NotNull(message = "请传入需要查询的id") Long id) {
        Tbcustomercategory byId = customercategoryService.getById(id);
        return Json.success(byId);
    }

    /**
     * 删除客户分类
     * 方法功能:删除客户分类
     * @author songly
     * @date 2023/11/24 14:00
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deletecustomerCategory")
    public Json deletecustomerCategory(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        customercategoryService.removeByIds(CollUtil.newArrayList(ids.split(",")));
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
        Integer maxSort = customercategoryService.getMaxSort();
        return Json.success(maxSort);
    }
}
