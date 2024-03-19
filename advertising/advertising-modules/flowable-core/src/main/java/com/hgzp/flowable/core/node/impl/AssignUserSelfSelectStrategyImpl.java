package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发起人自选
 */
@Component
@Slf4j
public class AssignUserSelfSelectStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        List<String> assignList = new ArrayList<>();

        Object variable = variables.get(StrUtil.format("{}_assignee_select", node.getId()));
        log.info("{}-发起人自选参数:{}", node.getNodeName(), variable);
        if (variable == null) {
            return assignList;
        }
        List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

        List<String> collect = nodeUserDtos.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

        assignList.addAll(collect);
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT);

    }
}
