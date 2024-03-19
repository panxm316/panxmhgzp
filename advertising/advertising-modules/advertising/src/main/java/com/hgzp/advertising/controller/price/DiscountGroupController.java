package com.hgzp.advertising.controller.price;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.advertising.service.price.TbdiscountgroupServiceI;
import com.hgzp.core.model.Tbdiscountgroup;
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
 * DiscountGroupController
 * 创建人：songly
 * 类描述：折扣服务类
 * 创建日期：2023/11/22 13:37
 *
 * @folder price/DiscountGroupController
 */
@RestController
@RequestMapping("/price/discountgroup")
public class DiscountGroupController extends BaseController {
    @Autowired
    private TbdiscountgroupServiceI discountgroupService;

    /**
     * 获取折扣总表分页列表
     * 方法功能:获取折扣总表分页列表
     *
     * @param pageRequeste
     * @param baseQueryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbdiscountgroup>>
     * @author songly
     * @date 2023/11/22 14:05
     */
    @GetMapping("/getdiscountGroupPageList")
    public Json<List<Tbdiscountgroup>> getDiscountGroupPageList(PageRequest pageRequeste,
                                                                DiscountItemVo baseQueryInfo) {
        Page page = getPage(pageRequeste);
        IPage<Tbdiscountgroup> adcolorList = discountgroupService.getDiscountGroupPageList(page, baseQueryInfo);
        return Json.success(adcolorList);
    }

    /**
     * 根据id获取折扣总表信息
     * 方法功能: 根据id获取折扣总表信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbdiscountgroup>
     * @author songly
     * @date 2023/11/22 14:06
     */
    @GetMapping(value = "/getdiscountGroupById")
    public Json<Tbdiscountgroup> getDiscountGroupById(@NotNull(message = "ID不可为空") String id) {
        Tbdiscountgroup discountgroup = discountgroupService.getById(id);
        return ObjectUtil.isNotNull(discountgroup) ? Json.success(discountgroup) : Json.fail();
    }

    /**
     * 保存折扣总表信息
     * 方法功能: 保存折扣总表信息
     *
     * @param tbdiscountgroup
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:06
     */
    @PostMapping("/savediscountGroup")
    public Json saveDiscountGroup(@RequestBody Tbdiscountgroup tbdiscountgroup) {
        try {
            discountgroupService.saveDiscountGroup(tbdiscountgroup);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除折扣总表信息
     * 方法功能: 删除折扣总表信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:07
     */
    @PostMapping("/deletediscountGroup")
    public Json deleteDiscountGroup(@NotBlank(message = "ids不能为空") String ids) {
        try {
            discountgroupService.deleteDiscountGroup(ids);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改折扣总表信息
     * 方法功能: 修改折扣总表信息
     *
     * @param tbdiscountgroup
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/22 14:07
     */
    @PostMapping("/updatediscountGroup")
    public Json updateDiscountGroup(@RequestBody Tbdiscountgroup tbdiscountgroup) {
        try {
            discountgroupService.updateDiscountGroup(tbdiscountgroup);
            return Json.success();
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 折扣总表下拉列表
     * 方法功能:
     *
     * @param sYear
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbdiscountgroup>>
     * @author songly
     * @date 2023/12/5 16:39
     */
    @GetMapping(value = "/getdiscountItemList")
    public Json<List<Tbdiscountgroup>> getDiscountItemList(String sYear) {
        List<Tbdiscountgroup> customerData = discountgroupService.getDiscountGroupList(sYear);
        return Json.success(customerData);
    }

    /**
     * 获取年度折扣
     * 方法功能:获取年度折扣(有符合条件的明细折扣取明细中定义的折扣，否则取总表折扣)
     *
     * @param discountItemVo
     * @return com.hgzp.core.page.Json
     * @author hgsongly
     * @date 2023/12/14 8:36
     */
    @GetMapping(value = "/getNdiscount")
    public Json getNdiscount(DiscountItemVo discountItemVo) {
        try {
            String sNdiscount = discountgroupService.getNdiscountByYear(discountItemVo);
            return Json.success("", sNdiscount);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

}