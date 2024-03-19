package com.hgzp.common.flowable.factory;


import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.common.flowable.dto.third.RoleDto;
import com.hgzp.common.flowable.dto.third.UserDto;

import java.util.List;

/**
 * 节点单个条件处理器
 */
public interface ApiStrategy {

    /**
     * 策略注册方法
     *
     * @param key
     */
    default void afterPropertiesSet(String key) {
        ApiStrategyFactory.register(key, this);
    }


    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    List<String> loadUserIdListByRoleIdList(List<String> roleIdList);


    /**
     * 获取所有的角色
     *
     * @return
     */
    List<RoleDto> loadAllRole();
    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     *
     * @param deptIdList 部门id集合
     * @return
     */
    List<String> loadUserIdListByDeptIdList(List<String> deptIdList);

    /**
     * 根据父级id获取所有的部门
     * 若父级id为空 则获取所有的部门
     * @param parentDeptId 父级id
     * @return
     */
    List<DeptDto> loadAllDept(String parentDeptId);

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    List<UserDto> loadUserByDept(String deptId);

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    UserDto getUser(String userId);

    /**
     * 根据名字搜索用户
     *
     * @param name
     * @return
     */
    List<UserDto> searchUser(String name);



    /**
     * 发送消息
     * @param messageDto
     */
    void sendMsg(MessageDto messageDto);

    /**
     * 根据用户id获取部门信息，包括单位管理员
     * @param userId
     * @return
     */
    DeptDto queryDeptByUserId(String userId);

    /**
     * 替换休假人员
     * @param userId
     * @return
     */
    default List<String> replaceHolidayUserId(List<String> userId){
        return null;
    }
}
