package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.dto.third.DeptDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指定主管
 */
@Component
public class AssignUserLeaderStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        List<String> userIdList=new ArrayList<>();

        //指定主管审批
        //第几级主管审批
//        Integer level = node.getDeptLeaderLevel();

        //去获取主管


        R<DeptDto> r = BizHttpUtil.queryDeptByUserId((rootUser.getId()));

        DeptDto deptDto = r.getObj();
        if (deptDto != null) {
            List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
            if(CollUtil.isNotEmpty(leaderUserIdList)){
                for (String s : leaderUserIdList) {
                    if(userIdList.contains(s)){
                        continue;
                    }
                    userIdList.add(s);
                }
            }

        }

        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.LEADER);

    }
}
