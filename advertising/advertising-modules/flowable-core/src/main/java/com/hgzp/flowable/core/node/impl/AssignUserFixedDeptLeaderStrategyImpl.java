package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.dto.third.DeptDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 指定部门主管
 *
 */
@Component
public class AssignUserFixedDeptLeaderStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        Set<String> userIdList = new HashSet<>();


        List<NodeUser> userDtoList = node.getNodeUserList();
        //部门id
        List<String> deptIdList = userDtoList.stream()
                .filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
                .map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        //去获取主管


        if (CollUtil.isNotEmpty(deptIdList)) {

            List<DeptDto> deptDtoList = BizHttpUtil.queryDeptList(deptIdList);
            for (DeptDto deptDto : deptDtoList) {
                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();

                if (CollUtil.isNotEmpty(leaderUserIdList)) {
                    userIdList.addAll(leaderUserIdList);
                }
            }

        }

        return new ArrayList<>(userIdList);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.FIXED_DEPT_LEADER);

    }
}
