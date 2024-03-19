package com.hgzp.flowable.core.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hgzp.flowable.core.utils.ModelUtil;
import com.hgzp.common.flowable.config.NotWriteLogAnno;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.flowable.core.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流控制器
 * 负责流程创建编辑发起等功能
 */
@RestController
@Slf4j
@RequestMapping("flow")
public class FlowController {

    @Autowired
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;


    @Resource
    private RuntimeService runtimeService;


    @PostMapping("create")
    public R<String> create(@RequestBody Node nodeDto, String userId) {
        String flowId = "p" + RandomUtil.randomString(9) + StrUtil.fillBefore(userId, '0', 10);
        log.info("flowId={}", flowId);
        BpmnModel bpmnModel = ModelUtil.buildBpmnModel(nodeDto, "测试1", flowId);
        {
            byte[] bpmnBytess = new BpmnXMLConverter().convertToXML(bpmnModel);
            // ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytess);
            String filename = "/tmp/flowable-deployment/" + flowId + ".bpmn20.xml";
            log.debug("部署时的模型文件：{}", filename);
            FileUtil.writeBytes(bpmnBytess, filename);
        }
        repositoryService.createDeployment()
                .addBpmnModel(StrUtil.format("{}.bpmn20.xml", "test1"), bpmnModel).deploy();


        return R.success(flowId);
    }

    /**
     * 启动
     *
     * @param processInstanceParamDto
     * @return
     */
    @PostMapping("/start")
    public R<String> start(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
        Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceParamDto.getFlowId(),
                processInstanceParamDto.getParamMap());

