package com.hgzp.advertisingsys.service.system;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleActionDTO;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.RoleVO;
import com.hgzp.core.model.Tbrole;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.system.BaseTbroleServiceI;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbroleServiceI extends BaseTbroleServiceI {


    /**
     * getRolePageList
     * 方法功能: 分页查询所有角色
     * @author wangwk
     * @date 2023/8/24 14:10
     * @param page 分页 参数
     * @param info 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.system.RoleVO>
     */
    IPage<RoleVO> getRolePageList(IPage<Tbrole> page, BaseQueryInfo info) throws Exception;


    /**
     * getRoleDataCombo
     * 方法功能: 显示角色的下拉列表
     * @author wangwk
     * @date 2023/9/2 9:33
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<DataCombo> getRoleDataCombo() throws Exception;

    /**
     * saveRole
     * 方法功能:  角色保存
     * @author wangwk
     * @date 2023/8/24 14:44
     * @param roleModel
     * @return void
     */
    Tbrole saveRole(RoleDTO roleModel) throws Exception;

    /**
     * updateRole
     * 方法功能: 更新角色
     * @author wangwk
     * @date 2023/8/24 16:33
     * @param roleModel
     * @return void
     */
    void updateRole(RoleDTO roleModel) throws Exception;

    /**
     * deleteRoleById
     * 方法功能: 删除角色
     * @author peij
     * @date 2023/8/31 9:38
     * @param ids
     * @return void
     */
    void deleteRoleById(String id) throws Exception;

    /**
     * getRoleActionScopeByRoleId
     * 方法功能:  根据角色id查询所有菜单需要赋权的按钮、以及按钮范围
     * @author wangwk
     * @date 2023/8/28 16:25
     * @param roleId
     * @return java.util.List<com.hgzp.advertising.pagemodel.system.RoleMenuModel>
     */
    RoleActionDTO getRoleActionScopeByRoleId(Long roleId);

    /**
     * saveRoleActionAndRoleActionScope
     * 方法功能:  保存角色按钮及范围
     * @author wangwk
     * @date 2023/9/1 16:46
     * @param roleMenuModelList
     * @param roleId
     * @return void
     */
    void saveRoleActionAndRoleActionScope(RoleActionDTO roleActionDTO) throws Exception;

    /**
     * saveCopyRole
     * 方法功能: 复制角色
     * @author wangwk
     * @date 2023/9/2 8:56
     * @param roleId  角色id
     * @param roleName  角色名称
     * @return void
     */
    void saveCopyRole(Long roleId, String roleName) throws Exception;

    /**
     * getRoleListGroupByUserId
     * 方法功能: 根据人员账号id的查询分组显示每个人的角色列表
     * @author wangwk
     * @date 2023/8/23 13:40
     * @param userIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbrole>>
     */
    Map<Long, List<Tbrole>> getRoleListGroupByUserId(List<Long> userIdList);
    /**
     * getRoleById
     * 方法功能: 根据roleid 获取 role
     * @author peij
     * @date 2023/9/12 14:22
     * @param roleId
     * @return com.hgzp.advertising.pagemodel.system.dto.RoleDTO
     */
    RoleDTO getRoleById(String roleId);
    /**
     * getRoleInfo
     * 方法功能:  获取用于工作流的角色信息
     * @author hgsongly
     * @date 2023/10/13 16:45
     * @param
     * @return com.hgzp.advertisingsys.pagemodel.flow.vo.FlowSelectItem
     */
    Json<Dict> getRoleForFlow() ;
}
