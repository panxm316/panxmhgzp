package com.hgzp.advertising.controller.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.customer.dto.CustomerServiceDTO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerServiceVO;
import com.hgzp.advertising.pagemodel.media.dto.FoldDTO;
import com.hgzp.advertising.service.customer.TwcustomerserviceServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twcustomerservice;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CustomerServiceController
 * 创建人：suny
 * 类描述：客户服务controller
 * 创建日期：2023/11/09 09:09
 *
 * @folder customer/CustomerServiceController
 */
@RestController
@RequestMapping("/customer/customerservice")
public class CustomerServiceController extends BaseController {
    @Autowired
    TwcustomerserviceServiceI twcustomerserviceServiceI;

    /**
     * 根据条件查询客户服务列表
     * 方法功能: 根据条件查询客户服务列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcustomerservice>>
     * @author suny
     * @date 2023/11/9 10:59
     */
    @GetMapping("/getCustomerServicePageList")
    public Json<List<Twcustomerservice>> getCustomerServicePageList(PageRequest pageRequest, CustomerServiceVO query) throws Exception {
        Page<Twcustomerservice> page = getPage(pageRequest);
        IPage<Twcustomerservice> pageList = twcustomerserviceServiceI.getCustomerServicePageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 新增保存客户服务信息
     * 方法功能: 新增保存客户服务信息
     *
     * @param entity
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.media.dto.FoldDTO>
     * @author suny
     * @date 2023/11/9 11:00
     */
    @PostMapping(value = "/saveCustomerService")
    public Json<FoldDTO> saveCustomerService(@Validated(value = {ValidateParam.add.class}) @RequestBody CustomerServiceDTO entity) throws Exception {
        twcustomerserviceServiceI.saveCustomerService(entity);
        return Json.success();
    }

    /**
     * 修改保存客户服务信息
     * 方法功能: 修改保存客户服务信息
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/9 11:00
     */
    @PostMapping(value = "/updateCustomerService")
    public Json updateCustomerService(@Validated(value = {ValidateParam.edit.class}) @RequestBody CustomerServiceDTO entity) throws Exception {
        twcustomerserviceServiceI.updateCustomerService(entity);
        return Json.success();
    }

    /**
     * 删除客户服务信息
     * 方法功能: 删除客户服务信息(逻辑删除)
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/9 11:00
     */
    @PostMapping(value = "/deleteCustomerServiceById")
    public Json deleteCustomerServiceById(@NotNull(message = "ID不可为空") String ids) throws Exception {
        twcustomerserviceServiceI.deleteCustomerService(ids);
        return Json.success();
    }
}
