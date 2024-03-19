package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.utils.CommonUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来自表单
 */
@Component
public class AssignUserFormStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        List<String> assignList=new ArrayList<>();
        //表单值

        Object variable = variables.get(node.getFormUserId());
        if (variable == null) {

        } else if (StrUtil.isBlankIfStr(variable)) {

        } else {

            String jsonString = JSON.toJSONString(variable);
            List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);

            List<String> userIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

            assignList.addAll(userIdList);

        }
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FORM_USER);

    }
}
