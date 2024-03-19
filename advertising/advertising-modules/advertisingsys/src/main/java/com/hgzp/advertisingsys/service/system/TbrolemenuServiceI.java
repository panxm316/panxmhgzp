package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbmenu;
import com.hgzp.service.system.BaseTbrolemenuServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbrolemenuServiceI extends BaseTbrolemenuServiceI {


    /**
     * getMenuListGroupByRoldId
     * 方法功能: 根据角色id 分组 查询角色对应的菜单
     * @author wangwk
     * @date 2023/8/24 13:48
     * @param roleIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbmenu>>
     */
    Map<Long, List<Tbmenu>> getMenuListGroupByRoldId(List<Long> roleIdList);
}
