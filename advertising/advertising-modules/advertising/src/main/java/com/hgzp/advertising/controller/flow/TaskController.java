package com.hgzp.advertising.controller.flow;

import com.hgzp.advertising.service.flow.ITaskService;
import com.hgzp.common.flowable.dto.TaskParamDto;
import com.hgzp.core.page.Json;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * TaskController
 * 创建人：wwk
 * 类描述：任务控制器controller
 * 创建日期：2023/8/23 13:09
 * @folder flow/TaskController
 */
@RestController
@RequestMapping("/flow/task")
public class TaskController {

    @Resource
    private ITaskService taskService;

    /**
     * 查询任务
     * 方法功能: 查询任务
     * @author wwk
     * @date 2023/10/27 11:27
     * @param taskId
     * @param view
     * @return com.hgzp.core.page.Json
     */
    @SneakyThrows
    @GetMapping("queryTask")
    public Json queryTask(String taskId, boolean view) {

        return taskService.queryTask(taskId,view );

    }

    /**
     * 完成任务
     * 方法功能: 完成任务
     * @author wwk
     * @date 2023/10/27 11:28
     * @param completeParamDto
     * @return com.hgzp.core.page.Json
     */
    @SneakyThrows
    @PostMapping("completeTask")
    public Json completeTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.completeTask(completeParamDto);

    }


    /**
     * 删除任务
     * 方法功能: 删除任务
     * @author wwk
     * @date 2023/10/27 11:28
     * @param completeParamDto
     * @return com.hgzp.core.page.Json
     */
    @SneakyThrows
    @PostMapping("delegateTask")
    public Json delegateTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.delegateTask(completeParamDto);

    }


    /**
     * 加签完成任务
     * 方法功能: 加签完成任务
     * @author wwk
     * @date 2023/10/27 11:28
     * @param completeParamDto
     * @return com.hgzp.core.page.Json
     */
    @SneakyThrows
    @PostMapping("resolveTask")
    public Json resolveTask(@RequestBody TaskParamDto completeParamDto) {

        return taskService.resolveTask(completeParamDto);

    }

    /**
     * 设置执行人
     * 方法功能: 设置执行人
     * @author wwk
     * @date 2023/10/27 11:28
     * @param completeParamDto
     * @return com.hgzp.core.page.Json
     */
    @SneakyThrows
    @PostMapping("setAssignee")
    public Json setAssignee(@RequestBody TaskParamDto completeParamDto) {

        return taskService.setAssignee(completeParamDto);

    }


    /**
     * 结束流程
     * 方法功能: 结束流程
     * @author wwk
     * @date 2023/10/27 11:29
     * @param completeParamDto
     * @return java.lang.Object
     */
    @SneakyThrows
    @PostMapping("stopProcessInstance")
    public Object stopProcessInstance(@RequestBody TaskParamDto completeParamDto) {

        return taskService.stopProcessInstance(completeParamDto);

    }


    /**
     * 退回
     * 方法功能: 退回
     * @author wwk
     * @date 2023/10/27 11:29
     * @param taskParamDto
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("back")
    public Json back(@RequestBody TaskParamDto taskParamDto) throws Exception {
        return taskService.back(taskParamDto);
    }



}
