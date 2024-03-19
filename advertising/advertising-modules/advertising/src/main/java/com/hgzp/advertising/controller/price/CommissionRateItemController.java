package com.hgzp.advertising.controller.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.CommissionRateItemDTO;
import com.hgzp.advertising.pagemodel.price.vo.CommissionRateItemVO;
import com.hgzp.advertising.service.price.TbcommissionrateitemServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbcommissionrateitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * CommissionRateItemController
 * 创建人：suny
 * 类描述：计提比例明细表 前端控制器
 * 创建日期：2023/11/25 9:55
 *
 * @folder price/CommissionRateItemController
 */
@RestController
@RequestMapping("/price/commissionrateitem")
public class CommissionRateItemController extends BaseController {
    @Autowired
    TbcommissionrateitemServiceI tbcommissionrateitemServiceI;

    /**
     * 根据查询条件获取计提比例明细表分页列表
     * 方法功能: 根据查询条件获取计提比例明细表分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbcommissionrateitem>>
     * @author suny
     * @date 2023/11/25 10:04
     */
    @GetMapping("/getCommissionRateItemPageList")
    public Json<List<Tbcommissionrateitem>> getCommissionRateItemPageList(PageRequest pageRequest, CommissionRateItemVO query) throws Exception {
        Page<Tbcommissionrateitem> page = getPage(pageRequest);
        Page<Tbcommissionrateitem> commissionRateItemPageList = tbcommissionrateitemServiceI.getCommissionRateItemPageList(page, query);
        return Json.success(commissionRateItemPageList);
    }

    /**
     * 新增保存计提比例明细表
     * 方法功能:  新增保存计提比例明细表
     *
     * @param entiry
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbcommissionrateitem>
     * @author suny
     * @date 2023/11/25 10:05
     */
    @PostMapping("/saveCommissionRateItem")
    public Json<Tbcommissionrateitem> saveCommissionRateItem(@Validated(value = {ValidateParam.add.class}) @RequestBody CommissionRateItemDTO entiry) throws Exception {
        Tbcommissionrateitem tbcommissionrateitem = new Tbcommissionrateitem();
        BeanUtils.copyProperties(entiry, tbcommissionrateitem);
        tbcommissionrateitemServiceI.saveCommissionRateItem(tbcommissionrateitem);
        return Json.success(tbcommissionrateitem);
    }

    /**
     * 修改保存计提比例明细表
     * 方法功能: 修改保存计提比例明细表
     *
     * @param entiry
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/25 10:05
     */
    @PostMapping("/updateCommissionRateItem")
    public Json updateCommissionRateItem(@Validated(value = {ValidateParam.edit.class}) @RequestBody CommissionRateItemDTO entiry) throws Exception {
        Tbcommissionrateitem tbcommissionrateitem = new Tbcommissionrateitem();
        BeanUtils.copyProperties(entiry, tbcommissionrateitem);
        tbcommissionrateitemServiceI.updateCommissionRateItem(tbcommissionrateitem);
        return Json.success();
    }

    /**
     * 删除计提比例明细表数据
     * 方法功能: 删除计提比例明细表数据
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/25 10:05
     */
    @PostMapping("/deleteCommissionRateItem")
    public Json deleteCommissionRateItem(@NotBlank(message = "计提比例明细id不能为空") String ids) {
        tbcommissionrateitemServiceI.deleteCommissionRateItem(ids);
        return Json.success();
    }
}