        String processInstanceId = processInstance.getProcessInstanceId();
        System.out.println("processInstanceId = " + processInstanceId);
        System.out.println("processInstance.getId = " + processInstance.getId());
        return R.success(processInstanceId);

    }


    @NotWriteLogAnno(all = false, printResultLog = false)
    @GetMapping("/showImg")
    public R<String> showImg(String procInsId) {


        String procDefId;
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(procInsId)
                    .singleResult();
            if(historicProcessInstance == null) {
                return R.success("");
            }
            procDefId = historicProcessInstance.getProcessDefinitionId();

        } else {
            procDefId = processInstance.getProcessDefinitionId();
        }

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);


        // 创建默认的流程图生成器
        DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
        // 生成图片的类型
        String imageType = "png";
        // 高亮节点集合
        List<String> highLightedActivities = new ArrayList<>();
        // 高亮连线集合
        List<String> highLightedFlows = new ArrayList<>();
        // 查询所有历史节点信息
        List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .list();


        // 遍历
        hisActInsList.forEach(historicActivityInstance -> {
            if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
                // 添加高亮连线
                highLightedFlows.add(historicActivityInstance.getActivityId());
            } else {
                // 添加高亮节点
                highLightedActivities.add(historicActivityInstance.getActivityId());
            }
        });
        // 节点字体
        String activityFontName = "宋体";
        // 连线标签字体
        String labelFontName = "微软雅黑";
        // 连线标签字体
        String annotationFontName = "宋体";
        // 类加载器
        ClassLoader customClassLoader = null;
        // 比例因子，默认即可
        double scaleFactor = 1.0d;
        // 不设置连线标签不会画
        boolean drawSequenceFlowNameWithNoLabelDI = true;

        BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
        bpmnAutoLayout.setTaskHeight(120);
        bpmnAutoLayout.setTaskWidth(120);
        bpmnAutoLayout.execute();
        // 生成图片
        InputStream inputStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel,
                imageType,
                highLightedActivities,
                highLightedFlows,
                activityFontName,
                labelFontName,
                annotationFontName,
                customClassLoader,
                scaleFactor,
                drawSequenceFlowNameWithNoLabelDI); // 获取输入流

        String content = Base64.encode(inputStream);
        return R.success(content);
    }

    @PostMapping("/approve")
    public void approve(String taskId, boolean approved) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        variables.put("ko", 10);
        variables.put("assigneeListSub", CollUtil.newArrayList("aa", "bb"));
        taskService.complete(taskId, variables);

    }

    /**
     * 终止流程
     *
     * @param taskParamDto
     * @return
     */
    @PostMapping("stopProcessInstance")
    public R stopProcessInstance(@RequestBody TaskParamDto taskParamDto) {

        List<String> processInstanceIdList = taskParamDto.getProcessInstanceIdList();
        for (String processInstanceId : processInstanceIdList) {
            ProcessInstance processInstance =
                    runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance != null) {
                List<Execution> executions = runtimeService.createExecutionQuery().parentId(processInstanceId).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                runtimeService.createChangeActivityStateBuilder().moveExecutionsToSingleActivityId(executionIds,
                        "end").changeState();
            }
        }


        return R.success();
    }

    /**
     * 查询用户已办任务
     *
     * @param taskQueryParamDto
     * @return
     */
    @PostMapping("/queryCompletedTask")
    public R<PageResultDto<TaskDto>> queryCompletedTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {
        HistoricActivityInstanceQuery historicActivityInstanceQuery =
                historyService.createHistoricActivityInstanceQuery();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateStart = formatter.parse(taskQueryParamDto.getStartTime());
            Date dateEnd = formatter.parse(taskQueryParamDto.getEndTime());
            List<HistoricActivityInstance> list = new ArrayList<>();
            long count = 0;
            if (taskQueryParamDto.isBendFlag()) {
                list = historicActivityInstanceQuery
                        .taskAssignee(taskQueryParamDto.getAssign())
                        .finished()
                        .finishedAfter(dateStart)
                        .finishedBefore(dateEnd)
                        .orderByHistoricActivityInstanceEndTime().desc()
                        .listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                                taskQueryParamDto.getPageSize());
                count = historicActivityInstanceQuery.taskAssignee(taskQueryParamDto.getAssign()).finished().finishedAfter(dateStart).finishedBefore(dateEnd).count();
            } else {
                list = historicActivityInstanceQuery
                        .taskAssignee(taskQueryParamDto.getAssign())
                        .finished()
                        .startedAfter(dateStart)
                        .startedBefore(dateEnd)
                        .orderByHistoricActivityInstanceEndTime().desc()
                        .listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                                taskQueryParamDto.getPageSize());
                count = historicActivityInstanceQuery.taskAssignee(taskQueryParamDto.getAssign()).finished().startedAfter(dateStart).startedBefore(dateEnd).count();
            }

            List<TaskDto> taskDtoList = new ArrayList<>();


            for (HistoricActivityInstance historicActivityInstance : list) {
                String activityId = historicActivityInstance.getActivityId();
                String activityName = historicActivityInstance.getActivityName();
                String executionId = historicActivityInstance.getExecutionId();
                String taskId = historicActivityInstance.getTaskId();
                Date startTime = historicActivityInstance.getStartTime();
                Date endTime = historicActivityInstance.getEndTime();
                Long durationInMillis = historicActivityInstance.getDurationInMillis();
                String processInstanceId = historicActivityInstance.getProcessInstanceId();

                String processDefinitionId = historicActivityInstance.getProcessDefinitionId();
                //流程id
                String flowId = NodeUtil.getFlowId(processDefinitionId);
                List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                        .processInstanceId(historicActivityInstance.getProcessInstanceId())
                        .list();
                Map<String, Object> variableMap = variableInstances.stream().collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue,(o,n)->n ));
                TaskDto taskDto = new TaskDto();
                taskDto.setFlowId(flowId);
                taskDto.setTaskCreateTime(startTime);
                taskDto.setTaskEndTime(endTime);
                taskDto.setNodeId(activityId);
                taskDto.setExecutionId(executionId);
                taskDto.setProcessInstanceId(processInstanceId);
                taskDto.setDurationInMillis(durationInMillis);
                taskDto.setTaskId(taskId);
                taskDto.setAssign(historicActivityInstance.getAssignee());
                taskDto.setTaskName(activityName);
                taskDto.setParamMap(variableMap);
                taskDtoList.add(taskDto);
            }
            PageResultDto<TaskDto> pageResultDto = new PageResultDto<>();
            pageResultDto.setTotal(count);
            pageResultDto.setRecords(taskDtoList);

            return R.success(pageResultDto);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询用户待办任务
     *
     * @param taskQueryParamDto
     * @return
     */
    @PostMapping("/queryAssignTask")
    public R<PageResultDto<TaskDto>> queryAssignTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {

        String assign = taskQueryParamDto.getAssign();
        String processId = taskQueryParamDto.getProcessInstanceId();

        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> tasks = StrUtil.isBlank(processId) ?
                taskQuery.taskAssignee(assign).orderByTaskCreateTime().desc().listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                        taskQueryParamDto.getPageSize()) :
                taskQuery.taskAssignee(assign).processInstanceId(processId).orderByTaskCreateTime().desc().listPage((taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
                        taskQueryParamDto.getPageSize());
        long count = StrUtil.isBlank(processId) ? taskQuery.taskAssignee(assign).count() : taskQuery.taskAssignee(assign).processInstanceId(processId).count();

        List<TaskDto> taskDtoList = new ArrayList<>();
        log.debug("当前有" + count + " 个任务:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskId = task.getId();
            String processInstanceId = task.getProcessInstanceId();
            log.debug((i + 1) + ") (" + taskId + ") " + task.getName() + " processInstanceId={} executrionId={}",
                    processInstanceId, task.getExecutionId());

            Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
            Map<String, Object> runtimeServiceVariables = runtimeService.getVariables(task.getProcessInstanceId());
            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
            log.debug("任务变量:{}", JSONUtil.toJsonStr(taskServiceVariables));
            log.debug("流程节点:{}", JSONUtil.toJsonStr(runtimeServiceVariables));
            log.debug("执行节点变量:{}", JSONUtil.toJsonStr(variables));

            //nodeid
            String taskDefinitionKey = task.getTaskDefinitionKey();

            String processDefinitionId = task.getProcessDefinitionId();
            //流程id
            String flowId = NodeUtil.getFlowId(processDefinitionId);

            TaskDto taskDto = new TaskDto();
            taskDto.setFlowId(flowId);
            taskDto.setTaskCreateTime(task.getCreateTime());
            taskDto.setNodeId(taskDefinitionKey);
            taskDto.setParamMap(taskServiceVariables);
            taskDto.setProcessInstanceId(processInstanceId);
            taskDto.setTaskId(taskId);
            taskDto.setAssign(task.getAssignee());
            taskDto.setTaskName(task.getName());

            taskDtoList.add(taskDto);

        }

        PageResultDto<TaskDto> pageResultDto = new PageResultDto<>();
        pageResultDto.setTotal(count);
        pageResultDto.setRecords(taskDtoList);


        return R.success(pageResultDto);
    }


}
