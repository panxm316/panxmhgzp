package com.hgzp.advertising.service.flow;


import com.hgzp.advertising.pagemodel.flow.NodeFormatParamVo;
import com.hgzp.advertising.pagemodel.flow.NodeVo;
import com.hgzp.advertising.pagemodel.flow.ProcessInstanceRecordVo;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;

import java.util.List;
import java.util.Map;

/**
 * 流程实例进程
 */
public interface IProcessInstanceService {

    /**
     * 本方法不支持组条件类型的表单字段流程的发起
     * 方法功能: 发起工作流
     * @author wangwk
     * @date 2023/11/14 14:02
     * @param flowId 工作流id
     * @param params 表单、条件参数
     * @param flowTypes 流程类型
     * @return com.hgzp.core.page.Json<java.lang.String>
     */
    Json<String> startProcessInstanceByFlowTypes(String flowId, Map<String, Object> params, FlowTypes flowTypes) throws Exception;

    /**
     * 启动流程
     * @param processInstanceParamDto
     * @return
     */
    Json<String> startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) throws Exception;

    /**
     * 查询当前登录用户的待办任务
     * @param pageVO
     * @return
     */
    Json<PageResultDto<TaskDto>> queryMineTask(ProcessInstancePageDto pageVO) throws Exception;

    /**
     *  查询已办任务
     * @param pageVO
     * @return
     */
    Json<PageResultDto<TaskDto>> queryMineEndTask(PageDto pageVO) throws Exception;

    /**
     * 流程结束
     *
     * @param processInstanceParamDto
     * @return
     */
    Json end(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 流程审批结束之后回调
     * 方法功能: 流程审批结束之后对应的流程处理结果
     * @author wangwk
     * @date 2023/10/31 13:45
     * @param processInstanceParamDto
     * @return void
     */
    void handlerProcessResult(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询我发起的
     * @param pageDto
     * @return
     */
    Json<List<ProcessInstanceRecordVo>>  queryMineStarted(ProcessInstancePageDto pageDto) throws Exception;


    List<ProcessInstanceRecord> getHistoryProcessInstanceRecordByBussinessId(String variableName, String variableValue);

    /**
     * 显示流程实例图片
     * @param procInsId
     * @return
     */
    Json<String> showImg(String procInsId);

    /**
     * 格式化流程显示
     * @param nodeFormatParamVo
     * @return
     */
    Json<List<NodeVo>> formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) throws Exception;

    /**
     * 流程详情
     * @param processInstanceId
     * @return
     */
    Json detail(String processInstanceId) throws Exception;

    /**
     * 据 流程类型/流程组 获取流程下拉列表
     * 方法功能:据 流程类型/流程组 获取流程下拉列表
     * @author yanz
     * @date 2023/11/6 11:02
     * @param flowType
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<DataCombo> getFlowComboByFlowType(String flowType);
}
