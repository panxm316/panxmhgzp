package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.service.system.TbmenuactionscopeServiceI;
import com.hgzp.advertisingsys.service.system.TbscopeServiceI;
import com.hgzp.core.model.Tbmenuactionscope;
import com.hgzp.core.model.Tbscope;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * ScopeController
 * 创建人：wangwk
 * 类描述：菜单范围Controller
 * 创建日期：2023/8/30 10:36
 *
 * @folder system/ScopeController
 */
@Validated
@RestController
@RequestMapping("/system/scope")
public class ScopeController extends BaseController {

    @Autowired
    TbscopeServiceI tbscopeService;
    @Autowired
    TbmenuactionscopeServiceI tbmenuactionscopeService;

    /**
     * 查询范围列表
     * 方法功能:  查询范围列表
     * @author wangwk
     * @date 2023/8/30 10:50
     * @param ph  分页参数
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbscope>>
     */
    @GetMapping("/getScopePageList")
    public Json<List<Tbscope>> getScopePageList(PageRequest ph){
        Page page = getPage(ph);
        IPage<Tbscope> result = tbscopeService.page(page);
        return Json.success(result);
    }


    /**
     * 获取范围列表
     * 方法功能: 获取范围列表
     * @author wangwk
     * @date 2023/8/30 11:06
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbscope>>
     */
    @GetMapping("/getScopeList")
    public Json<List<Tbscope>> getScopeList(){
        List<Tbscope> tbscopeList = tbscopeService.lambdaQuery()
                .eq(Tbscope::getBuse, true)
                .list();
        return Json.success(tbscopeList);
    }




    /**
     * 保存范围
     * 方法功能:  保存范围
     * @author wangwk
     * @date 2023/8/30 10:54
     * @param tbscope
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveScope")
    public Json saveScope(@RequestBody Tbscope tbscope){
        tbscopeService.save(tbscope);
        return Json.success();
    }


    /**
     * 更新范围
     * 方法功能: 更新范围
     * @author wangwk
     * @date 2023/8/30 10:55
     * @param tbscope
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateScope")
    public Json updateScope(@RequestBody Tbscope tbscope){
        tbscopeService.updateById(tbscope);
        return Json.success();
    }

    /**
     * 删除范围
     * 方法功能: 删除范围
     * @author wangwk
     * @date 2023/8/30 11:01
     * @param ids  id，多个用逗号隔开
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteScope")
    public Json deleteScope(@NotBlank(message = "ID不可为空") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        Long count = tbmenuactionscopeService.lambdaQuery().in(Tbmenuactionscope::getScopeid, idList).count();
        if(count > 0){
            return Json.fail("范围已经使用不能删除");
        }
        tbscopeService.removeBatchByIds(idList);
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能: 获取最大序号
     * @author peij
     * @date 2023/10/28 10:22
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort(){
        Integer maxSort = tbscopeService.getMaxSort();
        return Json.success(maxSort);
    }


}
