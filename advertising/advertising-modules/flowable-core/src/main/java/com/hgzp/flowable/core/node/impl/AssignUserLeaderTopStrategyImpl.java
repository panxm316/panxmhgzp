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
 * 连续多级主管
 */
@Component
public class AssignUserLeaderTopStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

        List<String> userIdList=new ArrayList<>();

        //去获取主管

        R<List<DeptDto>> r  = BizHttpUtil.queryParentDepListByUserId((rootUser.getId()));

        List<DeptDto> deptDtoList = r.getObj();

        //上级主管依次审批

        //第几级主管审批截止
        Integer level = node.getDeptLeaderLevel();


        if (CollUtil.isNotEmpty(deptDtoList)) {
            int index = 1;
            for (DeptDto deptDto : deptDtoList) {
                if (level != null && level < index) {
                    break;
                }
                List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                if(CollUtil.isNotEmpty(leaderUserIdList)){
                    for (String s : leaderUserIdList) {
                        if(userIdList.contains(s)){
                            continue;
                        }
                        userIdList.add(s);
                    }
                }
                index++;
            }
        }



        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.LEADER_TOP);

    }
}
