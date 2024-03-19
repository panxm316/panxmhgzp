package com.hgzp.flowable.core.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.flowable.core.node.AssignUserStrategy;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 指定具体用户
 */
@Component
public class AssignUserFixedStrategyImpl implements InitializingBean, AssignUserStrategy {
    @Override
    public List<String> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {


        //指定人员
        List<NodeUser> userDtoList = node.getNodeUserList();
        //用户id
        List<String> userIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            R<List<String>> r= BizHttpUtil.queryUserIdListByDepIdList(deptIdList);

            List<String> data = r.getObj();
            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }
        }
        return userIdList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSet(ProcessInstanceConstant.AssignedTypeClass.USER);

    }
}
