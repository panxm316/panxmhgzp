package com.hgzp.advertising.service.flow.impl;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.flow.TbflowconditionServiceI;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.mapper.flow.TbflowconditionMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 TbflowconditionServiceImpl
 创建人：songly
 类描述：获取工作流条件（分组获取）
 创建日期：2023/11/6 12:52
 */
@Service
public class TbflowconditionServiceImpl extends MyServiceImpl<TbflowconditionMapper, Tbflowcondition> implements TbflowconditionServiceI {
    @Resource
    private TbflowconditionMapper flowMapper;
    @Override
    public List<Tbflowcondition> getFlowConditionList(String sflowtype) {
        List<Tbflowcondition> lsFlowInfo = new LambdaQueryChainWrapper<>(flowMapper)
                .eq(Tbflowcondition::getBuse, true)
                .eq(ObjectUtil.isNotNull(sflowtype), Tbflowcondition::getSflowtype, sflowtype)
                .orderByAsc(Tbflowcondition::getIsort)
                .list();
        return lsFlowInfo;
    }
}