package com.hgzp.advertising.service.system.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.system.TbemployroleServiceI;
import com.hgzp.advertising.service.system.TbmenuServiceI;
import com.hgzp.advertising.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.model.Tbrolemenu;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.service.system.impl.BaseTbmenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmenuServiceImpl extends BaseTbmenuServiceImpl implements TbmenuServiceI {

    @Autowired
    TbmenuMapper tbmenuMapper;
    @Autowired
    TbemployroleServiceI tbemployroleService;
    @Autowired
    TbrolemenuServiceI tbrolemenuService;

    @Override
    public List<Tbmenu> getMenuList() {
        List<Tbmenu> list = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .orderByAsc(Tbmenu::getIdepth)
                .orderByAsc(Tbmenu::getIsort)
                .list();

        return list;
    }

    @Override
    public List<TreeModel> getMenuTree() {
        List<Tbmenu> list = this.lambdaQuery()
                .eq(Tbmenu::getBuse, true)
                .orderByAsc(Tbmenu::getIdepth)
                .orderByAsc(Tbmenu::getIsort)
                .list();
        return convertTreeModel(list);
    }



    /**
     * deptConvertTreeModel
     * 方法功能:  将数据库对象转换成树对象
     *
     * @param tbmenuList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/18 13:41
     */
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

    /**
     * 根据角色id获取菜单列表
     * 方法功能:
     * @author wangwk
     * @date 2023/11/2 16:09
     * @param roleIdList
     * @return java.util.List<com.hgzp.core.model.Tbmenu>
     */
    @Override
    public List<Tbmenu> getTbMenusByRoldIds(List<Long> roleIdList){
        List<Long> menuIdList = tbrolemenuService.lambdaQuery()
                .select(Tbrolemenu::getMenuid)
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list()
                .stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());

        List<Tbmenu> list = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .in(Tbmenu::getId, menuIdList)
                .eq(Tbmenu::getBuse, true)
                .orderByAsc(Tbmenu::getIdepth)
                .orderByAsc(Tbmenu::getIsort)
                .list();
        return list;
    }





}
