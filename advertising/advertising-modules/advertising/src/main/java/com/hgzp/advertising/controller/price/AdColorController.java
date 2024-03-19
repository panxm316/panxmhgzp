package com.hgzp.advertising.controller.price;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.price.TbadcolorServiceI;
import com.hgzp.core.model.Tbadcolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * AdColorController
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/8/17 11:54
 *
 * @folder price/AdColorController
 */
@RestController
@RequestMapping("/price/adcolor")
@Validated
public class AdColorController extends BaseController {

    @Autowired
    TbadcolorServiceI adcolorService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 新增广告色彩
     * 方法功能: 新增广告色彩
     *
     * @param tbadcolor
     * @return com.hgzp.core.page.Json     *
     * @author peij
     * @date 2023/8/17 16:57
     */
    @PostMapping("/saveAdcolor")
    public Json saveAdcolor(@RequestBody Tbadcolor tbadcolor) {
        innerInterceptor.recoredLog();
        return adcolorService.save(tbadcolor) ? Json.success() : Json.fail("保存失败");
    }

    /**
     * 根据id删除广告色彩
     * 方法功能: 根据id删除广告色彩
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author peij
     * @date 2023/8/17 17:07
     */
    @PostMapping("/deleteAdcolor")
    public Json deleteAdcolor(@NotBlank(message = "ids不能为空") String ids) {
        innerInterceptor.recoredLog();
        adcolorService.removeByIds(CollUtil.newArrayList(ids.split(",")));
        return Json.success();
    }

    /**
     * 更新广告色彩
     * 方法功能: 更新广告色彩
     *
     * @param adcolor
     * @return com.hgzp.core.page.Json
     * @author peij
     * @date 2023/8/17 17:07
     */
    @PostMapping("/updateAdcolor")
    public Json updateAdcolor(@RequestBody Tbadcolor adcolor) {
        innerInterceptor.recoredLog();
        adcolorService.updateById(adcolor);
        return Json.success();
    }

    /**
     * 获取广告色彩分页列表
     * 方法功能: 获取广告色彩分页列表 根据媒体类型获取
     *
     * @param pageRequeste
     * @param baseQueryInfo
     * @param mediatypekey  媒体类型key
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbadcolor>>
     * @author peij
     * @date 2023/8/24 14:32
     */
    @GetMapping("/getAdcolorList")
    public Json<List<Tbadcolor>> getAdcolorList(PageRequest pageRequeste, BaseQueryInfo baseQueryInfo,
                                                String mediatypekey) {
        Page page = getPage(pageRequeste);
        page.setSize(-1);
        IPage<Tbadcolor> adcolorList = adcolorService.getAdcolorList(page, baseQueryInfo, mediatypekey);
        return Json.success(adcolorList);
    }


    /**
     * 获取最大序号
     * 方法功能: 获取最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author peij
     * @date 2023/8/21 17:10
     */
    @GetMapping("/getMaxSort")
    public Json<Integer> getMaxSort() {
        Integer maxSort = adcolorService.getMaxSort();
        return Json.success(maxSort);

    }

    /**
     * 根据媒体类型获取广告色彩树
     * 方法功能:  根据媒体类型获取广告色彩树
     *
     * @param mediaType
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author CGD
     * @date 2023/11/13 9:54
     */
    @GetMapping("/getAdColorTreeList")
    public Json<List<TreeModel>> getAdColorTreeList(String mediaType) {
        List<TreeModel> adColorTreeList = adcolorService.getAdColorTreeList(mediaType);
        return Json.success(adColorTreeList);
    }

    /**
     * 根据媒体类型获取可用的广告颜色列表,已排序
     *
     * @param mediaType 媒体类型
     * @return {@link List<Tbadcolor>}
     * @author wangxk
     * @since 2023-12-12
     */
    @GetMapping("/listUsableAdColor")
    public Json<List<Tbadcolor>> listUsableAdColor(String mediaType) {
        return Json.success(adcolorService.getAdColorListValid(mediaType));
    }
}