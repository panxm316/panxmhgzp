package com.hgzp.service.system;

import com.hgzp.core.model.Tbemployrole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 人员角色表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbemployroleServiceI extends IService<Tbemployrole> {


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
     * getEmployIdByRoleId
     * 方法功能:  根据角色获取角色上的人员Id
     *
     * @param roleIds id字符串多个用逗号拼接
     * @return java.util.List<java.lang.Long>
     * @author CGD
     * @date 2023/9/15 16:46
     */
    List<Long> getEmployIdByRoleId(String roleIds);

    /**
     * getEmployIdByRoleId
     * 方法功能:  根据角色获取角色上的人员Id
     *
     * @param roleIdList id集合
     * @return java.util.List<java.lang.Long>
     * @author CGD
     * @date 2023/9/15 16:46
     */
    List<Long> getEmployIdByRoleId(List<Long> roleIdList);
}
