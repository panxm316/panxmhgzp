package com.hgzp.advertising.service.system;

import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.core.model.Tbrole;
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
     * getRoleListGroupByUserId
     * 方法功能: 根据人员账号id的查询分组显示每个人的角色列表
     * @author wangwk
     * @date 2023/8/23 13:40
     * @param userIdList
     * @return java.util.Map<java.lang.Long,java.util.List<com.hgzp.core.model.Tbrole>>
     */
    Map<Long, List<Tbrole>> getRoleListGroupByUserId(List<Long> userIdList);

    /**
     * 根据用户身份获取用户权限
     * 方法功能:
     *     1、兼职超管登录后，查询条件不限制；操作员（纯操作员）登录后，默认是能看到所有数据
     *     2、单位管理员登录后，查看所负责的部门及所有子部门
     *     3、业务部门负责人登录后，自己所在的部门和兼职部门及所有子部门的数据。
     *     4、业务员（纯业务员）登录后，只能看自己的数据
     *     5、以上均为默认权限规则，后续角色范围权限在权限拦截器拦截后进行相应赋值
     * @author wangwk
     * @date 2023/11/2 14:19
     * @param userId
     * @return EmpAuthorityDTO
     */
    EmpAuthorityDTO getEmpAuthIdsByEmpId(long userId);

    /**
     * 根据用户id和按钮url获取用户权限范围
     * 方法功能:  根据用户id查询在某个url上是否有权限以及
     *          媒体权限范围  或
     *          部门权限范围
     * @author wangwk
     * @date 2023/11/3 10:05
     * @param userId
     * @param functionUrl
     * @return com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO
     */
    EmpAuthorityDTO getUserRoleScopeByMenu(Long userId, String functionUrl);

    /**
     * 根据用户id获取角色列表
     * 方法功能: 根据用户id获取角色列表
     *         若有单独赋权则返回单独赋权
     * @author wangwk
     * @date 2023/11/2 15:47
     * @param userId
     * @return java.util.List<com.hgzp.core.model.Tbrole>
     */
    List<Tbrole> getRolesByEmpId(Long userId);

    /**
     * 根据用户id获取用户按钮权限
     * 方法功能: 根据用户id获取用户按钮权限
     * @author wangwk
     * @date 2023/11/7 14:03
     * @param empId
     * @return java.util.List<java.lang.String>
     */
    List<String> getUserPermissions(Long empId);
}
