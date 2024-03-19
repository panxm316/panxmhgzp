package com.hgzp.flowable.core.controller;

import com.hgzp.common.flowable.dto.IndexPageStatistics;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.VariableQueryParamDto;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequestMapping("process-instance")
public class ProcessInstanceController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;

    @Resource
    private ManagementService managementService;


    @Resource
    private RuntimeService runtimeService;

    /**
     * 查询统计数量
     * @param userId
     * @return
     */
    @GetMapping("querySimpleData")
    public R<IndexPageStatistics> querySimpleData(String userId){
        TaskQuery taskQuery = taskService.createTaskQuery();

        //待办数量
        long pendingNum = taskQuery.taskAssignee(String.valueOf(userId)).count();
        //已完成任务
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

        long completedNum = historicActivityInstanceQuery.taskAssignee(String.valueOf(userId)).finished().count();


        IndexPageStatistics indexPageStatistics = IndexPageStatistics.builder().pendingNum(pendingNum).completedNum(completedNum).build();

        return R.success(indexPageStatistics);
    }

    /**
     * 根据历史变量查询流程实例id
     * 方法功能: 根据历史变量查询流程实例id
     * @author wangwk
     * @date 2023/11/10 15:20
     * @param variableName
     * @param variableValue
     * @return com.hgzp.common.flowable.dto.R
     */
    @GetMapping("getProcessInstanceIdsByVar")
    public R<List<String>> getProcessInstanceIdsByVar(String variableName, String variableValue) {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .variableValueEquals(variableName, variableValue)
                .list();
        List<String> processInstanceIdList = list.stream().map(HistoricVariableInstance::getProcessInstanceId).collect(Collectors.toList());
        return R.success(processInstanceIdList);
    }


    /**
     * 查询变量
     *
     * @param paramDto
     * @return
     */
    @PostMapping("queryVariables")
    public R<Map<String, Object>> queryVariables(@RequestBody VariableQueryParamDto paramDto) {



        Map<String, Object> variables = runtimeService.getVariables(paramDto.getExecutionId());

        return R.success(variables);


    }


}
