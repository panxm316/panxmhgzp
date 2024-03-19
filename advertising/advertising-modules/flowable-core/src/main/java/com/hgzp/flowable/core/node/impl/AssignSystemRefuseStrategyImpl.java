package com.hgzp.flowable.core.node.impl;

import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统自动拒绝
 */
@Component
public class AssignSystemRefuseStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
        return new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.SYSTEM_REFUSE);

    }
}
