package com.hgzp.advertisingsys.controller.ad;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.ad.vo.WeekSettingVO;
import com.hgzp.advertisingsys.service.ad.TbweeksettingServiceI;
import com.hgzp.core.model.Tbweeksetting;
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
 * WeekSettingController
 * 创建人：yanz
 * 类描述： 星期设置 Controller
 * 创建日期：2023/8/18 11:26
 *
 * @folder ad/WeekSettingController
 */
@Validated
@RestController
@RequestMapping(value = "/ad/weeksetting")
public class WeekSettingController extends BaseController {
    @Autowired
    private TbweeksettingServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取星期设置
     * 方法功能:据id获取星期设置
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbweeksetting>
     * @author yanz
     * @date 2023/8/18 13:51
     */
    @GetMapping(value = "/getWeekSettingById")
    public Json<Tbweeksetting> getWeekSettingById(@NotNull(message = "ID不可为空") String id) {
        Tbweeksetting weeksetting = serviceI.getById(id);
        return ObjectUtil.isNotNull(weeksetting) ? Json.success(weeksetting) : Json.fail();
    }

    /**
     * 获取星期设置
     * 方法功能:分页：获取星期设置
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbweeksetting>>
     * @author yanz
     * @date 2023/8/25 11:21
     */
    @GetMapping(value = "/getWeekSettingPageList")
    public Json<List<Tbweeksetting>> getWeekSettingPageList(PageRequest pageRequest, WeekSettingVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbweeksetting> pages = serviceI.getWeekSettingPageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 修改星期设置
     * 方法功能:修改星期设置
     *
     * @param newweeksetting
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 13:51
     */
    @PostMapping(value = "/updateWeekSetting")
    public Json updateWeekSetting(@RequestBody Tbweeksetting newweeksetting) {
        if(serviceI.isDuplicateSname(newweeksetting.getId(), newweeksetting.getSname())){
            return Json.fail("星期名称不能重复！");
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(newweeksetting) ? Json.success() : Json.fail();
    }

    /**
     * 根据id删除星期设置
     * 方法功能:根据id删除星期设置
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deleteWeekSettingById")
    public Json deleteWeekSettingById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (serviceI.removeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加新星期设置
     * 方法功能:添加新星期设置
     *
     * @param weekSetting
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 13:52
     */
    @PostMapping(value = "/saveWeekSetting")
    public Json saveWeekSetting(@RequestBody Tbweeksetting weekSetting) {
        if(serviceI.isDuplicateSname(weekSetting.getId(), weekSetting.getSname())){
            return Json.fail("星期名称不能重复！");
        }
        innerInterceptor.recoredLog();
        return serviceI.save(weekSetting) ? Json.success() : Json.fail();
    }

    /**
     * 获取星期设置最大序号
     * 方法功能:获取星期设置最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:23
     */
    @GetMapping("/getWeekSettingMaxSort")
    public Json<Integer> getWeekSettingMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
