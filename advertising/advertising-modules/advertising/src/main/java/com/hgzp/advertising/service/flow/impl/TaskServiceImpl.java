package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.hgzp.advertising.service.flow.*;
import com.hgzp.advertising.utils.CoreHttpUtil;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.TaskParamDto;
import com.hgzp.common.flowable.dto.TaskResultDto;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.utils.CommonUtil;
import com.hgzp.common.flowable.vo.FormItemVO;
import com.hgzp.core.model.ProcessInstanceAssignUserRecord;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.page.Json;
import com.hgzp.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hgzp.core.model.Process;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements ITaskService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 查询任务
     *
     * @param taskId
     * @param view
     * @return
     */
    @Override
    public Json queryTask(String taskId, boolean view) throws Exception {


        String userId = WebUtil.getLoginUser().getUserid().toString();


        R<TaskResultDto> r = CoreHttpUtil.queryTask(taskId, userId);

        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }

        TaskResultDto taskResultDto = r.getObj();

        //变量
        Map<String, Object> paramMap = taskResultDto.getVariableAll();
        //是否是当前活动任务
        Boolean currentTask = taskResultDto.getCurrentTask();
        if (!currentTask) {
            List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, taskId)
                    .eq(ProcessInstanceAssignUserRecord::getUserId, userId)
                    .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YJS.getCode())

                    .orderByDesc(ProcessInstanceAssignUserRecord::getEndTime)
                    .list();

            if (CollUtil.isNotEmpty(list)) {
                String data = list.get(0).getData();
                if (StrUtil.isNotBlank(data)) {
                    Map<String, Object> collect = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                    });
                    paramMap.putAll(collect);

                }
            }

        } else {

        }

        //当前节点数据
        String nodeDataJson =
                nodeDataService.getNodeData(taskResultDto.getFlowId(), taskResultDto.getNodeId()).getObj();
        Node node = CommonUtil.toObj(nodeDataJson, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        Process oaForms = processService.getByFlowId(taskResultDto.getFlowId());
        if (oaForms == null) {
            return Json.fail("流程不存在");
        }

        List<FormItemVO> formItemVOList = CommonUtil.toArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : formItemVOList) {


            String id = formItemVO.getId();

            String perm = formPerms.get(id);
            formItemVO.setProps(new FormItemVO.Props());

            if (StrUtil.isNotBlank(perm)) {

                formItemVO.setPerm(view ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(perm) ? ProcessInstanceConstant.FormPermClass.READ : perm) : perm);

            } else {
                formItemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
            }

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(paramMap, id, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm(view ? (ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ? ProcessInstanceConstant.FormPermClass.READ : permSub)
                                    : permSub
                            );


                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);
                {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {


                            itemVO.setPerm(permSub);

                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    formItemVO.getProps().setOriForm(subItemList);

                }

            } else {
                formItemVO.getProps().setValue(paramMap.get(id));

            }

        }
        Dict set = Dict.create()
                .set("processInstanceId", taskResultDto.getProcessInstanceId())
                .set("node", taskResultDto.getTaskNode())
                .set("process", oaForms.getProcess())
                .set("delegateAgain", taskResultDto.getDelegate())
                .set("delegationTask", StrUtil.equals(taskResultDto.getDelegationState(), "PENDING"))

                .set("formItems", formItemVOList);

        return Json.success(set);
    }

    /**
     * 完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json completeTask(TaskParamDto taskParamDto) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();
        taskParamDto.setUserId(userId);


        R r = CoreHttpUtil.completeTask(taskParamDto);

        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    /**
     * 前加签
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json delegateTask(TaskParamDto taskParamDto) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();


        taskParamDto.setUserId(userId);

        String post = CoreHttpUtil.delegateTask(taskParamDto);
        R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    /**
     * 加签完成任务
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json resolveTask(TaskParamDto taskParamDto) {
        String post = CoreHttpUtil.resolveTask(taskParamDto);
        R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    /**
     * 设置执行人
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json setAssignee(TaskParamDto taskParamDto) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();

        taskParamDto.setUserId(userId);
        String post = CoreHttpUtil.setAssignee(taskParamDto);
        R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    /**
     * 结束流程
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json stopProcessInstance(TaskParamDto taskParamDto) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();

        String processInstanceId = taskParamDto.getProcessInstanceId();

        List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceId);
        CollUtil.reverse(allStopProcessInstanceIdList);
        allStopProcessInstanceIdList.add(processInstanceId);

        taskParamDto.setProcessInstanceIdList(allStopProcessInstanceIdList);
        taskParamDto.setUserId(userId);
        R r = CoreHttpUtil.stopProcessInstance(taskParamDto);

        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    /**
     * 退回
     *
     * @param taskParamDto
     * @return
     */
    @Override
    public Json back(TaskParamDto taskParamDto) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();

        taskParamDto.setUserId(userId);
        String post = CoreHttpUtil.back(taskParamDto);
        R r = JSON.parseObject(post, new TypeReference<R>() {
        });
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }


        return Json.success();
    }

    private List<String> getAllStopProcessInstanceIdList(String processInstanceId) {
        List<ProcessInstanceRecord> list = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getParentProcessInstanceId, processInstanceId).list();

        List<String> collect = list.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toList());

        for (ProcessInstanceRecord processInstanceRecord : list) {
            List<String> allStopProcessInstanceIdList = getAllStopProcessInstanceIdList(processInstanceRecord.getProcessInstanceId());

            collect.addAll(allStopProcessInstanceIdList);

        }
        return collect;
    }
}
