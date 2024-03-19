package com.hgzp.advertisingsys.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.media.vo.MediaTypeVO;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 媒体类型 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbmediatypeServiceI extends IMyService<Tbmediatype> {

    /**
     * getMediaTypePageList
     * 方法功能:分页：获取媒体类型
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbmediatype>
     * @author yanz
     * @date 2023/8/31 15:05
     */
    IPage<Tbmediatype> getMediaTypePageList(Page<Tbmediatype> page, MediaTypeVO vo);

    /**
     * getMediaTypeCombo
     * 方法功能: 获取媒体类型下拉列表
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author suny
     * @date 2023/8/24 14:36
     */
    List<DataCombo> getMediaTypeCombo() throws Exception;

    /**
     * deleteMediaTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:33
     */
    boolean deleteMediaTypeByIds(List<String> idList);

    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     *
     * @return Json
     * @author songly
     * @date 2023/9/15 10:34
     */
    Json doDefaultLogic(Tbmediatype tbmediatype);

    /**
     * saveMediaType
     * 方法功能: 新增媒体类型
     *
     * @param tbmediatype
     * @return void
     * @author suny
     * @date 2023/9/20 13:22
     */
    void saveMediaType(Tbmediatype tbmediatype);

    /**
     * updateMediaType
     * 方法功能: 修改更新媒体类型
     *
     * @param tbmediatype
     * @return void
     * @author suny
     * @date 2023/9/20 13:22
     */
    void updateMediaType(Tbmediatype tbmediatype);
}
