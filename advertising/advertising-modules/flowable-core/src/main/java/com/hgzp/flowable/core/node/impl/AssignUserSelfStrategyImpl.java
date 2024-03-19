package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 发起人自己
 */
@Component
public class AssignUserSelfStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        List<String> userIdList = CollUtil.newArrayList(String.valueOf(rootUser.getId()));
        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.SELF);

    }
}
