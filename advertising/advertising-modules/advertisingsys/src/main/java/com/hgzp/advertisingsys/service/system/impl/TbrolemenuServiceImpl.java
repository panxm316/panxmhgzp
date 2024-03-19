package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertisingsys.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.model.Tbrolemenu;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.service.system.impl.BaseTbrolemenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbrolemenuServiceImpl extends BaseTbrolemenuServiceImpl implements TbrolemenuServiceI {


    @Autowired
    TbmenuMapper tbmenuMapper;


    @Override
    public Map<Long, List<Tbmenu>> getMenuListGroupByRoldId(List<Long> roleIdList){
        Map<Long, List<Tbmenu>> resultMap = new HashMap<>();

        List<Tbrolemenu> tbrolemenuList = this.lambdaQuery()
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list();

        List<Long> menuIdList = tbrolemenuList.stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());

        if (menuIdList.size() == 0) {
            roleIdList.forEach(id -> resultMap.put(id, Collections.EMPTY_LIST));
            return resultMap;
        }

        Map<Long, List<Tbmenu>> menuMap = new LambdaQueryChainWrapper<>(tbmenuMapper)
                .in(Tbmenu::getId, menuIdList)
                .list()
                .stream()
                .collect(Collectors.groupingBy(Tbmenu::getId));

        Map<Long, List<Tbrolemenu>> rolemenuByRoleId = tbrolemenuList.stream()
                .collect(Collectors.groupingBy(Tbrolemenu::getRoleid));

        for (Long roleId : roleIdList) {
            List<Tbrolemenu> tbrolemenus = rolemenuByRoleId.get(roleId);
            if(tbrolemenus == null){
                resultMap.put(roleId, Collections.EMPTY_LIST);
                continue;
            }
            List<Tbmenu> tbmenuList = tbrolemenus.stream()
                    .map(Tbrolemenu::getMenuid)
                    .map(menuMap::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            resultMap.put(roleId, tbmenuList);

        }
        return resultMap;

    }


}
