package com.hgzp.advertising.service.system.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.system.TbroleactionServiceI;
import com.hgzp.core.model.Tbroleaction;
import com.hgzp.core.model.Tbrolemenu;
import com.hgzp.mapper.system.TbrolemenuMapper;
import com.hgzp.service.system.impl.BaseTbroleactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    TbrolemenuMapper tbrolemenuMapper;


    /**
     * 根据角色id获取按钮权限
     * 方法功能: 根据角色id获取按钮权限
     * @author wangwk
     * @date 2023/11/2 16:59
     * @param roleIdList
     * @return java.util.List<com.hgzp.core.model.Tbroleaction>
     */
    @Override
    public List<Tbroleaction> getTbroleactionByRoleIds(List<Long> roleIdList) {

        List<Long> tbrolemenuIdList = new LambdaQueryChainWrapper<>(tbrolemenuMapper)
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list()
                .stream().map(Tbrolemenu::getId).collect(Collectors.toList());
        return this.lambdaQuery()
                .in(Tbroleaction::getRolemenuid, tbrolemenuIdList)
                .list();
    }



}
