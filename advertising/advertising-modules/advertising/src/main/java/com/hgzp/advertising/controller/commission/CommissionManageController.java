package com.hgzp.advertising.controller.commission;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.core.model.Twcommission;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * CommissionManageController
 * 创建人：suny
 * 类描述：佣金管理相关接口
 * 创建日期：2024/1/15 16:58
 *
 * @folder commission/CommissionManageController
 */
@RestController
@RequestMapping("/commission/commissionmanage")
public class CommissionManageController extends BaseController {
    @Autowired
    private CommissionServiceI commissionServiceI;

    /**
     * 查询佣金计提分页列表
     * 方法功能:  根据查询条件获取佣金计提分页列表
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>>
     * @author suny
     * @date 2024/1/16 11:10
     */
    @GetMapping("/getCommissionList")
    public Json<List<OrderItemCommissionDTO>> getCommissionList(PageRequest pageRequest, OrderItemCommissionVO vo) throws Exception {
        Page<Twcommission> page = getPage(pageRequest);
        IPage<OrderItemCommissionDTO> pageList = commissionServiceI.getCommissionList(page, vo);
        return Json.success(pageList);
    }

    /**
     * 保存佣金计提状态和说明信息
     * 方法功能: 保存佣金计提状态和说明信息
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/16 14:02
     */
    @PostMapping(value = "/saveCommission")
    public Json saveCommission(@RequestBody List<OrderItemCommissionDTO> entity) throws Exception {
        commissionServiceI.saveCommission(entity);
        return Json.success();
    }

    /**
     * 根据id删除佣金计提信息
     * 方法功能: 根据id删除佣金计提信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/16 14:14
     */
    @PostMapping(value = "/deleteCommissionById")
    public Json deleteCommissionById(@NotNull(message = "ID不可为空") String ids) throws Exception {
        commissionServiceI.deleteCommission(ids);
        return Json.success();
    }

    /**
     * 根据订单刊期id查询佣金计提列表
     * 方法功能: 根据订单刊期id查询佣金计提列表
     *
     * @param orderitemid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>>
     * @author suny
     * @date 2024/1/18 15:23
     */
    @GetMapping("/getCommissionListByItemId")
    public Json<List<OrderItemCommissionDTO>> getCommissionListByItemId(Long orderitemid) throws Exception {
        List<OrderItemCommissionDTO> List = commissionServiceI.getCommissionListByItemId(orderitemid);
        return Json.success(List);
    }
}

