package com.hgzp.advertisingsys.controller.finance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.finance.vo.AdPrintItemVO;
import com.hgzp.advertisingsys.service.finance.TbadprintitemServiceI;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * AdPrintItemController
 * 创建人：yanz
 * 类描述： 开票项目 Controller
 * 创建日期：2023/8/18 15:55
 *
 * @folder finance/AdPrintItemController
 */
@Validated
@RestController
@RequestMapping(value = "/finance/adprintitem")
public class AdPrintItemController extends BaseController {
    @Autowired
    private TbadprintitemServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取开票项目
     * 方法功能:据id获取开票项目
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbadprintitem>
     * @author yanz
     * @date 2023/8/18 16:10
     */
    @GetMapping(value = "/getAdPrintItemById")
    public Json<Tbadprintitem> getAdPrintItemById(@NotNull(message = "ID不可为空") String id) {
        Tbadprintitem adprintitem = serviceI.getById(id);
        return ObjectUtil.isNotNull(adprintitem) ? Json.success(adprintitem) : Json.fail();
    }

    /**
     * 获取开票项目
     * 方法功能:分页：获取开票项目
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbadprintitem>>
     * @author yanz
     * @date 2023/8/31 15:06
     */
    @GetMapping(value = "/getAdPrintItemPageList")
    public Json<List<Tbadprintitem>> getAdPrintItemPageList(PageRequest pageRequest, AdPrintItemVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbadprintitem> pages = serviceI.getAdPrintItemPageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 修改开票项目
     * 方法功能:修改开票项目
     *
     * @param newadprintitem
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 16:10
     */
    @PostMapping(value = "/updateAdPrintItem")
    public Json updateAdPrintItem(@RequestBody Tbadprintitem newadprintitem) {
        if(serviceI.isDuplicateSname(newadprintitem)){
            return Json.fail("开票项目名称不能重复!");
        }
        Json jsonRet=serviceI.doDefaultLogic(newadprintitem);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(newadprintitem) ? Json.success() : Json.fail();
    }

    /**
     * 根据id删除开票项目
     * 方法功能:根据id删除开票项目
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:30
     */
    @PostMapping(value = "/deleteAdPrintItemById")
    public Json deleteAdPrintItemById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (serviceI.removeByIds(CollUtil.newArrayList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加新开票项目
     * 方法功能:添加新开票项目
     *
     * @param adprintitem
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 16:11
     */
    @PostMapping(value = "/saveAdPrintItem")
    public Json saveAdPrintItem(@RequestBody Tbadprintitem adprintitem) {
        if(serviceI.isDuplicateSname(adprintitem)){
            return Json.fail("开票项目名称不能重复!");
        }
        Json jsonRet=serviceI.doDefaultLogic(adprintitem);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        return serviceI.save(adprintitem) ? Json.success() : Json.fail();
    }

    /**
     * 获取开票项目最大序号
     * 方法功能:获取开票项目最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:24
     */
    @GetMapping("/getAdPrintItemMaxSort")
    public Json<Integer> getAdPrintItemMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
