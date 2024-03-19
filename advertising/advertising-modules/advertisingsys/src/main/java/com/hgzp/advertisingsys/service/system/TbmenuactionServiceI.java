package com.hgzp.advertisingsys.service.system;

import com.hgzp.advertisingsys.pagemodel.system.dto.MenuactionDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.MenuactionVO;
import com.hgzp.core.model.Tbmenuaction;
import com.hgzp.service.system.BaseTbmenuactionServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单行为表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbmenuactionServiceI extends BaseTbmenuactionServiceI {





    /**
     * getMenuActionListGroupByMenuId
     * 方法功能: 根据menuid 分组查询出所有的按钮 的信息
     * @author wangwk
     * @date 2023/8/25 12:54
     * @param menuIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbmenuaction>>
     */
    Map<Long, List<Tbmenuaction>> getMenuActionListGroupByMenuId(List<Long> menuIdList);

    /**
     * getMenuActionListByMenuId
     * 方法功能: 根据菜单id获取 菜单按钮的信息
     * @author wangwk
     * @date 2023/8/30 15:10
     * @param menuId 菜单id
     * @return java.util.List<com.hgzp.advertising.pagemodel.system.vo.MenuactionVo>
     */
    List<MenuactionVO> getMenuActionListByMenuId(Long menuId);

    /**
     * saveMenuAction
     * 方法功能: 保存 菜单行为
     * @author wangwk
     * @date 2023/8/30 16:14
     * @param menuactionDTO
     * @return void
     */
    void saveMenuAction(MenuactionDTO menuactionDTO);

    /**
     * updateMenuAction
     * 方法功能: 更新 菜单行为
     * @author wangwk
     * @date 2023/8/31 16:42
     * @param menuactionDTO
     * @return void
     */
    void updateMenuAction(MenuactionDTO menuactionDTO);


    /**
     * deleteMenuAction
     * 方法功能:  删除菜单行为，多个用逗号隔开
     * @author wangwk
     * @date 2023/8/31 17:10
     * @param ids
     * @return void
     */
    void deleteMenuAction(String ids);

    /**
     * checkActionKeyCodeOnly
     * 方法功能: 测试keycode 是否唯一
     * @author wangwk
     * @date 2023/9/1 9:14
     * @param actionId
     * @param keyCode
     * @return boolean
     */
    boolean checkActionKeyCodeOnly(String actionId, String keyCode);
}
