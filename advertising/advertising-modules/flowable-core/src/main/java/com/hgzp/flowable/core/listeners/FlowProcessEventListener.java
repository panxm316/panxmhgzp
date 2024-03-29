package com.hgzp.flowable.core.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.common.flowable.dto.ProcessInstanceAssignUserRecordParamDto;
import com.hgzp.common.flowable.dto.ProcessInstanceNodeRecordParamDto;
import com.hgzp.common.flowable.dto.ProcessInstanceParamDto;
import com.hgzp.common.flowable.dto.ProcessInstanceRecordParamDto;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.flowable.core.node.NodeDataStoreFactory;
import com.hgzp.flowable.core.utils.BizHttpUtil;
import com.hgzp.flowable.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.task.api.DelegationState;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.flowable.variable.api.event.FlowableVariableEvent;

import java.util.Map;

import static com.hgzp.common.flowable.constants.ProcessInstanceConstant.VariableKey.APPROVE_RESULT;


/**
 * 流程监听器
 */
@Slf4j
public class FlowProcessEventListener implements FlowableEventListener {

    /**
     * Called when an event has been fired
     *
     * @param event the event
     */
    @Override
    public void onEvent(FlowableEvent event) {
        log.debug("===================FlowProcessEventListener============================");
        log.debug("分支监听器 类型={} class={}", event.getType(), event.getClass().getCanonicalName());

        if (event.getType().toString().equals(FlowableEngineEventType.TASK_CREATED.toString())) {


            //任务被创建了
            FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;

            TaskEntity taskEntity = (TaskEntity) flowableEntityEvent.getEntity();


            String processInstanceId = taskEntity.getProcessInstanceId();
            String processDefinitionId = taskEntity.getProcessDefinitionId();

            String flowId = NodeUtil.getFlowId(processDefinitionId);

            String taskId = taskEntity.getId();
            String assignee = taskEntity.getAssignee();

            log.debug("TASK_CREATED  节点id：{} getAssignee:{}", taskEntity.getId(), taskEntity.getAssignee());
            //发送消息
            MessageDto messageDto = MessageDto.builder()
                    .userId(assignee)
                    .flowId(flowId)
                    .processInstanceId(processInstanceId)

                    .uniqueId(taskId)
                    .param(JSON.toJSONString(taskEntity.getVariables()))

                    .type(MessageTypeEnum.TODO_TASK.getType())
                    .readed(false).build();
            BizHttpUtil.saveMessage(messageDto);
        }
        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_STARTED.toString())) {
            //节点开始执行
            //org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl
            FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug(" ACTIVITY_STARTED  节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            Node node = NodeDataStoreFactory.getInstance().getNode(flowId, activityId);
            log.debug(" node:{}", node);


            ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
            processInstanceNodeRecordParamDto.setFlowId(flowId);
            processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processInstanceNodeRecordParamDto.setNodeId(activityId);
            if (node!=null) {

                processInstanceNodeRecordParamDto.setNodeType(String.valueOf(node.getType()));

            }
            processInstanceNodeRecordParamDto.setNodeName(activityName);
            processInstanceNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            BizHttpUtil.startNodeEvent(processInstanceNodeRecordParamDto);

        }

        if (
                event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
            ||
                event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())
        ) {
            //多实例任务
            FlowableMultiInstanceActivityCompletedEventImpl flowableActivityEvent = (FlowableMultiInstanceActivityCompletedEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug("节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
            processInstanceNodeRecordParamDto.setFlowId(flowId);
            processInstanceNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processInstanceNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processInstanceNodeRecordParamDto.setNodeName(activityName);

            BizHttpUtil.endNodeEvent(processInstanceNodeRecordParamDto);
        }
        if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_COMPLETED.toString())) {
            //节点完成执行

            FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
            String activityId = flowableActivityEvent.getActivityId();
            String activityName = flowableActivityEvent.getActivityName();
            log.debug("ACTIVITY_COMPLETED 节点id：{} 名字:{}", activityId, activityName);


            String processInstanceId = flowableActivityEvent.getProcessInstanceId();

            String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto = new ProcessInstanceNodeRecordParamDto();
            processInstanceNodeRecordParamDto.setFlowId(flowId);
            processInstanceNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
            processInstanceNodeRecordParamDto.setProcessInstanceId(processInstanceId);
//            processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
            processInstanceNodeRecordParamDto.setNodeId(activityId);
//            processNodeRecordParamDto.setNodeType(nodeDto.getType());
            processInstanceNodeRecordParamDto.setNodeName(activityName);

            BizHttpUtil.endNodeEvent(processInstanceNodeRecordParamDto);

        }

        if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_UPDATED.toString())) {
            //变量变化了
            FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
            log.debug("变量[{}]变化了:{}  ",
                    flowableVariableEvent.getVariableName(),
                    flowableVariableEvent.getVariableValue()
            );
        }

        if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_CREATED.toString())) {
            //变量创建了
            FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
            log.debug("变量[{}]创建了:{} ",
                    flowableVariableEvent.getVariableName(),
                    flowableVariableEvent.getVariableValue()
            );
        }
        if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_DELETED.toString())) {
            //变量删除了
            FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
            log.debug("变量[{}]删除了:{} ",
                    flowableVariableEvent.getVariableName(),
                    flowableVariableEvent.getVariableValue()
            );
        }
        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
            //流程开完成
            FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
            DelegateExecution execution = e.getExecution();
            String processInstanceId = e.getProcessInstanceId();
            ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();
            String flowId = entity.getProcessDefinitionKey();

            Boolean finalResult = execution.getVariable(StrUtil.format("{}_{}", flowId, APPROVE_RESULT), Boolean.class);
            Map<String, Object> variables = execution.getVariables();


            ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
            processInstanceParamDto.setProcessInstanceId(processInstanceId);
            processInstanceParamDto.setResult(finalResult);
            processInstanceParamDto.setParamMap(variables);
            BizHttpUtil.endProcessEvent(processInstanceParamDto);

        }

        if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {

            TaskService taskService = SpringUtil.getBean(TaskService.class);

            //任务完成
            FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
            TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
            //执行人id
            String assignee = task.getAssignee();

            //nodeid
            String taskDefinitionKey = task.getTaskDefinitionKey();

            //实例id
            String processInstanceId = task.getProcessInstanceId();

            String processDefinitionId = task.getProcessDefinitionId();

            log.debug("TASK_COMPLETED  节点id：{} assignee:{}, params：{}", task.getId(), assignee, task.getVariables());


            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);
            ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
            processInstanceAssignUserRecordParamDto.setFlowId(flowId);
            processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
            processInstanceAssignUserRecordParamDto.setData(JSON.toJSONString(taskService.getVariables(task.getId())));
            processInstanceAssignUserRecordParamDto.setLocalData(JSON.toJSONString(taskService.getVariablesLocal(task.getId())));
            processInstanceAssignUserRecordParamDto.setNodeId(taskDefinitionKey);
            processInstanceAssignUserRecordParamDto.setUserId((assignee));
            processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
            processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
            processInstanceAssignUserRecordParamDto.setTaskType("COMPLETE");
            processInstanceAssignUserRecordParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
            processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

            BizHttpUtil.taskEndEvent(processInstanceAssignUserRecordParamDto);

        }
        if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {
            //任务被指派了人员
            FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
            TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
            //执行人id
            String assignee = task.getAssignee();
            //任务拥有者
            String owner = task.getOwner();
            //
            String delegationStateString = task.getDelegationStateString();


            //nodeid
            String taskDefinitionKey = task.getTaskDefinitionKey();

            //实例id
            String processInstanceId = task.getProcessInstanceId();

            String processDefinitionId = task.getProcessDefinitionId();

            log.debug("TASK_ASSIGNED  节点id：{} assignee:{}, params：{}", task.getId(), assignee, task.getVariables());

            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);
            ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto = new ProcessInstanceAssignUserRecordParamDto();
            processInstanceAssignUserRecordParamDto.setFlowId(flowId);
            processInstanceAssignUserRecordParamDto.setProcessInstanceId(processInstanceId);
