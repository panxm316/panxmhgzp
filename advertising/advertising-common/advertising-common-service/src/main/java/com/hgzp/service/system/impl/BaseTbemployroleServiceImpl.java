package com.hgzp.service.system.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.core.model.Tbemployrole;
import com.hgzp.mapper.system.TbemployroleMapper;
import com.hgzp.service.system.BaseTbemployroleServiceI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 人员角色表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbemployroleServiceImpl extends ServiceImpl<TbemployroleMapper, Tbemployrole> implements BaseTbemployroleServiceI {

    @Autowired
    TbemployroleMapper tbemployroleMapper;


    @Override
    public List<Long> getRoldIdListByUserId(Long userId) {
        List<Long> roleIdlist = new LambdaQueryChainWrapper<>(tbemployroleMapper)
                .select(Tbemployrole::getRoleid)
                .eq(Tbemployrole::getEmployid, userId)
                .list()
                .stream()
                .map(Tbemployrole::getRoleid)
                .collect(Collectors.toList());

        return roleIdlist;
    }

    @Override
    public List<Long> getEmployIdByRoleId(String roleIds) {
        List<Long> roleIdList = Arrays.stream(roleIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return getEmployIdByRoleId(roleIdList);
    }

    @Override
    public List<Long> getEmployIdByRoleId(List<Long> roleIdList) {
        List<Tbemployrole> employRoleList = this.lambdaQuery()
                .in(Tbemployrole::getRoleid, roleIdList)
                .list();
        List<Long> employIdCollect = employRoleList.stream()
                .map(Tbemployrole::getEmployid)
                .collect(Collectors.toList());
        return employIdCollect;
    }

}
