package com.hgzp.advertising.service.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.media.dto.AdspecDateModelDTO;
import com.hgzp.advertising.pagemodel.media.vo.AdspecModelVO;
import com.hgzp.core.model.Tbadspec;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 广告规格 服务类
 * </p>
 *
 * @author CGD
 * @date 2023/9/18 15:39
 */
public interface TbadspecServiceI extends IMyService<Tbadspec> {

    /**
     * getAdspecPageList
     * 方法功能: 广告规格分页查询
     *
     * @param page
     * @param adspecModelVO
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.advertising.pagemodel.media.vo.AdspecModelVO>
     * @author CGD
     * @date 2023/9/20 14:02
     */
    Page<AdspecModelVO> getAdspecPageList(Page<Tbadspec> page, AdspecModelVO adspecModelVO);

    /**
     * saveAdspec
     * 方法功能:   广告规格保存
     *
     * @param adspec
     * @return void
     * @author CGD
     * @date 2023/9/20 14:02
     */

    void saveAdspec(Tbadspec adspec);

    /**
     * updateAdspec
     * 方法功能:    广告规格修改
     *
     * @param adspec
     * @return void
     * @author CGD
     * @date 2023/9/20 14:03
     */

    void updateAdspec(Tbadspec adspec);

    /**
     * deleteAdspecById
     * 方法功能:  广告规格删除
     *
     * @param ids
     * @return void
     * @author CGD
     * @date 2023/9/20 14:03
     */

    void deleteAdspecById(String ids);

    /**
     * updateAdspecDate
     * 方法功能:  更新规格时间
     *
     * @param adspecDate
     * @return void
     * @author CGD
     * @date 2023/9/20 15:43
     */
    void updateAdspecDate(AdspecDateModelDTO adspecDate);

    /**
     * 根据媒体id获取广告规格树
     * 方法功能:根据媒体id获取广告规格树
     *
     * @param mediaId
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author CGD
     * @date 2023/11/13 9:24
     */
    List<TreeModel> getAdspecTreeList(String mediaId);

    /**
     * 根据媒体id获取可用的广告规格列表,已排序
     *
     * @param mediaId 媒体id
     * @return {@link List<Tbadspec>}
     * @author wangxk
     * @since 2023-12-12
     */
    List<Tbadspec> listUsableAdSpec(Long mediaId);

    /**
     * 获取广告规格列表(只获取分类广告)
     * 方法功能: 获取广告规格列表 (只获取分类广告)
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbadspec>
     * @author suny
     * @date 2024/2/27 13:22
     */
    List<Tbadspec> getAdspecListForCJ();
}
