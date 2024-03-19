package com.hgzp.advertisingsys.controller.finance;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.finance.TbbusinessentityServiceI;
import com.hgzp.core.model.Tbbusinessentity;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * BusinessentityController
 * 类全名：com.hgzp.advertising.controller.finance.BusinessentityController
 * 创建人：CGD
 * 类描述：经营主体设置表操作
 * 创建日期：2023/8/16 15:33
 * @folder finance/BusinessentityController
 */
@Validated
@RequestMapping(value = "/finance/businessentity")
@RestController
public class BusinessentityController extends BaseController {

    @Autowired
    private TbbusinessentityServiceI tbbusinessentityService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 分页查询经营主体
     * 方法功能:   分页查询经营主体
     *
     * @param pageRequeste
     * @param queryKey
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/17 10:58
     */
    @GetMapping(value = "/getBusinessentityPageList")
    public Json<List<Tbbusinessentity>> getBusinessentityPageList(PageRequest pageRequeste, String queryKey) {
        Page page = getPage(pageRequeste);
        IPage<Tbbusinessentity> tbbusinessentities = tbbusinessentityService.getBusinessentityPageList(page, queryKey);
        return Json.success(tbbusinessentities);
    }

    /**
     * 新增经营主体
     * 方法功能:  新增经营主体
     *
     * @param businessentity
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/16 17:00
     */
    @PostMapping(value = "/saveBusinessentity")
    public Json saveBusinessentity(@RequestBody Tbbusinessentity businessentity) {
        if(tbbusinessentityService.isExistBusinessentity(businessentity)){
            return Json.fail("已存在同名经营主体！");
        }
        Json jsonRet=tbbusinessentityService.doDefaultLogic(businessentity);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        tbbusinessentityService.save(businessentity);
        return Json.success();
    }

    /**
     * 根据id修改经营主体
     * 方法功能:  根据id修改经营主体
     *
     * @param businessentity
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/16 17:00
     */
    @PostMapping(value = "/updateBusinessentity")
    public Json updateBusinessentity(@RequestBody Tbbusinessentity businessentity) {
        if(tbbusinessentityService.isExistBusinessentity(businessentity)){
            return Json.fail("已存在同名经营主体");
        }
        Json jsonRet=tbbusinessentityService.doDefaultLogic(businessentity);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        tbbusinessentityService.updateById(businessentity);
        return Json.success();
    }

    /**
     * 根据id查询经营主体信息
     * 方法功能: 根据id查询经营主体信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbbusinessentity>
     * @author CGD
     * @date 2023/8/18 10:49
     */
    @GetMapping(value = "/getBusinessentityById")
    public Json<Tbbusinessentity> getBusinessentityById(@NotNull(message = "请传入需要查询的id") Long id) {
        Tbbusinessentity byId = tbbusinessentityService.getById(id);
        return Json.success(byId);
    }

    /**
     * 根据id删除经营主体信息
     * 方法功能: 根据id删除经营主体信息 多选支持","分隔
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/16 17:00
     */
    @PostMapping(value = "/deleteBusinessentity")
    public Json deleteBusinessentity(@NotBlank(message = "请传入需要删除的id") String ids) {
//        innerInterceptor.recoredLog();
//        tbbusinessentityService.removeByIds(CollUtil.newArrayList(ids.split(",")));
//        return Json.success();
        try {
            tbbusinessentityService.deleteBusinessentity(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取付款方式序号最大值
     * 方法功能: 获取付款方式序号最大值
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 14:43
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = tbbusinessentityService.getMaxSort();
        return Json.success(maxSort);

    }
}
