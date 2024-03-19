package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.pagemodel.flow.NodeFormatParamVo;
import com.hgzp.advertising.pagemodel.flow.NodeVo;
import com.hgzp.advertising.pagemodel.flow.ProcessInstanceRecordVo;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.advertising.service.ad.TwadresourceapplicationServiceI;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.customer.TwcustomerFilingServiceI;
import com.hgzp.advertising.service.flow.*;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.advertising.service.system.impl.TbemployServiceImpl;
import com.hgzp.advertising.utils.CoreHttpUtil;
import com.hgzp.advertising.utils.NodeFormatUtil;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.common.flowable.vo.FormItemVO;
import com.hgzp.common.flowable.vo.ProcessVO;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.exception.DataNotSupportException;
import com.hgzp.core.model.Process;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实例进程服务
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceServiceImpl implements IProcessInstanceService {

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private TwmessageServiceI messageService;
    @Resource
    private TbemployServiceImpl tbemployService;
    @Resource
    private TbdeptServiceI tbdeptService;
    @Resource
    private TwcustomerFilingServiceI customerFilingServiceI;
    @Autowired
    private TbflowconditionServiceI flowconditionServiceI;
    @Autowired
    private TwadresourceapplicationServiceI twadresourceapplicationServiceI;
    @Autowired
    private TwpreinvoiceapplicationServiceI preinvoapplicationServiceI;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    private TwapplicationrelationsServiceI applicationrelationsServiceI;

    /**
     * 本方法不支持组条件类型的表单字段流程的发起
     * 方法功能: 发起工作流
     *
     * @param flowId    工作流id
     * @param params    表单、条件参数
     * @param flowTypes 流程类型
     * @return com.hgzp.core.page.Json<java.lang.String>
     * @author wangwk
     * @date 2023/11/14 14:02
     */
    @Override
    public Json<String> startProcessInstanceByFlowTypes(String flowId, Map<String, Object> params,
                                                        FlowTypes flowTypes) throws Exception {
        Map<String, Object> flowMap = new HashMap<>();
        Object businessId = params.get("businessId");
        Object businessName = params.get("businessName");
        if (StrUtil.isBlankIfStr(businessId) || StrUtil.isBlankIfStr(businessName)) {
            throw new DataNotExistException("业务id或业务名称不能为空");
        }
        flowMap.put("businessId", businessId);
        flowMap.put("businessName", businessName);

        List<Tbflowcondition> flowConditionList = flowconditionServiceI.getFlowConditionList(flowTypes.getKey());
        for (Tbflowcondition tbflowcondition : flowConditionList) {
            Object value = params.get(tbflowcondition.getSconditionkey());
            if (StrUtil.isBlankIfStr(value)) {
                throw new DataNotExistException(StrUtil.format("表单字段【{}】不能为空", tbflowcondition.getSconditionkey()));
            }
            if (FormTypeEnum.CONDITION_RELATION.getType().equals(tbflowcondition.getSconditiontype())) {
                throw new DataNotSupportException("本方法暂不支持条件关系类型的表单字段");
            }
            if (FormTypeEnum.SELECT_USER.getType().equals(tbflowcondition.getSconditiontype())) {
                List<Map<String, String>> userList = new ArrayList<>();
                Map<String, String> userMap = new HashMap<>();
                userMap.put("id", value.toString());
                userMap.put("name", tbemployService.getById(value.toString()).getSusername());
                userMap.put("type", "user");
                userList.add(userMap);
                flowMap.put(tbflowcondition.getSconditionkey(), userList);
                continue;
            }
            if (FormTypeEnum.SELECT_DEPT.getType().equals(tbflowcondition.getSconditiontype())) {
                List<Map<String, String>> deptList = new ArrayList<>();
                Map<String, String> deptMap = new HashMap<>();
                deptMap.put("id", value.toString());
                deptMap.put("name", tbdeptService.getById(value.toString()).getSdeptname());
                deptMap.put("type", "dept");
                deptList.add(deptMap);
                flowMap.put(tbflowcondition.getSconditionkey(), deptList);
                continue;
            }
            flowMap.put(tbflowcondition.getSconditionkey(), value);
        }
        ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
        processInstanceParamDto.setFlowId(flowId);
        processInstanceParamDto.setParamMap(flowMap);
        return startProcessInstance(processInstanceParamDto);
    }


    /**
     * 启动流程
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public Json<String> startProcessInstance(ProcessInstanceParamDto processInstanceParamDto) throws Exception {

        String userId = WebUtil.getLoginUser().getUserid().toString();

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        processInstanceParamDto.setStartUserId(String.valueOf(userId));
        Map<String, Object> paramMap = processInstanceParamDto.getParamMap();
        Dict rootUser = Dict.create().set("id", userId).set("name", user.getName()).set("type",
                NodeUserTypeEnum.USER.getKey());
        paramMap.put("root", CollUtil.newArrayList(rootUser));

        String post = CoreHttpUtil.startProcess(processInstanceParamDto);
        R<String> r = JSON.parseObject(post, new TypeReference<R<String>>() {
        });
        if (!r.isSuccess()) {
            return Json.fail(r.getMsg());
        }
        String data = r.getObj();


        return Json.success("", data);
    }

    /**
     * 查询当前登录用户的待办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public Json<PageResultDto<TaskDto>> queryMineTask(ProcessInstancePageDto pageVO) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();

        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(userId);

        R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryAssignTask(taskQueryParamDto);

        PageResultDto<TaskDto> pageResultDto = r.getObj();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Json.success(pageResultDto);
        }

        Set<String> processInstanceIdSet =
                records.stream().map(TaskDto::getProcessInstanceId).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList =
                processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(ProcessInstanceRecord::getUserId).collect(Collectors.toSet());

        List<UserDto> startUserList = new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }
        for (TaskDto record : records) {
            if (MapUtil.isNotEmpty(record.getParamMap()) && record.getParamMap().get("businessName") != null) {
                record.setBusinessName(record.getParamMap().get("businessName").toString());
            }
            if (MapUtil.isNotEmpty(record.getParamMap()) && record.getParamMap().get("businessId") != null) {
                record.setBusinessId(record.getParamMap().get("businessId").toString());
            }

            ProcessInstanceRecord processInstanceRecord =
                    processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {
                record.setGroupId(processInstanceRecord.getGroupId());
                record.setProcessName(processInstanceRecord.getName());

                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(processInstanceRecord.getUserId())).findAny().orElse(null);


                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
        }

        return Json.success(pageResultDto);
    }


    /**
     * 查询已办任务
     *
     * @param pageVO
     * @return
     */
    @Override
    public Json<PageResultDto<TaskDto>> queryMineEndTask(PageDto pageVO) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        String userId = loginUser.getUserid().toString();
        if (pageVO.getEndTime() != null) {
            DateTime dateTime = DateUtil.offsetDay(DateUtil.parse(pageVO.getEndTime()), 1);
            String formatDate = DateUtil.formatDate(dateTime);
            pageVO.setEndTime(formatDate);
        }
        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(pageVO, TaskQueryParamDto.class);
        taskQueryParamDto.setAssign(userId);

        R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryCompletedTask(taskQueryParamDto);


        PageResultDto<TaskDto> pageResultDto = r.getObj();
        List<TaskDto> records = pageResultDto.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Json.success(pageResultDto);

        }


        Set<String> processInstanceIdSet =
                records.stream().map(w -> w.getProcessInstanceId()).collect(Collectors.toSet());

        //流程实例记录
        List<ProcessInstanceRecord> processInstanceRecordList =
                processInstanceRecordService.lambdaQuery().in(ProcessInstanceRecord::getProcessInstanceId,
                processInstanceIdSet).list();

        //发起人
        Set<String> startUserIdSet =
                processInstanceRecordList.stream().map(w -> w.getUserId()).collect(Collectors.toSet());

        List<UserDto> startUserList = new ArrayList<>();
        {
            for (String userIds : startUserIdSet) {
                UserDto user = ApiStrategyFactory.getStrategy().getUser(userIds);
                startUserList.add(user);
            }
        }


        for (TaskDto record : records) {
            if (MapUtil.isNotEmpty(record.getParamMap()) && record.getParamMap().get("businessName") != null) {
                record.setBusinessName(record.getParamMap().get("businessName").toString());
            }
            if (MapUtil.isNotEmpty(record.getParamMap()) && record.getParamMap().get("businessId") != null) {
                record.setBusinessId(record.getParamMap().get("businessId").toString());
            }
            ProcessInstanceRecord processInstanceRecord =
                    processInstanceRecordList.stream().filter(w -> StrUtil.equals(w.getProcessInstanceId(),
                    record.getProcessInstanceId())).findAny().orElse(null);

            if (processInstanceRecord != null) {
                record.setGroupId(processInstanceRecord.getGroupId());
                record.setProcessName(processInstanceRecord.getName());


                UserDto startUser = startUserList.stream().filter(w -> w.getId()
                        .equals(processInstanceRecord.getUserId())).findAny().orElse(null);


                record.setRootUserId(processInstanceRecord.getUserId());
                record.setGroupName(processInstanceRecord.getGroupName());
                record.setRootUserName(startUser.getName());
                record.setRootUserAvatarUrl(startUser.getAvatarUrl());
                record.setStartTime(processInstanceRecord.getCreateTime());
            }
        }


        return Json.success(pageResultDto);
    }

    /**
     * 流程结束
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public Json end(ProcessInstanceParamDto processInstanceParamDto) {
        Boolean result = processInstanceParamDto.getResult();
        processInstanceRecordService.lambdaUpdate()
                .set(result != null, ProcessInstanceRecord::getResult, result)
                .set(ProcessInstanceRecord::getEndTime, new Date())
                .set(ProcessInstanceRecord::getStatus, NodeStatusEnum.YJS.getCode())
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId())
                .update(new ProcessInstanceRecord());

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceParamDto.getProcessInstanceId())
                .one();

        MessageDto messageDto = MessageDto.builder()
                .processInstanceId(processInstanceParamDto.getProcessInstanceId())
                .flowId(processInstanceRecord.getFlowId())
                .readed(false)
                .userId(processInstanceRecord.getUserId()).build();

        if (result != null && result) {
            //审批通过
            String content = StrUtil.format("您有一条审批已通过，流程名称：【{}】，发起时间：【{}】",
                    processInstanceRecord.getName(),
                    DateUtil.formatDateTime(processInstanceRecord.getCreateTime()));
            String title = StrUtil.format("您的【{}】审批已通过", processInstanceRecord.getName());

            messageDto.setTitle(title);
            messageDto.setContent(content);
            messageDto.setType(MessageTypeEnum.APPROVE_PASS.getType());
            messageDto.setProcessInstanceCreate(processInstanceRecord.getCreateTime());
            messageService.saveMessage(messageDto);
        }

        if (result != null && !result) {
            //审批未通过
            List<ProcessInstanceAssignUserRecord> processInstanceAssignUserRecords =
                    processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId,
                            processInstanceParamDto.getProcessInstanceId())
                    .list();
            //拒绝人id
            String refuseUserId = "";
            String reason = "";
            for (ProcessInstanceAssignUserRecord processInstanceAssignUserRecord : processInstanceAssignUserRecords) {
                if (StrUtil.isNotBlank(processInstanceAssignUserRecord.getData())) {
                    JSONObject jsonObject = JSON.parseObject(processInstanceAssignUserRecord.getData());
                    Boolean nodeResult = jsonObject.getBoolean(processInstanceAssignUserRecord.getNodeId() +
                            "_approve_condition");
                    if (!nodeResult) {
                        refuseUserId = processInstanceAssignUserRecord.getUserId();
                        reason = jsonObject.getString("approveDesc");
                        break;
                    }
                }
            }

            Tbemploy tbemploy = tbemployService.getById(refuseUserId);
            String refuseUserName = "";
            if ("-99999999".equals(refuseUserId)) {
                refuseUserName = "系统默认拒绝";
            } else if (tbemploy == null) {
                refuseUserName = "系统";
            } else {
                refuseUserName = tbemploy.getSusername();
            }
            String content = StrUtil.format("您有一条审批被驳回，流程名称：【{}】，发起时间：【{}】, 驳回人：【{}】， 原因：【{}】",
                    processInstanceRecord.getName(),
                    DateUtil.formatDateTime(processInstanceRecord.getCreateTime()),
                    refuseUserName,
                    reason);
            String title = StrUtil.format("您的【{}】审批被驳回,请及时查看", processInstanceRecord.getName());

            messageDto.setTitle(title);
            messageDto.setContent(content);
            messageDto.setType(MessageTypeEnum.APPROVE_REJECT.getType());
            messageDto.setProcessInstanceCreate(processInstanceRecord.getCreateTime());
            messageService.saveMessage(messageDto);

        }


        return Json.success();
    }

    /**
     * 流程审批结束之后回调
     * 方法功能: 流程审批结束之后对应的流程处理结果
     *
     * @param processInstanceParamDto
     * @return void
     * @author wangwk
     * @date 2023/10/31 13:45
     */
    @Override
    public void handlerProcessResult(ProcessInstanceParamDto processInstanceParamDto) {
        //流程结果
        Boolean result = processInstanceParamDto.getResult();
        //流程实例id
        String processInstanceId = processInstanceParamDto.getProcessInstanceId();
        if (result != null) {
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .one();
            ProcessInstanceAssignUserRecord userRecord = processNodeRecordAssignUserService.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceId)
                    .orderByDesc(ProcessInstanceAssignUserRecord::getUpdateTime)
                    .list().get(0);
//                    .one();
            //最后一个人的审批意见
            String approveDesc = userRecord.getApproveDesc();

            if (FlowTypes.FLOW_CUSTOMER.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 客户挂牌审批结果处理
                if (ObjUtil.isNotNull(processInstanceParamDto.getParamMap().get("businessId"))) {
                    String sCustomerId = processInstanceParamDto.getParamMap().get("businessId").toString();
                    Integer iApproveStatus = processInstanceRecord.getResult() ? ApproveStatus.APPROVE_PASS.getKey()
                            : ApproveStatus.APPROVE_REJECT.getKey();
                    customerFilingServiceI.updateCustomerApprovalopinions(processInstanceRecord.getProcessInstanceId(), sCustomerId, true, approveDesc, iApproveStatus);
                }
            }
            if (FlowTypes.FLOW_ADBOOKING.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 口头预定审批流程结果处理
            }
            if (FlowTypes.FLOW_ADSOURCE.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 资源审批流程结果处理
                String businessId = processInstanceParamDto.getParamMap().get("businessId").toString();
                twadresourceapplicationServiceI.updateCheckInfo(processInstanceId, businessId,
                        processInstanceRecord.getResult(), approveDesc);
            }
            if (FlowTypes.FLOW_CONTRACT.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 电子认刊书(折扣)审批流程结果处理
                if (ObjUtil.isNotNull(processInstanceParamDto.getParamMap().get("businessId"))) {
                    String sorderId = processInstanceParamDto.getParamMap().get("businessId").toString();
                    Integer iApproveStatus = processInstanceRecord.getResult() ? ApproveStatus.APPROVE_PASS.getKey()
                            : ApproveStatus.APPROVE_REJECT.getKey();
                    orderServiceI.updateOrderApprovalopinions(processInstanceRecord.getProcessInstanceId(), sorderId,
                            true, approveDesc, ApproveType.APPROVE_Discount.getKey(), iApproveStatus);
                    //如果折扣审批通过，订单审批免审就自动通过
                    if (processInstanceRecord.getResult()) {
                        Tbflow tbflow = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDER.getKey());
                        if (ObjUtil.isNotNull(tbflow) && !tbflow.getBactive()) {
                            orderServiceI.updateOrderApprovalopinions("", sorderId, false, "",
                                    ApproveType.APPROVE_Order.getKey(), ApproveStatus.APPROVE_PASS.key);
                        }
                    }
                }
            }
            if (FlowTypes.FLOW_PREINVOICE.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 预开发票审批流程结果处理
                Object businessId = processInstanceParamDto.getParamMap().get("businessId");
                if (ObjUtil.isNotNull(businessId)) {
                    String id = businessId.toString();
                    preinvoapplicationServiceI.updateApplyStatus(id, processInstanceRecord.getResult() ?
                            ApproveStatus.APPROVE_PASS.getKey() : ApproveStatus.APPROVE_REJECT.getKey(), approveDesc);
                }
            }
            if (FlowTypes.FLOW_ORDER.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 订单审批流程结果处理
                if (ObjUtil.isNotNull(processInstanceParamDto.getParamMap().get("businessId"))) {
                    String sorderId = processInstanceParamDto.getParamMap().get("businessId").toString();
                    Integer iApproveStatus = processInstanceRecord.getResult() ? ApproveStatus.APPROVE_PASS.getKey()
                            : ApproveStatus.APPROVE_REJECT.getKey();
                    orderServiceI.updateOrderApprovalopinions(processInstanceRecord.getProcessInstanceId(), sorderId,
                            true, approveDesc, ApproveType.APPROVE_Order.getKey(), iApproveStatus);
                }
            }
            if (FlowTypes.FLOW_ORDERCHANGE.getKey().equals(processInstanceRecord.getGroupId())) {
                //TODO 改加停刊审批流程结果处理
                if (ObjUtil.isNotNull(processInstanceParamDto.getParamMap().get("businessId"))) {
                    String sorderId = processInstanceParamDto.getParamMap().get("businessId").toString();

                    //从关联表中获取子业务id
                    TwapplicationrelationsDTO appQuery= new TwapplicationrelationsDTO();
                    appQuery.setApplicationid(processInstanceRecord.getProcessInstanceId());
                    appQuery.setFlowtype(FlowTypes.FLOW_ORDERCHANGE.getKey());
                    appQuery.setMainid(Long.valueOf(sorderId));
                    Twapplicationrelations twapplicationrelations = applicationrelationsServiceI.getApplicationRelations(appQuery);

                    String subbusinessId="";
                    String sapprovetype="";
                    if(ObjUtil.isNotNull(twapplicationrelations)){
                         subbusinessId = twapplicationrelations.getChildid().toString();
                        sapprovetype = twapplicationrelations.getApprovetype().toString();
                    }

                    Integer iApproveStatus = processInstanceRecord.getResult() ? ApproveStatus.APPROVE_PASS.getKey()
                            : ApproveStatus.APPROVE_REJECT.getKey();
                    orderServiceI.updateOrderApprovalopinions(processInstanceRecord.getProcessInstanceId(), sorderId,
                            true, approveDesc, Integer.valueOf(sapprovetype), iApproveStatus,subbusinessId);
                    //如果折扣审批通过，订单审批免审就自动通过
                    if (processInstanceRecord.getResult()) {
                        Tbflow tbflow = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_ORDER.getKey());
                        if (ObjUtil.isNotNull(tbflow) && !tbflow.getBactive()) {
                            orderServiceI.updateOrderApprovalopinions("", sorderId, false, "",
                                    ApproveType.APPROVE_Order.getKey(), ApproveStatus.APPROVE_PASS.key);
                        }
                    }
                }
            }

        }
    }


    /**
     * 查询我发起的
     *
     * @param pageDto
     * @return
     */
    @Override
    public Json<List<ProcessInstanceRecordVo>> queryMineStarted(ProcessInstancePageDto pageDto) throws Exception {

        String userId = WebUtil.getLoginUser().getUserid().toString();
        if (pageDto.getEndTime() != null) {
            DateTime dateTime = DateUtil.offsetDay(DateUtil.parse(pageDto.getEndTime()), 1);
            String formatDate = DateUtil.formatDate(dateTime);
            pageDto.setEndTime(formatDate);
        }

        Page<ProcessInstanceRecord> instanceRecordPage = processInstanceRecordService.lambdaQuery()
                .eq(ProcessInstanceRecord::getUserId, userId)
                .ge(pageDto.getStartTime() != null, ProcessInstanceRecord::getCreateTime, pageDto.getStartTime())
                .lt(pageDto.getEndTime() != null, ProcessInstanceRecord::getCreateTime, pageDto.getEndTime())
                .eq(StrUtil.isNotBlank(pageDto.getProcessInstanceId()), ProcessInstanceRecord::getProcessInstanceId,
                        pageDto.getProcessInstanceId())
                .orderByDesc(ProcessInstanceRecord::getCreateTime)
                .page(new Page<>(pageDto.getPageNum(), pageDto.getPageSize()));

        //转换对象
        Page<ProcessInstanceRecordVo> lstProcessInstanceRecordVo = new Page<>();
        if (CollUtil.isNotEmpty(instanceRecordPage.getRecords())) {
            List<ProcessInstanceRecordVo> lstInstanceRecordVo = new ArrayList<>();
            for (ProcessInstanceRecord processInstanceRecord : instanceRecordPage.getRecords()) {
                ProcessInstanceRecordVo processInstanceRecordVo = new ProcessInstanceRecordVo();
                BeanUtil.copyProperties(processInstanceRecord, processInstanceRecordVo);
                if (ObjUtil.isNotNull(processInstanceRecord.getFormData())) {
                    JSONObject jsonObject = JSON.parseObject(processInstanceRecord.getFormData());
                    processInstanceRecordVo.setBusinessName(jsonObject.getString("businessName"));
                    processInstanceRecordVo.setBusinessId(jsonObject.getString("businessId"));
                }
                lstInstanceRecordVo.add(processInstanceRecordVo);
            }
            lstProcessInstanceRecordVo.setRecords(lstInstanceRecordVo);
        }

        return Json.success(lstProcessInstanceRecordVo);
    }


    /**
     * 根据参数获取历史流程
     * 方法功能: 根据参数获取历史流程
     *
     * @param variableName  吗，
     * @param variableValue
     * @return java.util.List<com.hgzp.core.model.ProcessInstanceRecord>
     * @author wangwk
     * @date 2023/11/10 15:35
     */
    @Override
    public List<ProcessInstanceRecord> getHistoryProcessInstanceRecordByBussinessId(String variableName,
                                                                                    String variableValue) {
        R<List<String>> processInstanceIdsByVar = CoreHttpUtil.getProcessInstanceIdsByVar(variableName, variableValue);
        List<String> processInstanceIdsList = processInstanceIdsByVar.getObj();
        if (CollUtil.isNotEmpty(processInstanceIdsList)) {
            List<ProcessInstanceRecord> processInstanceRecords = processInstanceRecordService.lambdaQuery()
                    .in(ProcessInstanceRecord::getProcessInstanceId, processInstanceIdsList)
                    .orderByDesc(ProcessInstanceRecord::getCreateTime)
                    .orderByAsc(ProcessInstanceRecord::getFlowId)
                    .list();
            return processInstanceRecords;
        }

        return Collections.emptyList();
    }


    /**
     * 显示流程实例图片
     *
     * @param procInsId
     * @return
     */
    @Override
    public Json<String> showImg(String procInsId) {
        String s = CoreHttpUtil.showImg(procInsId);
        R<String> stringR = JSON.parseObject(s, new TypeReference<R<String>>() {
        });
        String data = stringR.getObj();
//
//        OutputStream out = response.getOutputStream();
//
//        Base64.decodeToStream(data, out, true);
        return Json.success("", data);
    }

    /**
     * 格式化流程显示
     *
     * @param nodeFormatParamVo
     * @return
     */
    @Override
    public Json<List<NodeVo>> formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo) throws Exception {
        String flowId = nodeFormatParamVo.getFlowId();
        String processInstanceId = nodeFormatParamVo.getProcessInstanceId();
        if (StrUtil.isAllBlank(flowId, processInstanceId)) {
            return Json.success(new ArrayList<>());
        }

        if (StrUtil.isBlankIfStr(flowId) && StrUtil.isNotBlank(processInstanceId)) {
            ProcessInstanceRecord processInstanceRecord =
                    processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                    processInstanceId).one();
            flowId = processInstanceRecord.getFlowId();


        }
        Map<String, Object> paramMap = nodeFormatParamVo.getParamMap();
        Set<String> continueNodeSet = new HashSet<>();
        if (StrUtil.isNotBlank(nodeFormatParamVo.getTaskId())) {
            String s = CoreHttpUtil.queryTaskVariables(nodeFormatParamVo.getTaskId(), null);

//            String userId = WebUtil.getLoginUser().getUserid().toString();
//            R<TaskResultDto> taskResultDtoR = CoreHttpUtil.queryTask(nodeFormatParamVo.getTaskId(), userId);
//            continueNodeSet.add(taskResultDtoR.getObj().getNodeId());

            R<Map<String, Object>> r = JSON.parseObject(s,
                    new TypeReference<R<Map<String, Object>>>() {
                    });
            if (!r.isSuccess()) {

                List<ProcessInstanceAssignUserRecord> list = processNodeRecordAssignUserService.lambdaQuery()
                        .eq(ProcessInstanceAssignUserRecord::getTaskId, nodeFormatParamVo.getTaskId())
                        .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.YJS.getCode())

                        .orderByDesc(ProcessInstanceAssignUserRecord::getEndTime)
                        .list();

                String data = list.get(0).getData();
                Map<String, Object> variableMap = JSON.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            } else {
                Map<String, Object> variableMap = r.getObj();
                variableMap.putAll(paramMap);
                paramMap.putAll(variableMap);
            }

        }

        Set<String> completeNodeSet = new HashSet<>();

        if (StrUtil.isNotBlank(processInstanceId)) {
            List<ProcessInstanceNodeRecord> processInstanceNodeRecordList = processNodeRecordService.lambdaQuery()
                    .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                    .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.YJS.getCode())
                    .list();
            Set<String> collect =
                    processInstanceNodeRecordList.stream().map(w -> w.getNodeId()).collect(Collectors.toSet());
            completeNodeSet.addAll(collect);

            List<ProcessInstanceNodeRecord> processInstanceNodeRecordContinueList =
                    processNodeRecordService.lambdaQuery()
                    .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceId)
                    .eq(ProcessInstanceNodeRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                    .list();

            Set<String> jxz =
                    processInstanceNodeRecordContinueList.stream().map(w -> w.getNodeId()).collect(Collectors.toSet());
            continueNodeSet.addAll(jxz);
        }


        Process oaForms = processService.getByFlowId(flowId);
        String process = oaForms.getProcess();
        Node nodeDto = JSON.parseObject(process, Node.class);

        List<NodeVo> processNodeShowDtos = NodeFormatUtil.formatProcessNodeShow(nodeDto, completeNodeSet,
                continueNodeSet, processInstanceId, paramMap);
        if (nodeFormatParamVo.getIsAllProcess()) {
            return Json.success(processNodeShowDtos);
        } else {
            List<NodeVo> ProcessNodeVoShowDtos = NodeFormatUtil.formatProcessNodeVoShow(processNodeShowDtos);
            return Json.success(ProcessNodeVoShowDtos);
        }
    }

    /**
     * 流程详情
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public Json detail(String processInstanceId) throws Exception {


        String userId = WebUtil.getLoginUser().getUserid().toString();


        ProcessInstanceRecord processInstanceRecord =
                processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId,
                        processInstanceId).one();


        Process oaForms = processService.getByFlowId(processInstanceRecord.getFlowId());
        if (oaForms == null) {
            return Json.fail("流程不存在");
        }


        //发起人变量数据
        String formData = processInstanceRecord.getFormData();
        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });
        //发起人表单权限
        String process = oaForms.getProcess();
        Node nodeDto = JSON.parseObject(process, Node.class);
        Map<String, String> formPerms1 = nodeDto.getFormPerms();


        List<FormItemVO> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : jsonObjectList) {
            String id = formItemVO.getId();
            String perm = formPerms1.get(id);

            formItemVO.setProps(new FormItemVO.Props());
            formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.READ :
                    (StrUtil.equals(perm, ProcessInstanceConstant.FormPermClass.HIDE) ?
                            perm : ProcessInstanceConstant.FormPermClass.READ
                    )
            );

            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, id,
                        new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms1.get(itemVO.getId());

                        itemVO.setPerm(StrUtil.isBlankIfStr(permSub) ? ProcessInstanceConstant.FormPermClass.READ :
                                (StrUtil.equals(permSub, ProcessInstanceConstant.FormPermClass.HIDE) ?
                                        permSub : ProcessInstanceConstant.FormPermClass.READ
                                ));


                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            } else {
                formItemVO.getProps().setValue(variableMap.get(id));

            }


        }
        Dict set = Dict.create()
                .set("processInstanceId", processInstanceId)
                .set("process", oaForms.getProcess())


                .set("formItems", jsonObjectList);

        return Json.success(set);
    }

    /**
     * 据 流程类型/流程组 获取流程下拉列表
     * 方法功能:据 流程类型/流程组 获取流程下拉列表
     *
     * @param flowType
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author yanz
     * @date 2023/11/6 11:02
     */
    @Override
    public List<DataCombo> getFlowComboByFlowType(String flowType) {
        List<Process> list = processService.lambdaQuery()
                .eq(StrUtil.isNotBlank(flowType), Process::getGroupId, flowType)
                .eq(Process::getHidden, false)
                .orderByAsc(Process::getSort)
                .orderByDesc(Process::getCreateTime)
                .list();

        List<ProcessVO> processVOList = BeanUtil.copyToList(list, ProcessVO.class);
        processVOList.forEach(vo -> vo.setGroupName(FlowTypes.getNameByKey(vo.getGroupId())));

        if (ObjUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<DataCombo> comboList = list.stream()
                .map(item -> new DataCombo(item.getFlowId(), item.getName()))
                .collect(Collectors.toList());

        return comboList;
    }
}
