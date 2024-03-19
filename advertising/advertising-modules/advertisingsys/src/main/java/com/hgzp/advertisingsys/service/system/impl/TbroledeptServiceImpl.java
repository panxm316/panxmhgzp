package com.hgzp.advertisingsys.service.system.impl;



import com.hgzp.advertisingsys.service.system.TbroledeptServiceI;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbroledept;
import com.hgzp.mapper.system.TbdeptMapper;
import com.hgzp.service.system.impl.BaseTbroledeptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色部门表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbroledeptServiceImpl extends BaseTbroledeptServiceImpl implements TbroledeptServiceI {

    @Autowired
    TbdeptMapper tbdeptMapper;


    @Override
    public Map<Long, List<Tbdept>> getDeptListGroupByRoleIds(List<Long> roleIdList){
        Map<Long, List<Tbdept>> resultMap = new HashMap<>();

        List<Tbroledept> tbroledeptList = this.lambdaQuery()
                .in(Tbroledept::getRoleid, roleIdList)
                .list();
        List<Long> deptIdList = tbroledeptList
                .stream()
                .map(Tbroledept::getDeptid)
                .distinct()
                .collect(Collectors.toList());

        if(deptIdList.size() == 0){
            roleIdList.forEach(id -> resultMap.put(id, Collections.EMPTY_LIST));
            return resultMap;
        }

        Map<Long, List<Tbdept>> tbdeptListMap = tbdeptMapper.selectBatchIds(deptIdList).stream().collect(Collectors.groupingBy(Tbdept::getId));
        Map<Long, List<Tbroledept>> roleIdMap = tbroledeptList.stream().collect(Collectors.groupingBy(Tbroledept::getRoleid));

        for (Long roleId : roleIdList) {
            List<Tbroledept> tbroledepts = roleIdMap.get(roleId);
            if(tbroledepts == null){
                resultMap.put(roleId, Collections.EMPTY_LIST);
                continue;
            }
            List<Tbdept> tbdeptList = tbroledepts.stream()
                    .map(Tbroledept::getDeptid)
                    .map(tbdeptListMap::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            resultMap.put(roleId, tbdeptList);
        }
        return resultMap;
    }




}
