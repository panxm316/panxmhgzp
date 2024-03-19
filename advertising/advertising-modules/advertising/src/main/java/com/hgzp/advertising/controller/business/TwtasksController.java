package com.hgzp.advertising.controller.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaAddDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaUpdateDTO;
import com.hgzp.advertising.pagemodel.business.dto.TwtaskDTO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.service.business.TwtasksServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import java.util.List;

/**
 * <p>
 * 任务额度表 前端控制器
 * </p>
 *
 * @author wwk
 * @since 2023-10-31
 * @modify lhl
 * @modify-date 2023-11-08
 */
@RestController
@RequestMapping("/business/twtasks")
public class TwtasksController extends BaseController {

    @Autowired
    TwtasksServiceI twtasksServiceI;

    /**
     * 查询任务额度记录
     * 方法功能: 返回符合查询条件的任务额度记录列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO>>
     * @author lhl
     * @date 2023/11/09
     */
    @GetMapping("/getTaskQuotaPageList")
    public Json<List<TaskQuotaDTO>> getTaskQuotaPageList(PageRequest pageRequest, TaskReportsVO query) throws Exception {
        Page<Twtasks> page = getPage(pageRequest);
        IPage<TaskQuotaDTO> pageList = twtasksServiceI.getTaskReportPageList(page, query);
        return Json.success(pageList);
    }


    /**
     * 批量添加任务额度记录
     * 方法功能: 依据年度期间批量添加任务额度记录
     *
     * @param taskQuotaAddDTO
     * @author lhl
     * @date 2023/11/10
     */

    @PostMapping(value = "/saveTaskQuota")
    public Json saveTaskQuota(@Validated(value = {ValidateParam.add.class}) @RequestBody TaskQuotaAddDTO taskQuotaAddDTO) throws Exception {
        return twtasksServiceI.saveTaskQuota(taskQuotaAddDTO);
    }

    /**
     * 批量更新任务额度记录
     * 方法功能: 批量更新匹配ID的配额记录，主要更新任务配额
     *
     * @param taskQuotaUpdateDTO
     * @author lhl
     * @date 2023/11/11
     */

    @PostMapping(value = "/updateTaskQuota")
    public Json updateTaskQuota(@Validated(value = {ValidateParam.add.class}) @RequestBody TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {

        return twtasksServiceI.updateTaskQuota(taskQuotaUpdateDTO);
    }

    /**
     * 批量删除任务额度记录
     * 方法功能: 批量删除匹配ID的配额记录
     *
     * @param taskQuotaUpdateDTO
     * @author lhl
     * @date 2023/11/11
     */

    @PostMapping(value = "/deleteTaskQuota")
    public Json deleteTaskQuota(@Validated(value = {ValidateParam.add.class}) @RequestBody TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {

        return twtasksServiceI.deleteTaskQuota(taskQuotaUpdateDTO);
    }

    /**
     * 批量复制额度记录
     * 方法功能: 批量复制额度记录到指定的年度
     *
     * @param taskQuotaUpdateDTO
     * @author lhl
     * @date 2023/11/24
     */

    @PostMapping(value = "/batchCopyTaskQuota")
    public Json batchCopyTaskQuota(@Validated(value = {ValidateParam.add.class}) @RequestBody TaskQuotaUpdateDTO taskQuotaUpdateDTO) throws Exception {

        return twtasksServiceI.batchCopyTaskQuota(taskQuotaUpdateDTO);
    }

    /**
     * 获取额度记录
     * 方法功能: 获取额度记录
     *
     * @param taskid
     * @author lhl
     * @date 2023/12/29
     */

    @GetMapping(value = "/getTwtaskBody")
    public Json<TwtaskDTO> getTwtaskBody(String taskid) throws Exception {
        TwtaskDTO twtaskDTO = twtasksServiceI.getTwtasksById(taskid);
        return Json.success(twtaskDTO);
    }

    /**
     * 添加任务额度记录
     * 方法功能: 添加任务额度记录
     *
     * @param twtaskDTO
     * @author lhl
     * @date 2023/12/29
     */
    @PostMapping(value = "/addTwtask")
    public Json addTwtask(@RequestBody TwtaskDTO twtaskDTO) throws Exception {
        return twtasksServiceI.addTaskQuota(twtaskDTO);
    }

    /**
     * 调整任务额度记录
     * 方法功能: 调整任务额度记录
     *
     * @param twtaskDTO
     * @author lhl
     * @date 2023/12/29
     */
    @PostMapping(value = "/updateTwtask")
    public Json updateTwtask(@RequestBody TwtaskDTO twtaskDTO) throws Exception {
        return twtasksServiceI.updateTwtask(twtaskDTO);
    }

}
