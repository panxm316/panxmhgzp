package com.hgzp.advertisingsys.service.flow.impl;


import cn.hutool.core.collection.CollUtil;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import com.hgzp.advertisingsys.service.system.TbemployServiceI;
import com.hgzp.advertisingsys.service.system.TbemployroleServiceI;
import com.hgzp.advertisingsys.service.system.TbroleServiceI;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.common.flowable.dto.third.RoleDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategy;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.model.Tbrole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocalApi implements ApiStrategy, InitializingBean {

    @Resource
    private TbemployroleServiceI tbemployroleService;
    @Resource
    private TbroleServiceI roleService;
    @Resource
    private TbdeptServiceI deptService;
    @Resource
    private TbemployServiceI tbemployService;

//    @Resource
//    private IMessageService messageService;

    /**
     * 根据角色id集合获取拥有该角色的用户id集合
     *
     * @param roleIdList 角色id集合
     * @return
     */
    @Override
    public List<String> loadUserIdListByRoleIdList(List<String> roleIdList) {

        List<Long> roleIdLList = roleIdList.stream().map(Long::parseLong).collect(Collectors.toList());

        List<Long> empIdList = tbemployroleService.getEmployIdByRoleId(roleIdLList);
        List<String> list = empIdList.stream().map(String::valueOf).distinct().collect(Collectors.toList());

        return list;
    }

    /**
     * 获取所有的角色
     *
     * @return
     */
    @Override
    public List<RoleDto> loadAllRole() {
        List<Tbrole> tbroleList = roleService.lambdaQuery()
                .eq(Tbrole::getBuse, true)
                .eq(Tbrole::getBselfrole, false)
                .list();

        List<RoleDto> roleDtos = tbroleList.stream().map(r -> {

            return RoleDto.builder()
                    .id(String.valueOf(r.getId()))
                    .name(r.getSname())
                    .key(r.getSname())
                    .status(r.getBuse() ? 1 : 0)
                    .build();

        }).collect(Collectors.toList());

        return roleDtos;

    }

    /**
     * 根据部门id集合获取该部门下的用户id集合
     * 注意：直属部门，不包括子级及以下部门
     *
     * @param deptIdList 部门id集合
     * @return
     */
    @Override
    public List<String> loadUserIdListByDeptIdList(List<String> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)) {
            return new ArrayList<>();
        }
        List<Tbemploy> userList = tbemployService.lambdaQuery()
                .in(Tbemploy::getDeptid, deptIdList)
                .eq(Tbemploy::getBuse, true)
                .list();
        return userList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());
    }

    /**
     * 获取所有的部门
     *
     * @return
     */
    @Override
    public List<DeptDto> loadAllDept(String parentDeptId) {

        List<Tbdept> tbdeptList = deptService.lambdaQuery()
                .eq(parentDeptId != null, Tbdept::getParentid, parentDeptId)
                .eq(Tbdept::getBuse, true)
                .list();

        List<DeptDto> deptDtoList = new ArrayList<>();
        for (Tbdept dept : tbdeptList) {

//            List<String> deptLeaderIdList = tbemployService.getDeptLeaderIdList(dept.getId()).stream().map(String::valueOf).collect(Collectors.toList());


            DeptDto deptDto = DeptDto.builder()
                    .id(dept.getId().toString())
                    .name(dept.getSdeptname())
                    .parentId(dept.getParentid() == null ? "0" : dept.getParentid().toString())
//                    .leaderUserIdList(deptLeaderIdList)
                    .status(dept.getBuse() ? 1 : 0)
                    .build();
            deptDtoList.add(deptDto);
        }

        return deptDtoList;

    }

    /**
     * 根据部门获取部门下的用户集合
     *
     * @param deptId 部门id
     * @return
     */
    @Override
    public List<UserDto> loadUserByDept(String deptId) {
        List<Tbemploy> tbemployList = tbemployService.lambdaQuery()
                .eq(Tbemploy::getDeptid, deptId)
                .eq(Tbemploy::getBuse, true)
                .list();
        List<UserDto> userDtoList = tbemployList.stream()
                .map(e -> {
                    return UserDto.builder()
                            .id(e.getId().toString())
                            .name(e.getSusername())
                            .avatarUrl(e.getSimg())
                            .deptId(e.getDeptid().toString())
                            .status(e.getBuse() ? 1 : 0)
                            .build();
                }).collect(Collectors.toList());

        return userDtoList;

    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserDto getUser(String userId) {
        Tbemploy tbemploy = tbemployService.getById(userId);
        UserDto userDto = UserDto.builder()
                .id(tbemploy.getId().toString())
                .name(tbemploy.getSusername())
                .avatarUrl(tbemploy.getSimg())
                .deptId(tbemploy.getDeptid().toString())
                .status(tbemploy.getBuse() ? 1 : 0)
                .build();
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String name) {
        List<Tbemploy> tbemployList = tbemployService.lambdaQuery()
                .like(Tbemploy::getSusername, name)
                .list();

        List<UserDto> userDtoList = tbemployList.stream()
                .map(e -> {
                    return UserDto.builder()
                            .id(e.getId().toString())
                            .name(e.getSusername())
                            .avatarUrl(e.getSimg())
                            .deptId(e.getDeptid().toString())
                            .status(e.getBuse() ? 1 : 0)
                            .build();
                }).collect(Collectors.toList());

        return userDtoList;

    }

    /**
     * 发送消息
     *
     * @param messageDto
     */
    @Override
    public void sendMsg(MessageDto messageDto) {
//        messageService.saveMessage(messageDto);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet("local");
    }

    @Override
    public DeptDto queryDeptByUserId(String userId) {
        Tbemploy tbemploy = tbemployService.getById(userId);
        if(tbemploy != null && tbemploy.getDeptid() != null){
            Tbdept dept = deptService.getById(tbemploy.getDeptid());
//            List<String> deptLeaderIdList = tbemployService.getDeptLeaderIdList(tbemploy.getDeptid())
//                    .stream().map(String::valueOf).collect(Collectors.toList());


            DeptDto deptDto = DeptDto.builder()
                    .id(dept.getId().toString())
                    .name(dept.getSdeptname())
                    .parentId(dept.getParentid() == null ? "0" : dept.getParentid().toString())
//                    .leaderUserIdList(deptLeaderIdList)
                    .status(dept.getBuse() ? 1 : 0)
                    .build();
            return deptDto;
        }
        return null;
    }


}
