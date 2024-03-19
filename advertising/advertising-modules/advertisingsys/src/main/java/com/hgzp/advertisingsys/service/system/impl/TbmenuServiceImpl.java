package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertisingsys.service.system.TbemployroleServiceI;
import com.hgzp.advertisingsys.service.system.TbmenuServiceI;
import com.hgzp.advertisingsys.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.service.system.impl.BaseTbmenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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



}
