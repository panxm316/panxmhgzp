package com.hgzp.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Menu;
import com.hgzp.core.page.MetaVo;
import com.hgzp.core.page.RouterVo;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.mapper.system.TwmymenuMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.service.system.BaseTbemployroleServiceI;
import com.hgzp.service.system.BaseTbmenuServiceI;
import com.hgzp.service.system.BaseTbrolemenuServiceI;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbmenuServiceImpl extends MyServiceImpl<TbmenuMapper, Tbmenu> implements BaseTbmenuServiceI {

    @Autowired
    TbmenuMapper tbmenuMapper;
    @Autowired
    BaseTbemployroleServiceI tbemployroleService;
    @Autowired
    BaseTbrolemenuServiceI tbrolemenuService;
    @Autowired
    TwmymenuMapper twmymenuMapper;


    @Override
    public List<Tbmenu> getTbmenuListBymenuClass(String menuClass){
        return new LambdaQueryChainWrapper<>(tbmenuMapper)
                .eq(Tbmenu::getSclass, menuClass)
                .orderByAsc(Tbmenu::getIdepth)
                .orderByAsc(Tbmenu::getIsort)
                .list();
    }


    @Override
    public List<RouterVo> getMenuRouterByUserId(Long userId, String menuClass) {
        List<Long> roleIdList = tbemployroleService.getRoldIdListByUserId(userId);
        List<Long> menuIdList = tbrolemenuService.lambdaQuery()
                .select(Tbrolemenu::getMenuid)
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list()
                .stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());

        List<Menu> list = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .in(Tbmenu::getId, menuIdList)
                .eq(Tbmenu::getBmenushow, true)
                .eq(Tbmenu::getSclass, menuClass)
                .orderByAsc(Tbmenu::getIdepth)
                .orderByAsc(Tbmenu::getIsort)
                .list()
                .stream()
                .map(Menu::convertMenu)
                .collect(Collectors.toList());

        List<Menu> tbmenuFirstList = list.stream()
                .filter(menu -> menu.getParentid() == null)
                .collect(Collectors.toList());

        tbmenuFirstList.forEach(node -> node.setMenus(buildChild(node.getId(), list)));

        return buildMenus(tbmenuFirstList);
    }

    private List<RouterVo> buildMenus(List<Menu> menus){
        List<RouterVo> routerVos = new ArrayList<>();
        menus.sort(Comparator.comparing(Menu::getIsort));

        for (Menu t : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setName(getRouteName(t));
            router.setPath(getRouterPath(t));
            router.setComponent(getComponent(t));
            router.setMeta(new MetaVo(t.getSname(), t.getSimageurl(), false, t.getSrouterpath()));
            router.setAlwaysShow(false);
            router.setRedirect("noRedirect");

            if(t.getMenus().size() > 0) {
                router.setChildren(buildMenus(t.getMenus()));
            }

            routerVos.add(router);

        }
        return routerVos;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(Menu menu) {
        return StrUtil.upperFirst(menu.getSrouterpath());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getSrouterpath();
        // 非外链并且是一级目录（类型为目录）
        if (menu.getParentid() == null && !isHttpPath(menu)) {
            routerPath = "/" + menu.getSrouterpath();
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = "Layout";
        if (StrUtil.isNotBlank(menu.getScomponent()) && menu.getParentid() != null) {
            component = menu.getScomponent();
        } else if (StrUtil.isBlank(menu.getScomponent()) && menu.getParentid() != null && isHttpPath(menu)) {
            component = "InnerLink";
        } else if (StrUtil.isBlank(menu.getScomponent()) && menu.getParentid() != null) {
            component = "ParentView";
        }
        return component;
    }


    public boolean isHttpPath(Menu menu){
        return StrUtil.startWithAny(menu.getSrouterpath(), "http", "https");
    }

    private List<Menu> buildChild(Long parentId, List<Menu> tbmenuList){
        List<Menu> child = new ArrayList<>();

        List<Menu> childMenus = tbmenuList.stream()
                .filter(node -> parentId.equals(node.getParentid()))
                .collect(Collectors.toList());
        if(childMenus.size() != 0){
            child.addAll(childMenus);
            childMenus.forEach(node -> node.setMenus(buildChild(node.getId(), tbmenuList)));
        }else{
            return child;
        }
        return child;
    }
    @Override
    public List<Menu> getMymenu(Long userId) {
        List<Menu> lsTbmenu = new ArrayList<>();
        List<Tbmenu> menuList = tbmenuMapper.selectList(Wrappers.emptyWrapper());
        //获取固定菜单ID
        List<Twmymenu> lsbuiltinMenu =  twmymenuMapper.selectList(new LambdaQueryWrapper<>(Twmymenu.class)
                .eq( Twmymenu::getEmploytype, 1)
                .eq(Twmymenu::getBbuiltin, true)
                .orderByAsc(Twmymenu::getIsort));

        if (lsbuiltinMenu.size() > 0) {
            List<Long> lsMenuIds = lsbuiltinMenu.stream().map(Twmymenu::getMenuid).collect(Collectors.toList());
            List<Tbmenu> lsMenu = tbmenuMapper.selectBatchIds(lsMenuIds);
            Map<Long, Tbmenu> mapMenu = lsMenu.stream().collect(Collectors.toMap(Tbmenu::getId, tbmenu -> tbmenu));
            for (Twmymenu twmymenu : lsbuiltinMenu) {
                Tbmenu currentMenu = mapMenu.get(twmymenu.getMenuid());
                Tbmenu parentMenu = menuList.stream().filter(menu -> menu.getId().equals(currentMenu.getParentid())).findFirst().orElse(null);
                Menu mytbmenu =Menu.convertMenu(currentMenu);
                mytbmenu.setSrouterpath(parentMenu.getSrouterpath()+"/"+currentMenu.getSrouterpath());
                lsTbmenu.add(mytbmenu);
            }
        }
        //获取我的角色ID
        List<Long> roleIdList = tbemployroleService.getRoldIdListByUserId(userId);
        List<Long> menuIdList = tbrolemenuService.lambdaQuery()
                .select(Tbrolemenu::getMenuid)
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list()
                .stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());
        //菜单过滤串
        String sEmpFilter = menuIdList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        //获取我的菜单ID
        List<Twmymenu> lsMyMenu =  twmymenuMapper.selectList(new LambdaQueryWrapper<>(Twmymenu.class)
                .eq(Twmymenu::getCreateempid, userId)
                .eq(Twmymenu::getBbuiltin, false)
                .inSql(Twmymenu::getMenuid,"select id from tbmenu where id in (" + sEmpFilter + ")")
                .orderByAsc(Twmymenu::getIsort));

        if (lsMyMenu.size() > 0) {
            List<Long> lsMenuIds = lsMyMenu.stream().map(Twmymenu::getMenuid).collect(Collectors.toList());
            List<Tbmenu> lsMenu = tbmenuMapper.selectBatchIds(lsMenuIds);
            Map<Long, Tbmenu> mapMenu = lsMenu.stream().collect(Collectors.toMap(Tbmenu::getId, tbmenu -> tbmenu));
            for (Twmymenu twmymenu : lsMyMenu) {
                Tbmenu currentMenu = mapMenu.get(twmymenu.getMenuid());
                Tbmenu parentMenu = menuList.stream().filter(menu -> menu.getId().equals(currentMenu.getParentid())).findFirst().orElse(null);
                Menu mytbmenu = Menu.convertMenu(currentMenu);
                mytbmenu.setSrouterpath(parentMenu.getSrouterpath()+"/"+currentMenu.getSrouterpath());
                lsTbmenu.add(mytbmenu);
            }
        }

        return lsTbmenu;
    }

}
