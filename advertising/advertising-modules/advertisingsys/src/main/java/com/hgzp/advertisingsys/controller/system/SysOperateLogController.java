package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.service.system.TwsysoperatelogServiceI;
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
 * SysOperateLogController
 * 创建人：CGD
 * 类描述：系统管理操作日志
 * 创建日期：2023/9/12 15:37
 *
 * @folder system/SysOperateLogController
 */
@RequestMapping(value = "/system/sysoperatelog")
@RestController
public class SysOperateLogController extends BaseController {
    @Autowired
    private TwsysoperatelogServiceI twsysoperatelogService;

    /**
     * 系统管理操作日志分页查询
     * 方法功能:  系统管理操作日志分页查询      
     * @author CGD
     * @date 2023/9/12 16:05
     * @param pageRequeste
     * @param query
     * @param slogtype
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Twsysoperatelog>>
     */
    @GetMapping(value = "/getSysoperatelogPageList")
    public Json<List<Twsysoperatelog>> getSysoperatelogPageList(PageRequest pageRequeste, BaseQueryInfo query, String slogtype){
        Page page = getPage(pageRequeste);
        Page<Twsysoperatelog> sysoperatelogPageList = twsysoperatelogService.getSysoperatelogPageList(page, query, slogtype);
        return Json.success(sysoperatelogPageList);
    }
}

