package com.hgzp.advertising.controller.price;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.PriceItemDTO;
import com.hgzp.advertising.pagemodel.price.vo.PriceItemVO;
import com.hgzp.advertising.service.price.TbpriceitemServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbpriceitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * PriceItemController
 * 创建人：CGD
 * 类描述：价格表明细设置
 * 创建日期：2023-11-10 11:02:52
 *
 * @folder price/PriceItemController
 */
@RestController
@RequestMapping("/price/priceitem")
public class PriceItemController extends BaseController {
    @Autowired
    TbpriceitemServiceI tbpriceitemService;


    /**
     * 根据查询条件获价格表明细分页列表
     * 方法功能:根据查询条件获价格表明细分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpricegroup>>
     * @date 2023/11/11 10:06
     */
    @GetMapping("/getPriceItemPageList")
    public Json<List<PriceItemVO>> getPriceItemPageList(PageRequest pageRequest, PriceItemVO query) {
        Page<Tbpriceitem> page = getPage(pageRequest);
        Page<PriceItemVO> priceItemPageList = tbpriceitemService.getPriceItemPageList(page, query);
        return Json.success(priceItemPageList);
    }

    /**
     * 新增保存价格明细
     * 方法功能:新增保存价格明细
     *
     * @param priceItemDTO
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.price.dto.PriceItemDTO>
     * @author suny
     * @date 2023/11/24 13:46
     */
    @PostMapping("/savePriceItem")
    public Json<PriceItemDTO> savePriceItem(@Validated(value = {ValidateParam.add.class}) @RequestBody PriceItemDTO priceItemDTO) {
        tbpriceitemService.savePriceItem(priceItemDTO);
        return Json.success(priceItemDTO);
    }

    /**
     * 修改价格明细
     * 方法功能:修改价格明细
     *
     * @param priceItemDTO
     * @return void
     * @author CGD
     * @date 2023/11/13 11:23
     */
    @PostMapping("/updatePriceItem")
    public Json updatePriceItem(@Validated(value = {ValidateParam.edit.class}) @RequestBody PriceItemDTO priceItemDTO) {
        tbpriceitemService.updatePriceItem(priceItemDTO);
        return Json.success();
    }

    /**
     * 删除价格明细
     * 方法功能:  删除价格明细
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/11/16 13:53
     */
    @PostMapping("/deletePriceItem")
    public Json deletePriceItem(@NotBlank(message = "价格明细id不能为空") String ids) {
        tbpriceitemService.deletePriceItem(ids);
        return Json.success();
    }

    /**
     * 保存批量修改结束日期
     * 方法功能: 保存批量修改结束日期
     *
     * @param ids
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/24 13:47
     */
    @PostMapping("/batchUpdatePriceItem")
    public Json batchUpdatePriceItem(@NotBlank(message = "价格明细id不能为空") String ids, BaseQueryInfo queryInfo) {
        tbpriceitemService.batchUpdatePriceItem(ids, queryInfo);
        return Json.success();
    }

    /**
     * 获取最大排序
     * 方法功能:获取最大排序
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author CGD
     * @date 2023/11/13 12:33
     */
    @GetMapping("/getPriceItemMaxSort")
    public Json<Integer> getPriceItemMaxSort() {
        Integer maxSort = tbpriceitemService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 获取有效的价格明细列表
     * 方法功能: 获取有效的价格明细列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpriceitem>>
     * @author songly
     * @date 2023/12/6 9:18
     */
    @GetMapping("/getPriceItemList")
    public Json<List<Tbpriceitem>> getPriceItemList(PriceItemVO query) {
        List<Tbpriceitem> priceItemPageList = tbpriceitemService.getPriceItemList(query);
        return Json.success(priceItemPageList);
    }
}
