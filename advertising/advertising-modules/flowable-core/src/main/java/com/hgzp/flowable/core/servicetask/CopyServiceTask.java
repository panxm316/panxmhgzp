package com.hgzp.flowable.core.servicetask;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.flowable.core.node.NodeDataStoreFactory;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.dto.ProcessInstanceCopyDto;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抄送任务处理器--java服务任务
 */
public class CopyServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        ExecutionEntityImpl entity= (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();

        Node node = NodeDataStoreFactory.getInstance().getNode(flowId, nodeId);



        //指定人员
        List<NodeUser> userDtoList = node.getNodeUserList();
        //用户id
        List<String> userIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = userDtoList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toStr(w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            R<List<String>> r = BizHttpUtil.queryUserIdListByDepIdList(deptIdList);

            List<String> data = r.getObj();
            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }
        }


        //发起人
        Object rootUserObj = execution.getVariable("root");
        NodeUser rootUser = JSON.parseArray(JSON.toJSONString(rootUserObj), NodeUser.class).get(0);

        Map<String, Object> variables = execution.getVariables();

        for (String userIds : userIdList) {
            //发送抄送任务
            ProcessInstanceCopyDto processInstanceCopyDto = new ProcessInstanceCopyDto();
            processInstanceCopyDto.setNodeTime(new Date());
            processInstanceCopyDto.setStartUserId((rootUser.getId()));
            processInstanceCopyDto.setFlowId(flowId);
            processInstanceCopyDto.setProcessInstanceId(execution.getProcessInstanceId());
            processInstanceCopyDto.setNodeId(nodeId);
            processInstanceCopyDto.setNodeName(node.getNodeName());
            processInstanceCopyDto.setFormData(JSON.toJSONString(variables));
            processInstanceCopyDto.setUserId((userIds));

            BizHttpUtil.saveCC(processInstanceCopyDto);
        }


    }
}
