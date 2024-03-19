package com.hgzp.advertising.controller.flow;

import com.hgzp.advertising.pagemodel.flow.NodeFormatParamVo;
import com.hgzp.advertising.pagemodel.flow.NodeVo;
import com.hgzp.advertising.pagemodel.flow.ProcessInstanceRecordVo;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ProcessInstanceController
 * 创建人：wwk
 * 类描述：流程实例controller
 * 创建日期：2023/8/23 13:09
 *
 * @folder flow/ProcessInstanceController
 */
@RestController
@RequestMapping("/flow/process-instance")
public class ProcessInstanceController {

    @Resource
    private IProcessInstanceService processInstanceService;

    /**
     * 启动流程
     * 方法功能: 启动流程
     *
     * @param processInstanceParamDto
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author wwk
     * @date 2023/10/27 11:10
     */
    @SneakyThrows
    @PostMapping("startProcessInstance")
    public Json<String> startProcessInstance(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
       //TODO 此处参数中的paramMap 应该根据实际业务id 进行传递
//        Map<String, Object> param = new HashMap<>();
//        param.put("businessId", "1701499815157653504");
//        param.put("businessType", "部门");
//        param.put("name", "suny");
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", "1692806568527138816");
//        map.put("name", "caogd的部门");
//        map.put("type", "dept");
//        list.add(map);
//
//        param.put("hgzpid_dept", list);
//        processInstanceParamDto.setParamMap(param);

        return processInstanceService.startProcessInstance(processInstanceParamDto);

    }

    /**
     * 查询当前登录用户的待办任务
     * 方法功能: 查询当前登录用户的待办任务
     *
     * @param pageDto
     * @return com.hgzp.core.page.Json<com.hgzp.common.flowable.dto.PageResultDto < com.hgzp.common.flowable.dto.TaskDto>>
     * @author wwk
     * @date 2023/10/27 11:11
     */
    @SneakyThrows
    @PostMapping("queryMineTask")
    public Json<PageResultDto<TaskDto>> queryMineTask(@RequestBody ProcessInstancePageDto pageDto) {

        return processInstanceService.queryMineTask(pageDto);

    }

    /**
     * 查询当前登录用户已办任务
     * 方法功能: 查询当前登录用户已办任务
     *
     * @param pageDto
     * @return com.hgzp.core.page.Json<com.hgzp.common.flowable.dto.PageResultDto < com.hgzp.common.flowable.dto.TaskDto>>
     * @author wwk
     * @date 2023/10/27 11:11
     */
    @SneakyThrows
    @PostMapping("queryMineEndTask")
    public Json<PageResultDto<TaskDto>> queryMineEndTask(@RequestBody PageDto pageDto) {

        return processInstanceService.queryMineEndTask(pageDto);

    }

    /**
     * 查询我发起的
     * 方法功能: 查询我发起的
     *
     * @param pageDto
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.ProcessInstanceRecord>>
     * @author wwk
     * @date 2023/10/27 11:11
     */
    @SneakyThrows
    @PostMapping("queryMineStarted")
    public Json<List<ProcessInstanceRecordVo>> queryMineStarted(@RequestBody ProcessInstancePageDto pageDto) {
        return processInstanceService.queryMineStarted(pageDto);
    }

    /**
     * 根据业务id查询历史流程信息
     * 方法功能: 根据发起的流程的变量及变量值查询历史流程
     * @author wangwk
     * @date 2023/11/10 15:41
     * @param variableValue
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.ProcessInstanceRecord>>
     */
    @GetMapping("queryHistoryByBussinessId")
    public Json<List<ProcessInstanceRecord>> queryHistoryByBussinessId(String variableValue){
        List<ProcessInstanceRecord> historyProcessInstanceRecord = processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId", variableValue);
        return Json.success(historyProcessInstanceRecord);
    }

    /**
     * 显示流程图
     * 方法功能: 显示流程图
     *
     * @param procInsId
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author wwk
     * @date 2023/10/27 11:20
     */
    @SneakyThrows
    @GetMapping("showImg")
    public Json<String> showImg(String procInsId) {
        return processInstanceService.showImg(procInsId);
    }

    /**
     * 格式化流程显示
     * 方法功能: 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.flow.NodeVo>>
     * @author wwk
     * @date 2023/10/27 11:20
     */
    @PostMapping("formatStartNodeShow")
    public Json<List<NodeVo>> formatStartNodeShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) throws Exception {
        return processInstanceService.formatStartNodeShow(nodeFormatParamVo);
    }

    /**
     * 流程详情
     * 方法功能: 流程详情
     *
     * @param processInstanceId
     * @return com.hgzp.core.page.Json
     * @author wwk
     * @date 2023/10/27 11:21
     */
    @GetMapping("detail")
    public Json detail(String processInstanceId) throws Exception {
        return processInstanceService.detail(processInstanceId);
    }

    /**
     * 流程下拉列表（按流程分组）
     * 方法功能:流程下拉列表（按流程分组）
     * @author yanz
     * @date 2023/11/6 10:58
     * @param flowType
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping("getFlowlistComboByFlowType")
    public Json<List<DataCombo>> getFlowlistComboByFlowType(String flowType) throws Exception {
        List<DataCombo> flowComboByFlowType = processInstanceService.getFlowComboByFlowType(flowType);
        return Json.success(flowComboByFlowType);
    }
}
