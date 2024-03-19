package com.hgzp.advertising.controller.system;

import com.hgzp.advertising.service.system.TwparameterServiceI;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ParameterController
 * 创建人：suny
 * 类描述：系统参数表 前端控制器
 * 创建日期：2024/3/8 8:54
 *
 * @folder system/ParameterController
 */
@Validated
@RestController
@RequestMapping("/system/parameter")
public class ParameterController extends BaseController {
    @Autowired
    TwparameterServiceI twparameterServiceI;

    /**
     * 根据key获取系统参数值
     * 方法功能:根据key获取系统参数值
     *
     * @param key
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/8 8:52
     */
    @RequestMapping("/getParameterByKey")
    public Json getParameterByKey(String key) throws Exception {
        String svalue = twparameterServiceI.getParameterByKey(key);
        return Json.success("", svalue);
    }
}

