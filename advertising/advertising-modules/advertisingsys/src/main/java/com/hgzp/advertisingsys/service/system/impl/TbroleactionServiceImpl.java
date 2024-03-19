package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertisingsys.service.system.TbroleactionServiceI;
import com.hgzp.core.model.Tbroleaction;
import com.hgzp.service.system.impl.BaseTbroleactionServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限行为表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbroleactionServiceImpl extends BaseTbroleactionServiceImpl implements TbroleactionServiceI {


    @Override
    public void deleteRoleActionsByRoleMenuId(List<Long> roleMenuIds){
        LambdaQueryWrapper<Tbroleaction> queryWrapper = Wrappers.lambdaQuery(Tbroleaction.class)
                .in(Tbroleaction::getRolemenuid,roleMenuIds);
        this.remove(queryWrapper);
    }


    @Override
    public Map<Long, List<Tbroleaction>> getRoleActionListGroupByRoleMenuId(List<Long> roleMenuIds){
        Map<Long, List<Tbroleaction>> listMap = getRoleActionListByRoleMenuId(roleMenuIds)
                .stream()
                .collect(Collectors.groupingBy(Tbroleaction::getRolemenuid));
        return listMap;
    }

    @Override
    public List<Tbroleaction> getRoleActionListByRoleMenuId(List<Long> roleMenuIds){
        return this.lambdaQuery()
                .in(Tbroleaction::getRolemenuid, roleMenuIds)
                .list();
    }



}
