package com.hgzp.advertisingsys.controller.ad;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.ad.vo.PricePackageTypeVO;
import com.hgzp.advertisingsys.service.ad.TbpricepackagetypeServiceI;
import com.hgzp.core.model.Tbpricepackagetype;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * PricePackageType
 * 创建人：yanz
 * 类描述： 打包类型 Controller
 * 创建日期：2023/8/18 14:02
 *
 * @folder ad/PricePackageType
 */
@Validated
@RestController
@RequestMapping(value = "/ad/pricepackagetype")
public class PricePackageTypeController extends BaseController {

    @Autowired
    private TbpricepackagetypeServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取打包类型
     * 方法功能:据id获取打包类型
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbpricepackagetype>
     * @author yanz
     * @date 2023/8/18 14:12
     */
    @GetMapping(value = "/getPricePackageTypeById")
    public Json<Tbpricepackagetype> getPricePackageTypeById(@NotNull(message = "ID不可为空") String id) {
        Tbpricepackagetype pricepackagetype = serviceI.getById(id);
        return ObjectUtil.isNotNull(pricepackagetype) ? Json.success(pricepackagetype) : Json.fail();
    }

    /**
     * 获取打包类型
     * 方法功能:分页：获取打包类型
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpricepackagetype>>
     * @author yanz
     * @date 2023/8/31 15:11
     */
    @GetMapping(value = "/getPricePackageTypePageList")
    public Json<List<Tbpricepackagetype>> getPricePackageTypePageList(PageRequest pageRequest, PricePackageTypeVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbpricepackagetype> pages = serviceI.getPricePackageTypePageList(page, vo);
        return Json.success(pages);
    }


    /**
     * 修改打包类型
     * 方法功能:修改打包类型
     *
     * @param newpricepackagetype
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 14:23
     */
    @PostMapping(value = "/updatePricePackageType")
    public Json updatePricePackageType(@RequestBody Tbpricepackagetype newpricepackagetype) {
        if (serviceI.isDuplicateSname(newpricepackagetype.getId(), newpricepackagetype.getSname())) {
            return Json.fail("存在同名打包类型");
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(newpricepackagetype) ? Json.success() : Json.fail();
    }

    /**
     * 根据id删除打包类型
     * 方法功能: 根据id删除打包类型
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deletePricePackageTypeById")
    public Json deletePricePackageTypeById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (serviceI.removeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加新打包类型
     * 方法功能:添加新打包类型
     *
     * @param pricepackagetype
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 14:24
     */
    @PostMapping(value = "/savePricePackageType")
    public Json savePricePackageType(@RequestBody Tbpricepackagetype pricepackagetype) {
        if (serviceI.isDuplicateSname(pricepackagetype.getId(), pricepackagetype.getSname())) {
            return Json.fail("存在同名打包类型");
        }
        innerInterceptor.recoredLog();
        return serviceI.save(pricepackagetype) ? Json.success() : Json.fail();
    }

    /**
     * 获取打包类型最大序号
     * 方法功能:获取打包类型最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:21
     */
    @GetMapping("/getPricePackageTypeMaxSort")
    public Json<Integer> getPricePackageTypeMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
