package com.hgzp.advertising.controller.media;

import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 根据媒体类型查询媒体下拉列表
     * 方法功能: 根据媒体类型查询媒体下拉列表
     *
     * @param type 媒体类型
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author wangwk
     * @date 2023/9/19 15:53
     */
    @GetMapping("/getMediaDataComboByType")
    public Json<List<DataCombo>> getMediaDataComboByType(String type) {
        List<DataCombo> mediaDataCombo = tbmediaService.getMediaDataComboByType(type);
        return Json.success(mediaDataCombo);
    }

    /**
     * 媒体信息树形列表
     * 方法功能: 媒体信息树形列表 角色设置使用
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author suny
     * @date 2023/12/1 14:03
     */
    @GetMapping("/getSysMediaTree")
    public Json<List<TreeModel>> getSysMediaTree(TreeQuery query) {
        List<TreeModel> mediaTree = tbmediaService.getSysMediaTree(query, true);
        return Json.success(mediaTree);
    }

    /**
     * 获取有效的媒体信息
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbmedia>>
     * @author songly
     * @date 2023/12/11 16:23
     */
    @GetMapping("/getMediaDataList")
    public Json<List<Tbmedia>> getMediaList() {
        List<Tbmedia> mediaList = tbmediaService.getMediaList();
        return Json.success(mediaList);
    }

    /**
     * 媒体叠次信息树形列表
     * 方法功能: 媒体叠次信息树形列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/20
     */
    @GetMapping("/getMediaFloadTree")
    public Json<List<TreeModel>> getMediaFloadTree(TreeQuery query) {
        List<TreeModel> mediaTree = tbmediaService.getMediaFloadTree(query);
        return Json.success(mediaTree);
    }

    /**
     * 媒体叠次版本信息树形列表
     * 方法功能: 媒体叠次版本信息树形列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/22
     */
    @GetMapping("/getMediaFloadAreaverTree")
    public Json<List<TreeModel>> getMediaFloadAreaverTree(TreeQuery query) {
        List<TreeModel> mediaTree = tbmediaService.getMediaFloadAreaverTree(query);
        return Json.success(mediaTree);
    }

}
