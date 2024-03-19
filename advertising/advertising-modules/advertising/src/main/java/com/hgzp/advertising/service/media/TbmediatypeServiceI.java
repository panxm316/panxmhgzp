package com.hgzp.advertising.service.media;

import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
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
     * 获取可用的媒体列表,已排序
     *
     * @return {@link List<Tbmediatype>} 可用的媒体列表
     * @author wangxk
     * @since 2023-12-11
     */
    List<Tbmediatype> listUsableMediaType();
}
