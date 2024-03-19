package com.hgzp.advertising.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.service.system.TwoperatelogServiceI;
import com.hgzp.core.model.Twoperatelog;
import com.hgzp.core.model.Twsysoperatelog;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 OperateLogController
 创建人：songly
 类描述：操作日志
 创建日期：2024/2/21 13:31
 @folder system/OperateLogController
 */
@RequestMapping(value = "/system/operatelog")
@RestController
public class OperateLogController extends BaseController {
    @Autowired
    private TwoperatelogServiceI twoperatelogService;

    /**
     * 操作日志分页查询
     * 方法功能:  操作日志分页查询
     * @author songly
     * @date 2024/2/21 13:31
     * @param pageRequeste
     * @param query
     * @param slogtype
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Twoperatelog>>
     */
    @GetMapping(value = "/getoperatelogPageList")
    public Json<List<Twoperatelog>> getOperatelogPageList(PageRequest pageRequeste, BaseQueryInfo query, String slogtype){
        Page page = getPage(pageRequeste);
        Page<Twoperatelog> operatelogPageList = twoperatelogService.getOperatelogPageList(page, query, slogtype);
        return Json.success(operatelogPageList);
    }
}