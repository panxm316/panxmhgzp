package com.hgzp.advertising.controller.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.business.dto.TaskQuotaDTO;
import com.hgzp.advertising.pagemodel.business.vo.TaskReportsVO;
import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelGroupDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.ReportmodelItemDTO;
import com.hgzp.advertising.pagemodel.statistic.dto.SumObjectDTO;
import com.hgzp.advertising.service.statistic.ReportmodelGroupServiceI;
import com.hgzp.advertising.service.statistic.ReportmodelItemServiceI;
import com.hgzp.core.model.Twtasks;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import static com.hgzp.core.constant.ValidateParam.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 报表模板组设置 前端控制器
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@RestController
@RequestMapping("/statistic/reportmodelgroup")
@Validated
@Component
public class ReportmodelGroupController extends BaseController {

    @Autowired
    private ReportmodelGroupServiceI  reportmodelgroupService;

    @Autowired
    private ReportmodelItemServiceI  reportmodelItemServiceI;

    /**
     * 新增Tbreportmodelgroup
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/save")
    public Json save(@RequestBody @Validated(add.class) ReportmodelGroupDTO reportmodelgroupDTO) {
        reportmodelgroupService.save(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 删除Tbreportmodelgroup
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/delete")
    public Json delete(@RequestBody @Validated(delete.class) ReportmodelGroupDTO reportmodelgroupDTO) {
        reportmodelgroupService.delete(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 修改Tbreportmodelgroup
     *
     * @author muyn
     * @since 2024-01-16
     * @param reportmodelgroupDTO 报表模板组设置数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/update")
    public Json update(@RequestBody @Validated(edit.class) ReportmodelGroupDTO reportmodelgroupDTO) {
        reportmodelgroupService.update(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 查询报表配置记录
     * 方法功能: 查询报表配置记录
     *
     * @param reportFormId
     * @return JSON
     * @author lhl
     * @date 2024/1/18
     */
    @GetMapping("/getReportFormList")
    public Json getReportFormList(String reportFormId)  {
        return Json.success(reportmodelgroupService.getReportFormList(reportFormId));
    }

    /**
     * 启用报表配置记录
     * 方法功能: 启用报表配置记录
     *
     * @param reportId
     * @return JSON
     * @author lhl
     * @date 2024/1/19
     */
    @GetMapping("/enableReportForm")
    public Json enableReportForm(String reportId,String reportFormId) {
        reportmodelgroupService.enableReportForm(reportId,reportFormId);
        return Json.success();
    }

    /**
     * 新增报表配置明细记录
     *
     * @author lhl
     * @since 2024-01-19
     * @param reportmodelgroupDTO
     * @return {@link Json}
     */
    @PostMapping("/saveReportmodelItem")
    public Json saveReportmodelItem(@RequestBody @Validated(add.class) ReportmodelItemDTO reportmodelgroupDTO) {
        reportmodelItemServiceI.save(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 删除报表汇总项
     *
     * @author lhl
     * @since 2024-01-19
     * @param reportmodelgroupDTO
     * @return {@link Json}
     */
    @PostMapping("/deleteReportmodelItem")
    public Json deleteReportmodelItem(@RequestBody @Validated(delete.class) ReportmodelItemDTO reportmodelgroupDTO) {
        reportmodelItemServiceI.delete(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 更新报表配置明细记录
     *
     * @author lhl
     * @since 2024-01-19
     * @param reportmodelgroupDTO
     * @return {@link Json}
     */
    @PostMapping("/updateReportmodelItem")
    public Json updateReportmodelItem(@RequestBody @Validated(edit.class) ReportmodelItemDTO reportmodelgroupDTO) {
        reportmodelItemServiceI.update(reportmodelgroupDTO);
        return Json.success();
    }

    /**
     * 查询报表汇总项记录
     * 方法功能: 查询报表汇总项记录
     *
     * @param reportGroupId
     * @return JSON
     * @author lhl
     * @date 2024/1/19
     */
    @GetMapping("/getReportSumItemList")
    public Json getReportSumItemList(String reportGroupId,String reportType)  {
        return Json.success(reportmodelItemServiceI.getReportSumItemList(reportGroupId,reportType));
    }

    /**
     * 配置部门/区域/业务
     * 方法功能: 配置部门/区域/业务
     *
     * @param sumObjectDTO
     * @return JSON
     * @author lhl
     * @date 2024/1/20
     */
    @PostMapping("/addSumObject")
    public Json addSumObject(@RequestBody @Validated(add.class)SumObjectDTO sumObjectDTO)  {
        reportmodelItemServiceI.addSumObject(sumObjectDTO);
        return Json.success();
    }

}
