package com.hgzp.advertising.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.service.finance.TwcostemployServiceI;
import com.hgzp.advertising.service.finance.TworderitemcostServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * OrderItemCostController
 * 创建人：suny
 * 类描述：TODO 广告成本表 前端控制器
 * 创建日期：2023/12/13 16:21
 *
 * @folder finance/OrderItemCostController
 */
@RestController
@RequestMapping("/finance/orderitemcost")
public class OrderItemCostController extends BaseController {
    @Autowired
    TworderitemcostServiceI tworderitemcostServiceI;
    @Autowired
    TwcostemployServiceI twcostemployServiceI;

    /**
     * 查询订单以及相关成本列表
     * 方法功能: 查询订单以及相关成本列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author suny
     * @date 2023/12/13 16:25
     */
    @GetMapping("/getOrderAndItemPageList")
    public Json<List<OrderAndItemDTO>> getOrderAndItemPageList(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderAndItemDTO> pageList = tworderitemcostServiceI.getOrderAndItemPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 新增保存广告成本
     * 方法功能: 新增保存广告成本
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/14 9:53
     */
    @PostMapping(value = "/saveOrderItemCost")
    public Json saveOrderItemCost(@Validated(value = {ValidateParam.add.class}) @RequestBody OrderItemCostDTO entity) throws Exception {
        tworderitemcostServiceI.saveOrderItemCost(entity);
        return Json.success();
    }

    /**
     * 修改保存广告成本
     * 方法功能: 修改保存广告成本
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/14 9:53
     */
    @PostMapping(value = "/updateOrderItemCost")
    public Json updateOrderItemCost(@Validated(value = {ValidateParam.edit.class}) @RequestBody OrderItemCostDTO entity) throws Exception {
        tworderitemcostServiceI.updateOrderItemCost(entity);
        return Json.success();
    }

    /**
     * 删除广告成本
     * 方法功能: 删除广告成本
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/14 9:53
     */
    @PostMapping(value = "/deleteOrderItemCostById")
    public Json deleteOrderItemCostById(@NotNull(message = "ID不可为空") String ids) throws Exception {
        tworderitemcostServiceI.deleteOrderItemCost(ids);
        return Json.success();
    }

    /**
     * 更新广告成本状态
     * 方法功能: 更新广告成本状态
     *
     * @param id
     * @param istatus
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/14 9:53
     */
    @PostMapping(value = "/updateOrderItemCostStatus")
    public Json updateOrderItemCostStatus(@NotNull(message = "ID不可为空") String id, int istatus) throws Exception {
        tworderitemcostServiceI.updateOrderItemCostStatus(id, istatus);
        return Json.success();
    }

    //---------------------------以下为成本导入相关

    /**
     * 导入人员成本
     * 方法功能: 导入人员成本
     *
     * @param request
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/18 18:18
     */
    @PostMapping("/importCostEmploy")
    public Json importCostEmploy(HttpServletRequest request) throws Exception {
        FileInfo upload = WebUploadUtil.upload(request);
        twcostemployServiceI.importCostEmploy(upload);
        return Json.success();
    }
}

