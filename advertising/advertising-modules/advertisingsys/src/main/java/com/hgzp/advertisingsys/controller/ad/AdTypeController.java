package com.hgzp.advertisingsys.controller.ad;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdTypeVO;
import com.hgzp.advertisingsys.service.ad.TbadtypeServiceI;
import com.hgzp.core.model.Tbadtype;
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
 * AdTypeController
 * 创建人：yanz
 * 类描述：广告类型 Controller
 * 创建日期：2023-08-16 15:51
 *
 * @folder ad/AdTypeController
 */
@Validated
@RestController
@RequestMapping("/ad/adtype")
public class AdTypeController extends BaseController {
    @Autowired
    private TbadtypeServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    /**
     * 据id获取广告类型
     * 方法功能:据id获取广告类型
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbadtype>
     * @author yanz
     * @date 2023/8/17 15:29
     */
    @GetMapping(value = "/getAdTypeById")
    public Json<Tbadtype> getAdTypeById(@NotNull(message = "ID不可为空") String id) {
        Tbadtype adtype = serviceI.getById(id);
        return ObjectUtil.isNotNull(adtype) ? Json.success(adtype) : Json.fail();
    }

    /**
     * 获取广告类型
     * 方法功能:分页：获取广告类型
     * @author yanz
     * @date 2023/8/31 15:12
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbadtype>>
     */
    @GetMapping(value = "/getAdTypePageList")
    public Json<List<Tbadtype>> getAdTypePageList(PageRequest pageRequest, AdTypeVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbadtype> pages = serviceI.getAdTypePageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 修改广告类型
     * 方法功能:修改广告类型
     *
     * @param newadtype
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/17 15:52
     */
    @PostMapping(value = "/updateAdType")
    public Json updateAdType(@RequestBody Tbadtype newadtype) {
        Json jsonRet = serviceI.doDefaultLogic(newadtype);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        if (serviceI.isDuplicateSname(newadtype.getId(), newadtype.getSname())) {
            return Json.fail("存在同名类型");
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(newadtype) ? Json.success() : Json.fail();
    }

    /**
     * 据id删除广告类型
     * 方法功能:据id删除广告类型
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deleteAdTypeById")
    public Json deleteAdTypeById(@NotNull(message = "ID不可为空") String ids) {
        if (serviceI.deleteAdTypeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加新广告类型
     * 方法功能:添加新广告类型
     *
     * @param adtype
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbadtype>
     * @author yanz
     * @date 2023/8/17 15:53
     */
    @PostMapping(value = "/saveAdType")
    public Json saveAdType(@RequestBody Tbadtype adtype) {
        if (serviceI.isDuplicateSname(adtype.getId(), adtype.getSname())) {
            return Json.fail("存在同名类型");
        }
        Json jsonRet = serviceI.doDefaultLogic(adtype);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        return serviceI.save(adtype) ? Json.success() : Json.fail();
    }

    /**
     * 获取广告类型最大序号
     * 方法功能:获取广告类型最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:19
     */
    @GetMapping("/getAdTypeMaxSort")
    public Json<Integer> getAdTypeMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
