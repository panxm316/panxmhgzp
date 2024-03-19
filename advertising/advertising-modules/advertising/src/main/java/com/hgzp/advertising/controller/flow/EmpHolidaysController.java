package com.hgzp.advertising.controller.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.flow.dto.EmpHolidayDTO;
import com.hgzp.advertising.service.flow.TwempholidaysServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twempholidays;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * EmpHolidaysController
 * 创建人：suny
 * 类描述：人员假期controller
 * 创建日期：2023/10/23 15:57
 *
 * @测试：
 * @folder flow/EmpHolidaysController
 */
@RestController
@RequestMapping("/flow/empholiday")
@Validated
public class EmpHolidaysController extends BaseController {
    @Autowired
    TwempholidaysServiceI twempholidaysServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取人员休假列表
     * 方法功能: 获取人员休假列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twempholidays>>
     * @author suny
     * @date 2023/10/23 17:03
     */
    @GetMapping("/getHolidayPageList")
    public Json<List<Twempholidays>> getHolidayPageList(PageRequest pageRequest, Twempholidays query) throws Exception {
        Page<Twempholidays> page = getPage(pageRequest);
        IPage<Twempholidays> pageList = twempholidaysServiceI.getHolidayPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 保存人员休假信息
     * 方法功能: 保存人员休假信息
     *
     * @param twempholidays
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/23 17:04
     */
    @PostMapping(value = "/saveEmpHoliday")
    public Json saveEmpHoliday(@Validated(value = {ValidateParam.add.class}) @RequestBody EmpHolidayDTO twempholidays) throws Exception {
        twempholidaysServiceI.saveEmpHolidays(twempholidays);
        return Json.success();
    }

    /**
     * 更新人员假期信息
     * 方法功能: 更新人员假期信息
     *
     * @param twempholidays
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/26 11:21
     */
    @PostMapping(value = "/updateEmpHoliday")
    public Json updateEmpHoliday(@Validated(value = {ValidateParam.edit.class}) @RequestBody EmpHolidayDTO twempholidays) throws Exception {
        twempholidaysServiceI.updateEmpHolidays(twempholidays);
        return Json.success();
    }

    /**
     * 删除人员假期信息
     * 方法功能: 删除人员假期信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/25 10:29
     */
    @PostMapping(value = "/deleteEmpHolidayById")
    public Json deleteEmpHolidayById(@NotNull(message = "ID不可为空") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        twempholidaysServiceI.removeByIds(idList);
        return Json.success();
    }
}
