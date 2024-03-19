package com.hgzp.advertisingsys.controller.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaDTO;
import com.hgzp.advertisingsys.service.media.TbmediaServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.*;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * MediaController
 * 创建人：suny
 * 类描述：媒体设置controller
 * 创建日期：2023/8/23 13:51
 *
 * @folder media/MediaController
 */
@RestController
@RequestMapping("/media/media")
@Validated
public class MediaController extends BaseController {
    @Autowired
    private TbmediaServiceI tbmediaService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取媒体信息分页列表
     * 方法功能: 获取媒体信息分页列表
     *
     * @param pageRequest
     * @param baseQueryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbmedia>>
     * @author suny
     * @date 2023/8/23 11:05
     */
    @GetMapping("/getMediaPageList")
    public Json<List<Tbmedia>> getMediaPageList(PageRequest pageRequest, BaseQueryInfo baseQueryInfo) {
        Page<Tbmedia> page = getPage(pageRequest);
        IPage<Tbmedia> mediaList = tbmediaService.getMediaPageList(page, baseQueryInfo);
        return Json.success(mediaList);
    }

    /**
     * 新增媒体信息
     * 方法功能: 新增媒体信息
     *
     * @param media
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/8/23 11:09
     */
    @PostMapping(value = "/saveMedia")
    public Json saveMedia(@Validated(value = {ValidateParam.add.class}) @RequestBody MediaDTO media) {
        if (media.getIsort() == null) {
            Integer maxSort = tbmediaService.getMaxSort();
            media.setIsort(maxSort);
        }
        tbmediaService.saveMedia(media);
        return Json.success();
    }

    /**
     * 媒体编辑更新
     * 方法功能: 媒体编辑更新
     *
     * @param media
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/8/23 11:12
     */
    @PostMapping(value = "/updateMedia")
    public Json updateMedia(@Validated(value = {ValidateParam.edit.class}) @RequestBody MediaDTO media) {
        if (media.getIsort() == null) {
            Integer maxSort = tbmediaService.getMaxSort();
            media.setIsort(maxSort);
        }
        tbmediaService.updateMedia(media);
        return Json.success();
    }

    /**
     * 根据id删除媒体信息
     * 方法功能: 根据id删除媒体信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/8/23 11:13
     */
    @PostMapping(value = "/deleteMediaById")
    public Json deleteMediaById(@NotNull(message = "ID不可为空") String ids) {
//        List<String> idList = Arrays.asList(ids.split(","));
//        innerInterceptor.recoredLog();
//        tbmediaService.removeByIds(idList);
        try {
            tbmediaService.deleteMedia(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取媒体最大isort序号
     * 方法功能: 获取媒体最大isort序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author suny
     * @date 2023/8/24 16:49
     */
    @GetMapping("/getMediaMaxSort")
    public Json<Integer> getMediaMaxSort() {
        Integer maxSort = tbmediaService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 媒体信息树形列表
     * 方法功能: 媒体信息树形列表 角色设置使用
     *
     * @param query
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author peij
     * @date 2023/9/6 8:29
     */
    @GetMapping("/getSysMediaTree")
    public Json<List<TreeModel>> getSysMediaTree(TreeQuery query) {
        List<TreeModel> mediaTree = tbmediaService.getSysMediaTree(query, true);
        return Json.success(mediaTree);
    }

    /**
     * 媒体下拉列表
     * 方法功能: 媒体下拉列表
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author CGD
     * @date 2023/9/7 15:18
     */
    @GetMapping("/getMediaDataCombo")
    public Json<List<DataCombo>> getMediaDataCombo() {
        List<DataCombo> mediaDataCombo = tbmediaService.getMediaDataCombo();
        return Json.success(mediaDataCombo);
    }

    /**
     * 验证所选媒体下是否有关联数据
     * 方法功能: 验证所选媒体下是否有关联数据，有则不允许删除，返回存在的子集属性说明
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/11 13:58
     */
    @GetMapping(value = "/getBMediaChild")
    public Json getBMediaChild(@NotNull(message = "ID不可为空") String ids) {
        String chiledFlagMsg = tbmediaService.getMediaChild(ids);
        return Json.success(chiledFlagMsg);
    }
}
