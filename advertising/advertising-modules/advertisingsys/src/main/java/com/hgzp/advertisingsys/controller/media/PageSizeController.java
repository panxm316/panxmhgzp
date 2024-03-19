package com.hgzp.advertisingsys.controller.media;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.vo.PageSizeVO;
import com.hgzp.advertisingsys.service.media.TbpagesizeServiceI;
import com.hgzp.core.model.Tbpagesize;
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
 * PageSizeController
 * 创建人：yanz
 * 类描述： 报纸版心 Controller
 * 创建日期：2023/8/17 17:09
 *
 * @folder media/PageSizeController
 */
@Validated
@RestController
@RequestMapping(value = "/media/pagesize")
public class PageSizeController extends BaseController {
    @Autowired
    private TbpagesizeServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取报纸版心
     * 方法功能:据id获取报纸版心
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbpagesize>
     * @author yanz
     * @date 2023/8/18 9:12
     */
    @GetMapping(value = "/getPageSizeById")
    public Json<Tbpagesize> getPageSizeById(@NotNull(message = "ID不可为空") String id) {
        Tbpagesize PageSize = serviceI.getById(id);
        return ObjectUtil.isNotNull(PageSize) ? Json.success(PageSize) : Json.fail();
    }

    /**
     * 获取报纸版心
     * 方法功能:分页：获取报纸版心
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpagesize>>
     * @author yanz
     * @date 2023/8/31 15:04
     */
    @GetMapping(value = "/getPageSizePageList")
    public Json<List<Tbpagesize>> getPageSizePageList(PageRequest pageRequest, PageSizeVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbpagesize> pages = serviceI.getPageSizePageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 修改报纸版心
     * 方法功能:修改报纸版心
     *
     * @param newPageSize
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 9:21
     */
    @PostMapping(value = "/updatePageSize")
    public Json updatePageSize(@RequestBody Tbpagesize newPageSize) {
        Json jsonRet = serviceI.doDefaultLogic(newPageSize);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        if (serviceI.isDuplicateSname(newPageSize.getId(), newPageSize.getSname())) {
            return Json.fail("存在同名报纸版心");
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(newPageSize) ? Json.success() : Json.fail();
    }

    /**
     * 据id删除报纸版心
     * 方法功能:据id删除报纸版心
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deletePageSizeById")
    public Json deletePageSizeById(@NotNull(message = "ID不可为空") String ids) {
//        innerInterceptor.recoredLog();
//        if (serviceI.removeByIds(Arrays.asList(ids.split(",")))) {
//            return Json.success();
//        } else {
//            return Json.fail("传入ID异常，删除失败");
//        }
        try {
            serviceI.deletePageSizeByIds(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return  Json.fail(e.getMessage());
        }
    }

    /**
     * 添加报纸版心
     * 方法功能:添加报纸版心
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 9:22
     */
    @PostMapping(value = "/savePageSize")
    public Json savePageSize(@RequestBody Tbpagesize tbpageSize) {
        Json jsonRet = serviceI.doDefaultLogic(tbpageSize);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        if (serviceI.isDuplicateSname(tbpageSize.getId(), tbpageSize.getSname())) {
            return Json.fail("存在同名报纸版心");
        }
        innerInterceptor.recoredLog();
        return serviceI.save(tbpageSize) ? Json.success() : Json.fail();
    }

    /**
     * 获取报纸版心最大序号
     * 方法功能:获取报纸版心最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:25
     */
    @GetMapping("/getPageSizeMaxSort")
    public Json<Integer> getPageSizeMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
