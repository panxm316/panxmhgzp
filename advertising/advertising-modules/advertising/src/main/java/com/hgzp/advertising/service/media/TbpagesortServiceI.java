package com.hgzp.advertising.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.media.vo.PagesortVO;
import com.hgzp.core.model.Tbpagesort;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 版面类别 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbpagesortServiceI extends IMyService<Tbpagesort> {

    /**
     * getPagesortPageList
     * 方法功能: 版面类别列表
     * @author wangwk
     * @date 2023/9/19 17:15
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.media.vo.PagesortVO>
     */
    IPage<PagesortVO> getPagesortPageList(IPage<Tbpagesort> page, PagesortVO query);

    /**
     * savePagesort
     * 方法功能: 保存版面类别信息
     * @author wangwk
     * @date 2023/9/20 8:51
     * @param tbpagesort
     * @return void
     */
    void savePagesort(Tbpagesort tbpagesort);

    /**
     * updatePagesort
     * 方法功能:  更新版面类别信息
     * @author wangwk
     * @date 2023/9/20 8:53
     * @param tbpagesort
     * @return void
     */
    void updatePagesort(Tbpagesort tbpagesort);

    /**
     * 根据媒体类型和叠次获取版面类别树
     * 方法功能: 根据媒体类型和叠次获取版面类别树
     * @author CGD
     * @date 2023/11/13 9:53
     * @param mediaType
     * @param foldId
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    List<TreeModel> getPageSortTreeList(String mediaType, Long foldId);

    /**
     * 获取可用的版面类别列表,已排序
     *
     * @param mediaType 媒体类型
     * @param foldId    版次id
     * @return {@link List<Tbpagesort>}
     * @author wangxk
     * @since 2023-12-12
     */
    List<Tbpagesort> listUsablePageSort(String mediaType, Long foldId);
}