//        processNodeRecordAssignUserParamDto.setData();
            processInstanceAssignUserRecordParamDto.setNodeId(taskDefinitionKey);
            processInstanceAssignUserRecordParamDto.setUserId((assignee));
            processInstanceAssignUserRecordParamDto.setTaskId(task.getId());
            processInstanceAssignUserRecordParamDto.setNodeName(task.getName());
            processInstanceAssignUserRecordParamDto.setTaskType(StrUtil.equals(DelegationState.PENDING.toString(), delegationStateString) ? "DELEGATION" : (StrUtil.equals(DelegationState.RESOLVED.toString(), delegationStateString) ? "RESOLVED" : ""));
            processInstanceAssignUserRecordParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
            processInstanceAssignUserRecordParamDto.setExecutionId(task.getExecutionId());

            BizHttpUtil.startAssignUser(processInstanceAssignUserRecordParamDto);


        }

        if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_STARTED.toString())) {
            //流程开始了
            FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

            ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
            DelegateExecution execution = flowableProcessStartedEvent.getExecution();
            String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();
            {
                //上级实例id
                String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();

                String flowId = entity.getProcessDefinitionKey();

                Object variable = execution.getVariable(
                        "root");
                String startUserId =  (JSON.parseArray(JSON.toJSONString(variable), NodeUser.class).get(0).getId());
                Map<String, Object> variables = execution.getVariables();

                log.debug("PROCESS_STARTED  processInstanceId：{} getProcessDefinitionKey:{}, params：{}", processInstanceId, flowId, entity.getVariables());

                ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
                processInstanceRecordParamDto.setUserId(startUserId);
                processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
                processInstanceRecordParamDto.setFlowId(flowId);
                processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
                processInstanceRecordParamDto.setFormData(JSON.toJSONString(variables));
                BizHttpUtil.createProcessEvent(processInstanceRecordParamDto);
            }



        }
    }

    /**
     * @return whether or not the current operation should fail when this listeners execution throws an exception.
     */
    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * @return Returns whether this event listener fires immediately when the event occurs or
     * on a transaction lifecycle event (before/after commit or rollback).
     */
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    /**
     * @return if non-null, indicates the point in the lifecycle of the current transaction when the event should be fired.
     */
    @Override
    public String getOnTransaction() {
        return null;
    }
}
