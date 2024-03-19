package com.hgzp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.core.model.Tbmenuaction;
import com.hgzp.mapper.system.TbmenuactionMapper;
import com.hgzp.service.system.BaseTbmenuactionServiceI;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单行为表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbmenuactionServiceImpl extends ServiceImpl<TbmenuactionMapper, Tbmenuaction> implements BaseTbmenuactionServiceI {



    @Override
    public List<Tbmenuaction> getMenuActionListByMenuIdList(List<Long> menuIdList) {
        List<Tbmenuaction> tbmenuactions = this.lambdaQuery()
                .in(Tbmenuaction::getMenuid, menuIdList)
                .orderByAsc(Tbmenuaction::getIsort)
                .list();
        return tbmenuactions;
    }


}
