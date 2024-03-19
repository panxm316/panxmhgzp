package com.hgzp.advertisingsys.service.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaPublicNumDto;
import com.hgzp.core.model.Tbmediapublicnum;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.BaseQueryInfo;

import java.util.List;

/**
 * <p>
 * 媒体刊期 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-09-07
 */
public interface TbmediapublicnumServiceI extends IService<Tbmediapublicnum> {

    /**
     * getMediaPublicNumPageList
     * 方法功能: 分页查询媒体刊期
     *
     * @param page
     * @param baseQueryInfo
     * @param mediapublicnum
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbmediapublicnum>
     * @author CGD
     * @date 2023/9/8 15:20
     */
    Page<Tbmediapublicnum> getMediaPublicNumPageList(Page<Tbmediapublicnum> page, BaseQueryInfo baseQueryInfo,
                                                     Tbmediapublicnum mediapublicnum);

    /**
     * examinePublishno
     * 方法功能: 校验是否可保存
     *
     * @param mediapublicnum
     * @return boolean
     * @author CGD
     * @date 2023/9/8 15:20
     */
    boolean examinePublishno(Tbmediapublicnum mediapublicnum);

    /**
     * saveBatchMediaPublicNum
     * 方法功能:  批量添加
     *
     * @param mediaPublicNumDto
     * @return void
     * @author CGD
     * @date 2023/9/8 15:23
     */
    void saveBatchMediaPublicNum(MediaPublicNumDto mediaPublicNumDto);

    /**
     * 删除刊期
     * 方法功能:
     *
     * @param ids
     * @author songly
     * @date 2024/2/28 10:52
     */
    void deleteMediaPublicNumByIds(String ids) throws Exception;
}
