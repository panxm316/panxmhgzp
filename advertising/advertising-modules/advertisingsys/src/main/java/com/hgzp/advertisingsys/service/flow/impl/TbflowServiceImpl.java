package com.hgzp.advertisingsys.service.flow.impl;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.service.flow.TbflowServiceI;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.flow.TbflowMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 TbflowServiceImpl
 创建人：songly
 类描述：流程设置
 创建日期：2024/2/16 10:07
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TbflowServiceImpl extends MyServiceImpl<TbflowMapper, Tbflow> implements TbflowServiceI {
    @Resource
    private TbflowMapper flowMapper;

    @Override
    public IPage<Tbflow> getFlowPageList(Page<Tbflow> page, BaseQueryInfo baseQueryInfo) {
        LambdaQueryWrapper<Tbflow> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(baseQueryInfo.getQueryKey()), Tbflow::getSname, baseQueryInfo.getQueryKey())
                .orderByAsc(Tbflow::getIsort);
        IPage<Tbflow> lsResult = flowMapper.selectPage(page, lqw);
        return lsResult;
    }

    @Override
    public List<Tbflow> getFlowList(BaseQueryInfo baseQueryInfo) {
        List<Tbflow> lsFlowInfo = new LambdaQueryChainWrapper<>(flowMapper)
                .like(StringUtils.hasText(baseQueryInfo.getQueryKey()), Tbflow::getSname, baseQueryInfo.getQueryKey())
                .orderByAsc(Tbflow::getIsort)
                .list();
        return lsFlowInfo;
    }

    @Override
    public List<Tbflow> getFlowTypeValid() {
        List<Tbflow> lsFlowInfo = this.lambdaQuery().eq(Tbflow::getBuse, true).orderByAsc(Tbflow::getIsort).list();
        return lsFlowInfo;
    }

    @Override
    public boolean isExistFlow(Tbflow tbflow) {
        Long count = new LambdaQueryChainWrapper<>(flowMapper)
                .eq(Tbflow::getSname, tbflow.getSname())
                .ne(tbflow.getId() != null, Tbflow::getId, tbflow.getId())
                .count();
        return count > 0;
    }

    @Override
    public Tbflow getFlowTypeByKey(String flowKey) {
        Tbflow flowInfo = this.lambdaQuery().eq(Tbflow::getSkey, flowKey).one();
        return flowInfo;
    }
}