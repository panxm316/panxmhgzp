package com.hgzp.service.system;

import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.page.Menu;
import com.hgzp.core.page.RouterVo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbmenuServiceI extends IMyService<Tbmenu> {


    /**
     * getTbmenuListBymenuClass
     * 方法功能: 根据菜单类型
     *
     * @param menuClass
     * @return java.util.List<com.hgzp.core.model.Tbmenu>
     * @author wangwk
     * @date 2023/9/18 10:10
     */
    List<Tbmenu> getTbmenuListBymenuClass(String menuClass);

    /**
     * getMenuRouterByUserId
     * 方法功能: 根据人员id查询权限菜单构造前端路由
     *
     * @param userId    用户id
     * @param menuClass MenuClassConstants
     * @return java.util.List<com.hgzp.core.page.RouterVo>
     * @author wangwk
     * @date 2023/8/17 16:48
     */
    List<RouterVo> getMenuRouterByUserId(Long userId, String menuClass);

    /**
     * 获取我的常用菜单
     * 方法功能:
     *
     * @param userId
     * @return java.util.List<com.hgzp.core.page.Menu>
     * @author songly
     * @date 2024/1/25 16:56
     */
    List<Menu> getMymenu(Long userId);
}
