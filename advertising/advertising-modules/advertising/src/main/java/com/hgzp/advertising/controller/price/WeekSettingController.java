package com.hgzp.advertising.controller.price;

import cn.hutool.core.util.ObjectUtil;

import com.hgzp.advertising.service.price.TbweeksettingServiceI;
import com.hgzp.core.model.Tbweeksetting;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * WeekSettingController
 * 创建人：caogd
 * 类描述： 星期设置 Controller
 * 创建日期：2023/11/10 11:26
 *
 * @folder ad/WeekSettingController
 */
@Validated
@RestController
@RequestMapping(value = "/price/weeksetting")
public class WeekSettingController extends BaseController {
    @Autowired
    private TbweeksettingServiceI tbweeksettingService;

    /**
     * 获取树结构星期
     * 方法功能:获取树结构星期用于单选
     * @author CGD
     * @date 2023/11/10 16:54
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.TreeModel>>
     */
    @GetMapping(value = "/getWeekSettingTree")
    public Json<List<TreeModel>> getWeekSettingTree() {
        List<TreeModel> weekSettingTree = tbweeksettingService.getWeekSettingTree();
        return Json.success(weekSettingTree);
    }

    /**
     * 获取可用的星期设置,已排序
     *
     * @return {@link List<Tbweeksetting>}
     * @author wangxk
     * @since 2023-12-13
     */
    @GetMapping("/listUsableWeekSetting")
    public Json<List<Tbweeksetting>> listUsableWeekSetting() {
        return Json.success(tbweeksettingService.listUsableWeekSetting());
    }
}
