package com.hgzp.advertising.controller.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO;
import com.hgzp.advertising.pagemodel.business.vo.CustomerWorkReportsVO;
import com.hgzp.advertising.pagemodel.business.vo.WorkReportsVO;
import com.hgzp.advertising.service.business.TwworkreportsServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twworkreports;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TwworkreportsController
 * 创建人：suny
 * 类描述：工作报告相关接口
 * 创建日期：2023/10/28 15:53
 *
 * @测试：
 * @folder business/TwworkreportsController
 */
@RestController
@RequestMapping("/business/twworkreports")
public class TwworkreportsController extends BaseController {
    @Value("${ufile.uExtURL}")
    private String uExtURL;
    @Autowired
    TwworkreportsServiceI twworkreportsServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取工作报告分页列表
     * 方法功能: 获取工作报告分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twworkreports>>
     * @author suny
     * @date 2023/10/30 9:47
     */
    @GetMapping("/getWorkReportPageList")
    public Json<List<WorkReportsDTO>> getWorkReportPageList(PageRequest pageRequest, WorkReportsVO query) throws Exception {
        Page<Twworkreports> page = getPage(pageRequest);
        IPage<WorkReportsDTO> pageList = twworkreportsServiceI.getWorkReportPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 新增保存工作报告
     * 方法功能: 新增保存工作报告
     *
     * @param workReports
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/11 15:29
     */
    @PostMapping(value = "/saveWorkReport")
    public Json saveWorkReport(@Validated(value = {ValidateParam.add.class}) @RequestBody WorkReportsDTO workReports) throws Exception {
        twworkreportsServiceI.saveWorkReport(workReports);
        return Json.success();
    }

    /**
     * 修改保存工作报告
     * 方法功能: 修改保存工作报告
     *
     * @param workReports
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/30 9:48
     */
    @PostMapping(value = "/updateWorkReport")
    public Json updateWorkReport(@Validated(value = {ValidateParam.edit.class}) @RequestBody WorkReportsDTO workReports) throws Exception {
        twworkreportsServiceI.updateWorkReport(workReports);
        return Json.success();
    }

    /**
     * 保存审核意见
     * 方法功能:  保存审核意见
     *
     * @param workReports
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/2 10:10
     */
    @PostMapping(value = "/saveCheckInfo")
    public Json saveCheckInfo(@Validated(value = {ValidateParam.edit.class}) @RequestBody WorkReportsDTO workReports) throws Exception {
        if (!StringUtils.hasText(workReports.getScheckopinions())) {
            return Json.fail("审核意见不可为空");
        }
        twworkreportsServiceI.saveCheckInfo(workReports);
        return Json.success();
    }

    /**
     * 删除工作报告
     * 方法功能: 删除工作报告
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/2 14:40
     */
    @PostMapping(value = "/deleteWorkReportById")
    public Json deleteWorkReportById(@NotNull(message = "ID不可为空") String ids) throws Exception {
        twworkreportsServiceI.deleteWorkReport(ids);
        return Json.success();
    }

    /**
     * 工作报告提交审核
     * 方法功能: 工作报告提交审核
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/1 14:53
     */
    @PostMapping(value = "/updateCheck")
    public Json updateCheck(@NotNull(message = "ID不可为空") String ids) throws Exception {
        twworkreportsServiceI.updateCheck(ids);
        return Json.success();
    }

    /**
     * 获取客户的工作报告分页列表
     * 方法功能:
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.WorkReportsDTO>>
     * @author songly
     * @date 2024/3/7 12:35
     */
    @GetMapping("/getCustomerWorkReportPageList")
    public Json<List<WorkReportsDTO>> getCustomerWorkReportPageList(PageRequest pageRequest,
                                                                    CustomerWorkReportsVO query) throws Exception {
        Page<Twworkreports> page = getPage(pageRequest);
        IPage<WorkReportsDTO> pageList = twworkreportsServiceI.getCustomerWorkReportPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 根据id获取工作报告
     * 方法功能: 根据id获取工作报告
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twworkreports>
     * @author suny
     * @date 2024/3/11 17:57
     */
    @GetMapping(value = "/getWorkReportById")
    public Json<WorkReportsDTO> getWorkReportById(@NotNull(message = "请传入需要查询的id") String id) throws Exception {
        WorkReportsDTO twworkreports = twworkreportsServiceI.getWorkReportById(id);
        return Json.success(twworkreports);
    }
}
