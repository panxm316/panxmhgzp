package com.hgzp.advertising.controller.price;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.advertising.service.price.TbdiscountitemServiceI;
import com.hgzp.core.model.Tbdiscountitem;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DiscountItemController
 * 创建人：songly
 * 类描述：折扣明细服务类
 * 创建日期：2023/11/22 14:46
 *
 * @folder price/DiscountItemController
 */
@RestController
@RequestMapping("/price/discountitem")
public class DiscountItemController extends BaseController {
    @Autowired
    private TbdiscountitemServiceI discountitemService;

    /**
     * 获取折扣明细分页列表
     * 方法功能:获取折扣明细分页列表
     *
     * @param pageRequeste
     * @param baseQueryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbdiscountitem>>
     * @author songly
     * @date 2023/11/22 14:05
     */
    @GetMapping("/getdiscountItemPageList")
    public Json<List<Tbdiscountitem>> getDiscountItemPageList(PageRequest pageRequeste, DiscountItemVo baseQueryInfo) {
        if (baseQueryInfo.getDiscountgroupid() == null) {
            return Json.fail("折扣总表id不可为空");
        }
        Page page = getPage(pageRequeste);
        IPage<Tbdiscountitem> adcolorList = discountitemService.getDiscountItemPageList(page, baseQueryInfo);
        return Json.success(adcolorList);
    }

    /**
     * 根据id获取折扣明细信息
     * 方法功能: 根据id获取折扣明细信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbdiscountitem>
     * @author songly
     * @date 2023/11/22 14:06
     */
    @GetMapping(value = "/getdiscountItemById")
    public Json<Tbdiscountitem> getDiscountItemById(@NotNull(message = "ID不可为空") String id) {
        Tbdiscountitem discountitem = discountitemService.getById(id);
        return ObjectUtil.isNotNull(discountitem) ? Json.success(discountitem) : Json.fail();
    }

    /**
     * 保存折扣明细信息
     * 方法功能: 保存折扣明细信息
     *
     * @param tbdiscountitem
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:06
     */
    @PostMapping("/savediscountItem")
    public Json saveDiscountItem(@RequestBody Tbdiscountitem tbdiscountitem) {
        try {
            discountitemService.saveDiscountItem(tbdiscountitem);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除折扣明细信息
     * 方法功能: 删除折扣明细信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:07
     */
    @PostMapping("/deletediscountItem")
    public Json deleteDiscountItem(@NotBlank(message = "ids不能为空") String ids) {
        try {
            discountitemService.deleteDiscountItem(ids);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改折扣明细信息
     * 方法功能: 修改折扣明细信息
     *
     * @param tbdiscountitem
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:07
     */
    @PostMapping("/updatediscountItem")
    public Json updateDiscountItem(@RequestBody Tbdiscountitem tbdiscountitem) {
        try {
            discountitemService.updateDiscountItem(tbdiscountitem);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 折扣明细下拉列表（按媒体、行业、类型检索）
     * 方法功能:
     *
     * @param discountItemVo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/11/23 10:40
     */
    @GetMapping(value = "/getdiscountItemList")
    public Json<List<Tbdiscountitem>> getDiscountItemList(DiscountItemVo discountItemVo) {
        // String scurYear=String.valueOf(DateUtil.thisYear());
        List<Tbdiscountitem> customerData = discountitemService.getDiscountItemList(discountItemVo);
        return Json.success(customerData);
    }
}