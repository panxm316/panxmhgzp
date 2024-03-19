package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.utils.CommonUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 来自表单部门
 */
@Component
public class AssignDeptFormStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        Set<String> assignList = new HashSet<>();
        //表单值

        Object variable = variables.get(node.getFormUserId());
        if (variable == null) {

        } else if (StrUtil.isBlankIfStr(variable)) {

        } else {

            String deptUserType = node.getDeptUserType();

            String jsonString = JSON.toJSONString(variable);
            List<NodeUser> nodeUserDtoList = CommonUtil.toArray(jsonString, NodeUser.class);
            List<String> deptIdList = nodeUserDtoList.stream().map(w -> String.valueOf(w.getId())).collect(Collectors.toList());

            if (!StrUtil.equals(deptUserType, ProcessInstanceConstant.AssignedTypeFormDeptUserTypeClass.ALL_USER)) {
                List<DeptDto> deptDtos = BizHttpUtil.queryDeptList(deptIdList);
                for (DeptDto deptDto : deptDtos) {
                    List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                    if (CollUtil.isNotEmpty(leaderUserIdList)) {
                        assignList.addAll(leaderUserIdList);
                    }
                }
            } else {
                List<String> userIdList = BizHttpUtil.queryUserIdListByDepIdList(deptIdList).getObj();
                assignList.addAll(userIdList);
            }


        }
        return new ArrayList<>(assignList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FORM_DEPT);

    }
}
