package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ResourceTypeController
 * 创建人：CGD
 * 类描述：资源文件类型controller
 * 创建日期：2023/10/27 9:52
 *
 * @folder system/ResourceTypeController
 */
@RestController
@RequestMapping("/system/resourcetype")
@Validated
public class ResourceTypeController  extends BaseController {

    @Autowired
    private TbresourcetypeServiceI tbresourcetypeService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    /**
     * 获取资源文件类型分页列表
     * 方法功能: 获取资源文件类型分页列表
     * @param pageRequest
     * @param baseQueryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbresourcetype>>
     * @date 2023/10/27 9:37
     */
    @GetMapping("/getResourcetypePageList")
    public Json<List<Tbresourcetype>> getResourcetypePageList(PageRequest pageRequest, BaseQueryInfo baseQueryInfo) {
        Page<Tbresourcetype> page = getPage(pageRequest);
        IPage<Tbresourcetype> resourcetypeList = tbresourcetypeService.getResourcetypePageList(page, baseQueryInfo);
        return Json.success(resourcetypeList);
    }

    /**
     * 获取资源类型列表
     * 方法功能: 获取资源类型列表
     *
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbresourcetype>>
     * @date 2023/11/1 10:09
     */
    @GetMapping("/getResourceTypeFormatList")
    public Json<List<Tbresourcetype>> getResourceTypeFormatList() {
        List<Tbresourcetype> list = tbresourcetypeService.list();
        return Json.success(list);
    }
    /**
     * 新增资源文件类型
     * 方法功能: 新增资源文件类型
     * @param resourcetype
     * @return com.hgzp.core.page.Json
     * @date 2023/10/27 9:37
     */
    @PostMapping("/saveResourcetype")
    public Json saveResourcetype(@RequestBody Tbresourcetype resourcetype){
        tbresourcetypeService.save(resourcetype);
        return Json.success();
    }
    /**
     * 修改资源文件类型
     * 方法功能: 修改资源文件类型
     * @param resourcetype
     * @return void
     * @date 2023/10/27 9:37
     */
    @PostMapping("/updateResourceType")
    public Json updateResourceType(@RequestBody Tbresourcetype resourcetype){
        innerInterceptor.recoredLog();
        tbresourcetypeService.updateById(resourcetype);
        return Json.success();
    }

}

