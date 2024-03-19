package com.hgzp.advertising.service.media;

import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 媒体信息 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbmediaServiceI extends IMyService<Tbmedia> {

    /**
     * getMediaDataCombo
     * 方法功能: 媒体下拉列表
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author CGD
     * @date 2023/9/7 15:21
     */
    List<DataCombo> getMediaDataCombo();

    /**
     * getMediaDataComboByType
     * 方法功能: 根据媒体类型查询媒体下拉列表
     *
     * @param type
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author wangwk
     * @date 2023/9/19 15:52
     */
    List<DataCombo> getMediaDataComboByType(String type);

    /**
     * getSysMediaTree
     * 方法功能: 媒体信息树形列表 角色设置使用
     *
     * @param query         查询条件
     * @param showUnuseDept 是否显示未启用的媒体
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author peij
     * @date 2023/9/5 17:04
     */
    List<TreeModel> getSysMediaTree(TreeQuery query, boolean showUnuseDept);
    List<Tbmedia> getMediaList();
    /**
     * 媒体叠次信息树形列表
     * 方法功能: 媒体叠次信息树形列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/20
     */
    List<TreeModel> getMediaFloadTree(TreeQuery query);

    /**
     * 媒体叠次版本信息树形列表
     * 方法功能: 媒体叠次版本信息树形列表
     *
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/22
     */
    List<TreeModel> getMediaFloadAreaverTree(TreeQuery query);

}
