package com.hgzp.advertisingsys.service.system;

import com.hgzp.core.model.Tbroleaction;
import com.hgzp.service.system.BaseTbroleactionServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限行为表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbroleactionServiceI extends BaseTbroleactionServiceI {


    /**
     * deleteRoleActionsByRoleMenuId
     * 方法功能:根据角色菜单关联的id 删除角色关联的按钮
     * @author wangwk
     * @date 2023/9/1 13:16
     * @param roleMenuIds
     * @return void
     */
    void deleteRoleActionsByRoleMenuId(List<Long> roleMenuIds);

    /**
     * getRoleActionListGroupByRoleMenuId
     * 方法功能: 根据 roleMenuIds 分组查询出角色所有菜单已有的按钮 的信息
     * @author wangwk
     * @date 2023/8/25 13:10
     * @param roleMenuIds
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbroleaction>>
     */
    Map<Long, List<Tbroleaction>> getRoleActionListGroupByRoleMenuId(List<Long> roleMenuIds);


    /**
     * getRoleActionListByRoleMenuId
     * 方法功能:  根据  roleMenuIds 查询出角色已有的按钮信息
     * @author wangwk
     * @date 2023/8/28 9:02
     * @param roleMenuIds
     * @return java.util.List<com.hgzp.core.model.Tbroleaction>
     */
    List<Tbroleaction> getRoleActionListByRoleMenuId(List<Long> roleMenuIds);
}
