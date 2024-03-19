package com.hgzp.advertising.controller.schedule;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.schedule.vo.PageColorVo;
import com.hgzp.advertising.service.schedule.TbpagecolorServiceI;
import com.hgzp.core.model.Tbpagecolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * TbpagecolorController
 * 创建人：songly
 * 类描述：版面色彩服务接口
 * 创建日期：2023/11/15 10:20
 *
 * @folder schedule/TbpagecolorController
 */
@RestController
@RequestMapping("/schedule/pagecolor")
@Validated
public class TbpagecolorController extends BaseController {
    @Autowired
    TbpagecolorServiceI pageColorService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取版面色彩分页列表
     * 方法功能: 获取版面色彩分页列表
     *
     * @param pageRequeste
     * @param baseQueryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpagecolor>>
     * @author songly
     * @date 2023/11/15 9:59
     */
    @GetMapping("/getPagecolorList")
    public Json<List<PageColorVo>> getPagecolorList(PageRequest pageRequeste, BaseQueryInfo baseQueryInfo) {
        Page page = getPage(pageRequeste);
        IPage<PageColorVo> PagecolorList = pageColorService.getPageColorList(page, baseQueryInfo);
        return Json.success(PagecolorList);
    }

    /**
     * 获取所有版面色彩
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.schedule.Vo.PageColorVo>>
     * @author songly
     * @date 2023/11/16 13:32
     */
    @GetMapping("/getPagecolorAll")
    public Json<List<PageColorVo>> getPagecolorAllData() {
        List<PageColorVo> PagecolorList = pageColorService.getPageColorAll();
        return Json.success(PagecolorList);
    }

    /**
     * 根据Id获取版面色彩
     * 方法功能: 根据Id获取版面色彩
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbpagecolor>
     * @author songly
     * @date 2023/11/15 10:16
     */
    @GetMapping(value = "/getPagecolorById")
    public Json<PageColorVo> getPageColorById(@NotNull(message = "ID不可为空") String id) {
        Tbpagecolor tbpagecolor = pageColorService.getById(id);
        PageColorVo pageColorVo = new PageColorVo();
        BeanUtils.copyProperties(tbpagecolor, pageColorVo);
        if (tbpagecolor.getColorlist() != null && StrUtil.isBlank(tbpagecolor.getColorlist())) {
            List<String> list = Arrays.asList(tbpagecolor.getColorlist().split(","));
            pageColorVo.setLsColors(list);
        }
        return ObjectUtil.isNotNull(tbpagecolor) ? Json.success(pageColorVo) : Json.fail();
    }

    /**
     * 新增版面色彩
     * 方法功能:新增版面色彩
     *
     * @param tbpagecolor
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/15 10:00
     */
    @PostMapping("/savePagecolor")
    public Json savePagecolor(@RequestBody Tbpagecolor tbpagecolor) {
        innerInterceptor.recoredLog();
        return pageColorService.save(tbpagecolor) ? Json.success() : Json.fail("保存失败");
    }

    /**
     * 根据Id删除版面色彩
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/15 10:01
     */
    @PostMapping("/deletePagecolor")
    public Json deletePagecolor(@NotBlank(message = "ids不能为空") String ids) {
        innerInterceptor.recoredLog();
        pageColorService.deletePageColorById(ids);
        return Json.success();
    }

    /**
     * 更新版面色彩
     * 方法功能: 更新版面色彩
     *
     * @param tbpagecolor
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/15 10:09
     */
    @PostMapping("/updatePagecolor")
    public Json updatePagecolor(@RequestBody Tbpagecolor tbpagecolor) {
        innerInterceptor.recoredLog();
        pageColorService.updateById(tbpagecolor);
        return Json.success();
    }

    /**
     * 获取版面色彩下拉框数据
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/11/16 9:07
     */
    @GetMapping(value = "/getPagecolorCombo")
    public Json<List<DataCombo>> getPageColorCombo() {
        List<DataCombo> customerData = pageColorService.getPageColorCombo();
        return Json.success(customerData);
    }

    /**
     * 获取版面色彩的最大序号
     * 方法功能: 获取版面色彩的最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author songly
     * @date 2023/11/15 10:13
     */
    @GetMapping("/getMaxSort")
    public Json<Integer> getMaxSort() {
        Integer maxSort = pageColorService.getMaxSort();
        return Json.success(maxSort);
    }
}
