package com.hgzp.advertisingsys.service.system.impl;

import com.hgzp.advertisingsys.service.system.TbroleactionscopeServiceI;
import com.hgzp.core.model.Tbroleactionscope;
import com.hgzp.service.system.impl.BaseTbroleactionscopeServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色范围表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbroleactionscopeServiceImpl extends BaseTbroleactionscopeServiceImpl implements TbroleactionscopeServiceI {


    @Override
    public Map<Long, List<Tbroleactionscope>> getRoleActionScopeGroupByActionId(List<Long> roleActionIdList){
        if (roleActionIdList.size() == 0) {
            return Collections.emptyMap();
        }

        Map<Long, List<Tbroleactionscope>> listMap = this.lambdaQuery()
                .in(Tbroleactionscope::getRoleactionid, roleActionIdList)
                .list()
                .stream()
                .collect(Collectors.groupingBy(Tbroleactionscope::getRoleactionid));
        return listMap;
    }



}
