package com.hgzp.advertising.service.flow.impl;

import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.core.model.Tbflow;
import com.hgzp.mapper.flow.TbflowMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Tbflow getFlowTypeByKey(String flowKey) {
        Tbflow flowInfo = this.lambdaQuery().eq(Tbflow::getSkey, flowKey).eq(Tbflow::getBuse, 1).one();
        return flowInfo;
    }

    @Override
    public List<Tbflow> getFlowTypeList() {
        List<Tbflow> lsTbflow = this.lambdaQuery().eq(Tbflow::getBuse, 1).list();
        return lsTbflow;
    }
}