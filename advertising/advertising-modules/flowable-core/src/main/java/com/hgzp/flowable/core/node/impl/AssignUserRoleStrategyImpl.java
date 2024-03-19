package com.hgzp.flowable.core.node.impl;

import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来自角色
 */
@Component
public class AssignUserRoleStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        List<String> assignList=new ArrayList<>();

        //角色

        List<NodeUser> nodeUserList = node.getNodeUserList();

        List<String> roleIdList = nodeUserList.stream().map(w -> w.getId()).collect(Collectors.toList());


        R<List<String>> r = BizHttpUtil.queryUserIdListByRoleIdList(roleIdList);

        List<String> data = r.getObj();


        assignList.addAll(data);
        return assignList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.ROLE);

    }
}
