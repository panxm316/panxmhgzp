package com.hgzp.advertising.controller.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.PriceGroupDTO;
import com.hgzp.advertising.pagemodel.price.vo.PriceGroupVO;
import com.hgzp.advertising.service.price.TbpricegroupServiceI;
import com.hgzp.advertising.service.price.TbpriceitemServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbpricegroup;
import com.hgzp.core.page.*;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * PriceGroupController
 * 创建人：suny
 * 类描述：价格表 前端控制器
 * 创建日期：2023-11-10 11:02:52
 *
 * @folder price/PriceGroupController
 */
@RestController
@RequestMapping("/price/pricegroup")
public class PriceGroupController extends BaseController {
    @Autowired
    TbpricegroupServiceI tbpricegroupServiceI;

    @Autowired
    TbpriceitemServiceI tbpriceitemServiceI;

    /**
     * 根据查询条件获取价格组分页列表
     * 方法功能: 根据查询条件获取价格组分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpricegroup>>
     * @author suny
     * @date 2023/11/11 10:06
     */
    @GetMapping("/getPriceGroupPageList")
    public Json<List<Tbpricegroup>> getPriceGroupPageList(PageRequest pageRequest, PriceGroupVO query) throws Exception {
        Page<Tbpricegroup> page = getPage(pageRequest);
        Page<Tbpricegroup> priceGroupPageList = tbpricegroupServiceI.getPriceGroupPageList(page, query);
        return Json.success(priceGroupPageList);
    }

    /**
     * 根据查询条件获取后台设置价格组树形结构数据
     * 方法功能:  根据查询条件获取后台设置价格组树形结构数据
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author suny
     * @date 2023/11/10 14:00
     */
    @GetMapping("/getPriceGroupTree")
    public Json<List<TreeModel>> getPriceGroupTree(TreeQuery query) throws Exception {
        List<TreeModel> sysPriceGroupTree = tbpricegroupServiceI.getPriceGroupTree(query);
        return Json.success(sysPriceGroupTree);
    }

    /**
     * 新增保存价格组信息
     * 方法功能: 新增保存价格组信息
     *
     * @param entiry
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbpricegroup>
     * @author suny
     * @date 2023/11/10 14:01
     */
    @PostMapping("/savePriceGroup")
    public Json<Tbpricegroup> savePriceGroup(@Validated(value = {ValidateParam.add.class}) @RequestBody PriceGroupDTO entiry) throws Exception {
        Tbpricegroup tbpricegroup = new Tbpricegroup();
        BeanUtils.copyProperties(entiry, tbpricegroup);
        tbpricegroupServiceI.savePriceGroup(tbpricegroup);
        if (entiry.getCopyFlag()) {
            BaseQueryInfo query = new BaseQueryInfo();
            query.setStartTime(entiry.getStartTime());
            query.setEndTime(entiry.getEndTime());
            tbpriceitemServiceI.SaveCopyPriceItem(entiry.getId(), tbpricegroup.getId(), query);
        }
        return Json.success(tbpricegroup);
    }

    /**
     * 修改保存价格组信息
     * 方法功能: 修改保存价格组信息
     *
     * @param entiry
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/10 14:01
     */
    @PostMapping("/updatePriceGroup")
    public Json updatePriceGroup(@Validated(value = {ValidateParam.edit.class}) @RequestBody PriceGroupDTO entiry) throws Exception {
        Tbpricegroup tbpricegroup = new Tbpricegroup();
        BeanUtils.copyProperties(entiry, tbpricegroup);
        tbpricegroupServiceI.updatePriceGroup(tbpricegroup);
        return Json.success();
    }

    /**
     * 删除价格组信息
     * 方法功能:  删除价格组信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/10 14:01
     */
    @PostMapping("/deletePriceGroup")
    public Json deletePriceGroup(@NotBlank(message = "价格组id不能为空") String ids) {
        tbpricegroupServiceI.deletePriceGroup(ids);
        return Json.success();
    }

    /**
     * 获取价格组最大isort序号
     * 方法功能:  获取价格组最大isort序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author suny
     * @date 2023/11/10 14:14
     */
    @GetMapping("/getPriceGroupMaxSort")
    public Json<Integer> getPriceGroupMaxSort() {
        Integer maxSort = tbpricegroupServiceI.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 获取价格组列表
     * 方法功能: 获取价格组列表(如果没有找到就用默认的价格组)
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpricegroup>>
     * @author songly
     * @date 2023/12/5 17:11
     */
    @GetMapping("/getPriceGroupList")
    public Json<List<Tbpricegroup>> getPriceGroupList(PriceGroupVO query) throws Exception {
        List<Tbpricegroup> sysPriceGroupList = tbpricegroupServiceI.getPriceGroupList(query);
        return Json.success(sysPriceGroupList);
    }
}
