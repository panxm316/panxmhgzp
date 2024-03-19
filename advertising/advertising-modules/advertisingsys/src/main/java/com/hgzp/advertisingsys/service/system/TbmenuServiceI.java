package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.system.BaseTbmenuServiceI;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbmenuServiceI extends BaseTbmenuServiceI {


    /**
     * getMenuList
     * 方法功能: 查询所有的菜单
     * @author wangwk
     * @date 2023/8/17 9:47
     * @return java.util.List<com.hgzp.core.model.Tbmenu>
     */
    public List<Tbmenu> getMenuList();


    List<TreeModel> getMenuTree();
}
