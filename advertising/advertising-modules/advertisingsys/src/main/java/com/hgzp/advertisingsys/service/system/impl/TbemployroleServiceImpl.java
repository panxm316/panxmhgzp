package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertisingsys.service.system.TbemployroleServiceI;
import com.hgzp.core.model.Tbemployrole;
import com.hgzp.mapper.system.TbemployroleMapper;
import com.hgzp.service.system.impl.BaseTbemployroleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
@Transactional(rollbackFor = Exception.class)
public class TbemployroleServiceImpl extends BaseTbemployroleServiceImpl implements TbemployroleServiceI {

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
    public void deleteByEmpIds(List<Long> ids){
        LambdaQueryWrapper<Tbemployrole> queryWrapper = Wrappers.lambdaQuery(Tbemployrole.class)
                .in(Tbemployrole::getEmployid, ids);
        this.remove(queryWrapper);

    }

    @Override
    public void deleteByEmpIdsRoleId(List<Long> empoIds ,Long roleId){
        LambdaQueryWrapper<Tbemployrole> queryWrapper = Wrappers.lambdaQuery(Tbemployrole.class)
                .in(Tbemployrole::getEmployid, empoIds)
                .eq(Tbemployrole::getRoleid,roleId);
        this.remove(queryWrapper);

    }

    @Override
    public void deleteByRoleIds(List<Long> ids){
        LambdaQueryWrapper<Tbemployrole> queryWrapper = Wrappers.lambdaQuery(Tbemployrole.class)
                .in(Tbemployrole::getRoleid, ids);
        this.remove(queryWrapper);

    }

}
