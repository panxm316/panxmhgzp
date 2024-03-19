package com.hgzp.advertising.controller.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.statistic.dto.DatabackupGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.vo.DataBackupDetailVO;
import com.hgzp.advertising.pagemodel.statistic.vo.DatabackupGroupVO;
import com.hgzp.advertising.service.statistic.DatabackupGroupServiceI;
import com.hgzp.core.model.Twdatabackupdetail1;
import com.hgzp.core.model.Twdatabackupgroup;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DatabackupGroupController
 * 创建人：suny
 * 类描述：数据轧账总表 前端控制器
 * 创建日期：2024/1/19 16:09
 *
 * @folder statistic/DatabackupGroupController
 */
@Validated
@RestController
@RequestMapping("/statistic/databackupgroup")
public class DatabackupGroupController extends BaseController {

    @Autowired
    private DatabackupGroupServiceI databackupgroupService;

    /**
     * 获取数据轧账总表分页列表
     * 方法功能:  根据查询条件获取数据轧账总表分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twdatabackupgroup>>
     * @author suny
     * @date 2024/1/19 16:18
     */
    @GetMapping("/getDataBackupGroupPageList")
    public Json<List<Twdatabackupgroup>> getDataBackupGroupPageList(PageRequest pageRequest, DatabackupGroupVO query) throws Exception {
        Page<Twdatabackupgroup> page = getPage(pageRequest);
        IPage<Twdatabackupgroup> pageList = databackupgroupService.getDataBackupGroupPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 获取数据轧账明细分页列表
     * 方法功能: 获取数据轧账明细分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twdatabackupdetail1>>
     * @author suny
     * @date 2024/1/19 16:35
     */
    @GetMapping("/getDataBackupDetailPageList")
    public Json<List<Twdatabackupdetail1>> getDataBackupDetailPageList(PageRequest pageRequest, DataBackupDetailVO query) throws Exception {
        Page<Twdatabackupdetail1> page = getPage(pageRequest);
        IPage<Twdatabackupdetail1> pageList = databackupgroupService.getDataBackupDetailPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 根据时间范围查询定向刊期总数
     * 方法功能:  根据时间范围查询定向刊期总数
     *
     * @param query
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/19 16:40
     */
    @GetMapping("/getOrderItemCount")
    public Json getOrderItemCount(BaseQueryInfo query) throws Exception {
        long pageList = databackupgroupService.getOrderItemCount(query);
        return Json.success(pageList);
    }

    /**
     * 保存数据轧账明细
     * 方法功能: 根据时间范围查询定向刊期数据，写入数据轧账明细表
     *
     * @param entity
     * @return com.hgzp.core.page.Json<java.lang.Long>
     * @author suny
     * @date 2024/1/19 16:47
     */
    @PostMapping("/saveDataBackupDetail")
    public Json saveDataBackupDetail(@RequestBody DatabackupGroupDTO entity) throws Exception {
        databackupgroupService.saveDataBackupDetail(entity);
        return Json.success();
    }
}

