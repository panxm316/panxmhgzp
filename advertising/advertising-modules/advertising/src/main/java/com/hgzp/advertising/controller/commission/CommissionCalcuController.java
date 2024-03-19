package com.hgzp.advertising.controller.commission;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.pagemodel.finance.dto.CommissionDTO;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twcommission;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CommissionCalcuController
 * 创建人：suny
 * 类描述：佣金计提相关接口
 * 创建日期：2024/1/15 9:55
 *
 * @folder commission/CommissionCalcuController
 */
@RestController
@RequestMapping("/commission/commissioncalcu")
public class CommissionCalcuController extends BaseController {
    @Autowired
    private TworderitembelongServiceI orderitembelongServiceI;
    @Autowired
    private CommissionServiceI commissionServiceI;

    /**
     * 查询订单明细相关综合对象列表(佣金计提)
     * 方法功能: 查询 佣金计提 用到的 订单明细相关综合对象列表
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2024/1/12 13:17
     */
    @GetMapping("/getOrderAndItemDTOListForCommission")
    public Json<List<OrderItemCommissionDTO>> getOrderAndItemDTOListForCommission(PageRequest pageRequest, OrderItemCommissionVO vo) {
        Page<OrderItemCommissionDTO> page = getPage(pageRequest);
        IPage<OrderItemCommissionDTO> orderAndItemDTOList = orderitembelongServiceI.getOrderAndItemDTOListForCommission(page, vo);
        return Json.success(orderAndItemDTOList);
    }

    /**
     * 佣金计提计算结果保存或更新
     * 方法功能: 佣金计提计算结果保存或更新
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/16 17:16
     */
    @PostMapping(value = "/saveOrUpdateCommission")
    public Json saveOrUpdateCommission(@RequestBody OrderItemCommissionDTO entity) throws Exception {
        commissionServiceI.saveOrUpdateCommission(entity);
        return Json.success();
    }

    /**
     * 手动添加佣金计提明细
     * 方法功能: 手动添加佣金计提明细
     *
     * @param commissionDTO
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/16 11:10
     */
    @PostMapping("/saveAddCommission")
    public Json saveAddCommission(@RequestBody @Validated(ValidateParam.add.class) CommissionDTO commissionDTO) throws Exception {
        commissionServiceI.saveOrUpdata(commissionDTO);
        return Json.success();
    }

    /**
     * 查询手动提交的佣金计提列表
     * 方法功能:查询手动提交的佣金计提列表
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcommission>>
     * @author suny
     * @date 2024/3/18 10:39
     */
    @GetMapping("/getAddCommissionList")
    public Json<List<Twcommission>> getAddCommissionList(PageRequest pageRequest, OrderItemCommissionVO vo) throws Exception {
        Page<Twcommission> page = getPage(pageRequest);
        IPage<Twcommission> orderAndItemDTOList = commissionServiceI.getCommissionListByAddType(page, vo, "add");
        return Json.success(orderAndItemDTOList);
    }
}

