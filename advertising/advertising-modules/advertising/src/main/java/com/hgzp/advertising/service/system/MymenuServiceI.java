package com.hgzp.advertising.service.system;

import com.hgzp.advertising.pagemodel.system.dto.MymenuDTO;
import com.hgzp.advertising.pagemodel.system.vo.MymenuVO;
import com.hgzp.core.model.Twmymenu;
import com.hgzp.service.common.IMyService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 我的菜单 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-01-22
 */
public interface MymenuServiceI extends IMyService<Twmymenu> {

    /**
     * 保存Twmymenu
     *
     * @param mymenuDTO 我的菜单数据ids
     * @return void
     * @author muyn
     * @since 2024-01-22
     */
    void saveMymenu(MymenuDTO mymenuDTO) throws Exception;

    /**
     * 删除Twmymenu
     *
     * @param sMenuIds 我的菜单ids
     * @return void
     * @author muyn
     * @since 2024-01-22
     */
    void delete(String sMenuIds) throws Exception;

    /**
     * 根据ID查询Twmymenu
     *
     * @param id Twmymenu.id
     * @return {@link Twmymenu}
     * @author muyn
     * @since 2024-01-22
     */
    @Override
    Twmymenu getById(Serializable id);

    /**
     * 获取我的常用菜单设置项
     *
     * @return {@link List<MymenuVO>}
     * @author muyn
     * @since 2024-01-22
     */
    List<MymenuVO> getTwmymenu() throws Exception;

    /**
     * 获取我的常用菜单
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbmenu>
     * @author songly
     * @date 2024/1/25 13:21
     */


}
