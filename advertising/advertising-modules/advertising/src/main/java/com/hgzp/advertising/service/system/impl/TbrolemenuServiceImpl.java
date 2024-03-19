package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.system.TbroleServiceI;
import com.hgzp.advertising.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.service.system.impl.BaseTbrolemenuServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbrolemenuServiceImpl extends BaseTbrolemenuServiceImpl implements TbrolemenuServiceI {
    @Autowired
    private TbroleServiceI roleServiceI;
    @Autowired
    TbmenuMapper tbmenuMapper;


    @Override
    public Map<Long, List<Tbmenu>> getMenuListGroupByRoldId(List<Long> roleIdList) {
        Map<Long, List<Tbmenu>> resultMap = new HashMap<>();

        List<Tbrolemenu> tbrolemenuList = this.lambdaQuery()
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list();

        List<Long> menuIdList = tbrolemenuList.stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());

        if (menuIdList.size() == 0) {
            roleIdList.forEach(id -> resultMap.put(id, Collections.EMPTY_LIST));
            return resultMap;
        }

        Map<Long, List<Tbmenu>> menuMap = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .in(Tbmenu::getId, menuIdList)
                .list()
                .stream()
                .collect(Collectors.groupingBy(Tbmenu::getId));

        Map<Long, List<Tbrolemenu>> rolemenuByRoleId = tbrolemenuList.stream()
                .collect(Collectors.groupingBy(Tbrolemenu::getRoleid));

        for (Long roleId : roleIdList) {
            List<Tbrolemenu> tbrolemenus = rolemenuByRoleId.get(roleId);
            if (tbrolemenus == null) {
                resultMap.put(roleId, Collections.EMPTY_LIST);
                continue;
            }
            List<Tbmenu> tbmenuList = tbrolemenus.stream()
                    .map(Tbrolemenu::getMenuid)
                    .map(menuMap::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            resultMap.put(roleId, tbmenuList);

        }
        return resultMap;

    }

    @Override
    public List<TreeModel> getRoleMenuTree() throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //获取角色
        List<Long> empIds = new ArrayList<>();
        empIds.add(user.getUserid());
        Map<Long, List<Tbrole>> roleListGroupByUserId = roleServiceI.getRoleListGroupByUserId(empIds);
        //去掉我的
        List<Long> myMenuIds = new ArrayList<>();
        List<Tbmenu> tbMymenu = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .eq(Tbmenu::getSrouterpath, "personal")
                .list();
        if (tbMymenu.size() > 0) {
            myMenuIds =new LambdaQueryChainWrapper<>(tbmenuMapper)
                    .eq(Tbmenu::getParentid, tbMymenu.get(0).getId())
                    .list()
                    .stream()
                    .map(Tbmenu::getId)
                    .collect(Collectors.toList());
            myMenuIds.add(tbMymenu.get(0).getId());
        }
        //判断全部权限
        boolean bAllRight = false;
        for (Tbrole tbrole : roleListGroupByUserId.get(user.getUserid())) {
            if (tbrole.getBall()) {
                bAllRight = true;
                break;
            }
        }
        //如果全部权限返回所有菜单树
        if (bAllRight) {
            List<Tbmenu> lsMenu = new LambdaQueryChainWrapper<>(tbmenuMapper)
                    .eq(Tbmenu::getBuse, 1)
                    .eq(Tbmenu::getSclass, "advertising")
                    .notIn(myMenuIds.size()>0,Tbmenu::getId,myMenuIds)
                    .orderByAsc(Tbmenu::getIsort)
                    .list();
            return convertTreeModel(lsMenu);
        } else {
            //获取角色Id
            List<Long> roleIdList = roleListGroupByUserId.get(user.getUserid()).stream().map(Tbrole::getId).collect
                    (Collectors.toList());

            String sEmpFilter = roleIdList.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            //检索角色范围内菜单
            List<Tbmenu> lsRoleMenu = new LambdaQueryChainWrapper<>(tbmenuMapper)
                    .inSql(Tbmenu::getId, "select menuid from " +
                            "Tbrolemenu where roleid  in (" + sEmpFilter + ")")
                    .notIn(myMenuIds.size()>0,Tbmenu::getId,myMenuIds)
                    .eq(Tbmenu::getBuse, true)
                    .eq(Tbmenu::getSclass, "advertising")
                    .orderByAsc(Tbmenu::getIsort)
                    .list();

            //检索角色范围内菜单父菜单
            List<Long> lsRoleMenuParentId =
                    lsRoleMenu.stream().map(Tbmenu::getParentid).distinct().collect(Collectors.toList());
            List<Long> lsRoleMenuId = lsRoleMenu.stream().map(Tbmenu::getId).distinct().collect(Collectors.toList());
            List<Tbmenu> lsParentMenu = new LambdaQueryChainWrapper<>(tbmenuMapper)
                    .in(Tbmenu::getId, lsRoleMenuParentId)
                    .notIn(Tbmenu::getId, lsRoleMenuId)
                    .orderByAsc(Tbmenu::getIsort)
                    .list();

            List<TreeModel> menuTree = new ArrayList<>();

            if (lsParentMenu.size() > 0) {
                menuTree.addAll(convertTreeModel(lsParentMenu));
            }
            if (lsRoleMenu.size() > 0) {
                menuTree.addAll(convertTreeModel(lsRoleMenu));
            }

            return menuTree;
        }

    }

    private List<TreeModel> convertTreeModel(List<Tbmenu> tbmenuList) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbmenu tbmenu : tbmenuList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbmenu.getId());
            treeModel.setParentId(tbmenu.getParentid());
            treeModel.setName(tbmenu.getSname());
            treeModel.setNocheck(false);
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }
}
