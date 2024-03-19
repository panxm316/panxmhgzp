package com.hgzp.advertising.controller.system;

import com.hgzp.advertising.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ResourceTypeController
 * 创建人：CGD
 * 类描述：资源文件类型controller
 * 创建日期：2023/11/1 10:07
 *
 * @folder system/ResourceTypeController
 */
@Validated
@RestController
@RequestMapping("/system/resourcetype")
public class ResourceTypeController extends BaseController {
    @Autowired
    TbresourcetypeServiceI tbresourcetypeService;

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
}

