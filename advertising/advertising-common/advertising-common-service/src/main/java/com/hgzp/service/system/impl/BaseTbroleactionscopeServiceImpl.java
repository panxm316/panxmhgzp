package com.hgzp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.core.model.Tbroleactionscope;
import com.hgzp.mapper.system.TbroleactionscopeMapper;
import com.hgzp.service.system.BaseTbroleactionscopeServiceI;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 角色范围表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbroleactionscopeServiceImpl extends ServiceImpl<TbroleactionscopeMapper, Tbroleactionscope> implements BaseTbroleactionscopeServiceI {


    @Override
    public List<Tbroleactionscope> getRoleActionScopeList(List<Long> roleActionIdList){
        if (roleActionIdList.size() == 0) {
            return Collections.emptyList();
        }

        List<Tbroleactionscope> list = this.lambdaQuery()
                .in(Tbroleactionscope::getRoleactionid, roleActionIdList)
                .list();

        return list;
    }
}
