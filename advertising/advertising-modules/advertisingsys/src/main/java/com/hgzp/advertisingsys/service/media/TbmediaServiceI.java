package com.hgzp.advertisingsys.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaDTO;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
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
     * getMediaPageList
     * 方法功能: 获取媒体分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbmedia>
     * @author suny
     * @date 2023/8/23 13:16
     */
    IPage<Tbmedia> getMediaPageList(Page<Tbmedia> page, BaseQueryInfo query);

    /**
     * getMaxSort
     * 方法功能: 获取媒体最大isort序号
     *
     * @param
     * @return java.lang.Integer
     * @author suny
     * @date 2023/8/24 16:50
     */
    Integer getMaxSort();

    /**
     * saveMedia
     * 方法功能: 新增保存媒体信息
     *
     * @param tbmedia
     * @return void
     * @author suny
     * @date 2023/8/25 9:19
     */
    void saveMedia(MediaDTO tbmedia);

    /**
     * updateMedia
     * 方法功能: 更新保存媒体信息
     *
     * @param tbmedia
     * @return void
     * @author suny
     * @date 2023/8/25 9:19
     */
    void updateMedia(MediaDTO tbmedia);

    /**
     * 根据Id删除媒体
     * 方法功能:
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2024/2/28 13:24
     */
    void deleteMedia(String ids) throws Exception;

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
     * getMediaChild
     * 方法功能: 验证所选媒体下是否有关联数据，返回存在的子集属性说明
     *
     * @param ids
     * @return java.lang.String
     * @author suny
     * @date 2023/10/11 13:59
     */
    String getMediaChild(String ids);
}
