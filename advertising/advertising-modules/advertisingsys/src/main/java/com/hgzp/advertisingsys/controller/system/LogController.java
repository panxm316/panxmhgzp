package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Twlog;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.service.system.BaseTwlogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * LogController
 * 创建人：CGD
 * 类描述：操作日志controller
 * 创建日期：2023/8/21 15:01
 * @folder system/LogController
 */
@RequestMapping(value = "/system/log")
@RestController
public class LogController extends BaseController {

    @Autowired
    private BaseTwlogServiceI baseTwlogService;

    /**
     * 分页查询日志
     * 方法功能: 分页查询日志
     * @author CGD
     * @date 2023/9/6 12:43
     * @param pageRequeste
     * @param query
     * @param slogtype
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Twlog>>
     */
    @GetMapping(value = "/getLogPageList")
    public Json<List<Twlog>> getLogPageList(PageRequest pageRequeste, BaseQueryInfo query, String slogtype) {
        Page page = getPage(pageRequeste);
        Page<Twlog> list = baseTwlogService.getLogPageList(page, query,slogtype);
        return Json.success(list);
    }
}
