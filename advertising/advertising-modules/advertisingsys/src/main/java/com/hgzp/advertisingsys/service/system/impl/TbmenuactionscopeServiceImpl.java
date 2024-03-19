package com.hgzp.advertisingsys.service.system.impl;

import com.hgzp.advertisingsys.service.system.TbmenuactionscopeServiceI;
import com.hgzp.core.model.Tbmenuactionscope;
import com.hgzp.core.model.Tbscope;
import com.hgzp.mapper.system.TbscopeMapper;
import com.hgzp.service.system.impl.BaseTbmenuactionscopeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单范围关联表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmenuactionscopeServiceImpl extends BaseTbmenuactionscopeServiceImpl implements TbmenuactionscopeServiceI {

    @Autowired
    TbscopeMapper tbscopeMapper;

    @Override
    public Map<Long, List<Tbscope>> getScopeGroupByMenuActionId(List<Long> menuActionIdList){
        Map<Long, List<Tbscope>> resultMap = new HashMap<>();

        if(menuActionIdList.size() == 0){
            return resultMap;
        }

        List<Tbmenuactionscope> tbmenuactionscopeList = this.lambdaQuery()
                .in(Tbmenuactionscope::getMenuactionid, menuActionIdList)
                .list();

        List<Long> scopeIdList = tbmenuactionscopeList.stream()
                .map(Tbmenuactionscope::getScopeid)
                .distinct()
                .collect(Collectors.toList());
        if(scopeIdList.size() == 0){
            menuActionIdList.forEach(id -> resultMap.put(id, Collections.emptyList()));
            return resultMap;
        }
        Map<Long, List<Tbscope>> tbscopeMap = tbscopeMapper.selectBatchIds(scopeIdList).stream().collect(Collectors.groupingBy(Tbscope::getId));

        Map<Long, List<Tbmenuactionscope>> actionscopeMap = tbmenuactionscopeList.stream()
                .collect(Collectors.groupingBy(Tbmenuactionscope::getMenuactionid));

        for (Long actionId : menuActionIdList) {
            List<Tbmenuactionscope> tbmenuactionscopes = actionscopeMap.get(actionId);
            if(tbmenuactionscopes == null){
                resultMap.put(actionId, Collections.EMPTY_LIST);
                continue;
            }
            List<Tbscope> collect = tbmenuactionscopes.stream()
                    .map(Tbmenuactionscope::getScopeid)
                    .map(tbscopeMap::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            resultMap.put(actionId, collect);
        }
        return resultMap;


    }



}
