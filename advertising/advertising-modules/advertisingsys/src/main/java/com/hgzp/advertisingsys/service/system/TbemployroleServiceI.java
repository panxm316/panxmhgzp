package com.hgzp.advertisingsys.service.system;

import com.hgzp.service.system.BaseTbemployroleServiceI;

import java.util.List;

/**
 * <p>
 * 人员角色表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbemployroleServiceI extends BaseTbemployroleServiceI {


    /**
     * getRoldIdListByUserId
     * 方法功能:  根据用户id获取所有的角色id
     * @author wangwk
     * @date 2023/8/17 17:11
     * @param userId 用户id
     * @return java.util.List<java.lang.Long>
     */
    public List<Long> getRoldIdListByUserId(Long userId);

    /**
     * deleteByEmpIds
     * 方法功能:  根据人员id 解除关联角色
     * @author wangwk
     * @date 2023/9/2 10:01
     * @param ids
     * @return void
     */
    void deleteByEmpIds(List<Long> ids);

    /**
     * deleteByEmpIdsRoleId
     * 方法功能: 根据人员id和角色id删除关联角色
     * @author CGD
     * @date 2023/9/16 15:26
     * @param empoIds
     * @param roleId
     * @return void
     */
    public void deleteByEmpIdsRoleId(List<Long> empoIds ,Long roleId);

    /**
     * deleteByRoleIds
     * 方法功能:  根据角色id删除角色关联
     * @author CGD
     * @date 2023/9/18 9:10
     * @param ids
     * @return void
     */
    void deleteByRoleIds(List<Long> ids);
}
