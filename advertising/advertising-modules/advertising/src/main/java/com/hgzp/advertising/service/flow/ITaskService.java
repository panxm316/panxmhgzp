package com.hgzp.advertising.service.flow;


import com.hgzp.common.flowable.dto.TaskParamDto;
import com.hgzp.core.page.Json;

/**
 * 任务处理
 */
public interface ITaskService {
    /**
     * 查询任务
     *
     * @param taskId
     * @param view
     * @return
     */
    Json queryTask(String taskId, boolean view) throws Exception;

    /**
     * 完成任务
     * @param taskParamDto
     * @return
     */
    Json completeTask(TaskParamDto taskParamDto) throws Exception;

    /**
     * 前加签
     * @param taskParamDto
     * @return
     */
    Json delegateTask(TaskParamDto taskParamDto) throws Exception;

    /**
     *  加签完成任务
     * @param taskParamDto
     * @return
     */
    Json resolveTask(TaskParamDto taskParamDto);

    /**
     * 设置执行人
     * @param taskParamDto
     * @return
     */
    Json setAssignee(TaskParamDto taskParamDto) throws Exception;

    /**
     * 结束流程
     * @param taskParamDto
     * @return
     */
    Json stopProcessInstance(TaskParamDto taskParamDto) throws Exception;

    /**
     * 退回
     * @param taskParamDto
     * @return
     */
    Json back( TaskParamDto taskParamDto) throws Exception;

}
