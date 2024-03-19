package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.flow.dto.MytodoDTO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceRecordService;
import com.hgzp.advertising.service.flow.MyToDoServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.advertising.service.system.TbroleServiceI;
import com.hgzp.advertising.utils.CoreHttpUtil;
import com.hgzp.common.flowable.dto.PageResultDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.TaskDto;
import com.hgzp.common.flowable.dto.TaskQueryParamDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Twworkreports;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.mapper.business.TwworkreportsMapper;
import com.hgzp.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MyToDoServiceImpl
 * 创建人：songly
 * 类描述：我的待办 移动端用
 * 创建日期：2024/3/6 10:01
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MyToDoServiceImpl implements MyToDoServiceI {
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;
    @Autowired
    private TbroleServiceI tbroleService;
    @Autowired
    private TbemployServiceI tbemployService;
    @Autowired
    private TworderMapper orderMapper;
    @Autowired
    private TwworkreportsMapper workreportsMapper;

    @Override
    public Json<PageResultDto<TaskDto>> queryMyToDo(MytodoDTO mytodoDTO) throws Exception {
        String userId = mytodoDTO.getLoginUserId();
        //人员信息
        Tbemploy empInfo = tbemployService.getById(Long.valueOf(userId));
        //查询待办任务
        TaskQueryParamDto taskQueryParamDto = BeanUtil.copyProperties(mytodoDTO, TaskQueryParamDto.class);
        taskQueryParamDto.setPageNum(1);
        taskQueryParamDto.setPageSize(2000);
        taskQueryParamDto.setAssign(userId);

        R<PageResultDto<TaskDto>> r = CoreHttpUtil.queryAssignTask(taskQueryParamDto);

        PageResultDto<TaskDto> pageResultDto = r.getObj();
        List<TaskDto> records = pageResultDto.getRecords();
        if (records.size() > 0) {

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
        }

        if (empInfo.getBlead()) {
            //人员类型权限
            EmpAuthorityDTO empAuthorityDTO = tbroleService.getEmpAuthIdsByEmpId(Long.valueOf(userId));
            Date EndTime = new Date();
            if (mytodoDTO.getEndTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date enddate = sdf.parse(mytodoDTO.getEndTime());
                EndTime = DateUtil.offsetDay(enddate, 1);
            }
            //待处理的快速预约
            LambdaQueryWrapper<Tworder> lqw = Wrappers.lambdaQuery();
            lqw.eq(Tworder::getIbooktype, 2)
                    .eq(Tworder::getBuse, true)
                    .eq(Tworder::getBdelete, false)
                    .ge(ObjectUtil.isNotNull(mytodoDTO.getStartTime()), Tworder::getCreatetime,
                            mytodoDTO.getStartTime());

            if (mytodoDTO.getEndTime() != null) {
                lqw.le(ObjectUtil.isNotNull(mytodoDTO.getEndTime()), Tworder::getCreatetime, EndTime);
            }
            if (empAuthorityDTO.getDeptIdList().size() > 0) {
                lqw.in(Tworder::getDeptid, empAuthorityDTO.getDeptIdList());
            }
            lqw.eq(Tworder::getIpreapprovestatus, 1)
                    .orderByDesc(Tworder::getCreatetime);

            List<Tworder> lsOrders = orderMapper.selectList(lqw);
            if (lsOrders.size() > 0) {
                for (Tworder item : lsOrders) {
                    TaskDto taskDto = new TaskDto();
                    taskDto.setAssign(userId);
                    taskDto.setBusinessId(item.getId().toString());
                    taskDto.setBusinessName(item.getSordernum());
                    taskDto.setGroupId("");
                    taskDto.setGroupName("快速预约审批");
                    taskDto.setNodeId("");
                    taskDto.setProcessInstanceId("");
                    taskDto.setProcessName("");
                    taskDto.setRootUserId(item.getCreateempid().toString());
                    taskDto.setRootUserName(item.getCreateempname());
                    taskDto.setStartTime(item.getCreatetime());
                    taskDto.setTaskCreateTime(item.getCreatetime());
                    taskDto.setTaskId("");
                    taskDto.setTaskName("快速预约审批");

                    records.add(taskDto);
                }
            }

            //待处理的工作报告审批
            LambdaQueryWrapper<Twworkreports> lqwreports = Wrappers.lambdaQuery();
            lqwreports.eq(Twworkreports::getBuse, 1)
                    .ge(ObjectUtil.isNotNull(mytodoDTO.getStartTime()), Twworkreports::getDcreatedate,
                            mytodoDTO.getStartTime());

            if (mytodoDTO.getEndTime() != null) {
                lqwreports.le(ObjectUtil.isNotNull(mytodoDTO.getEndTime()), Twworkreports::getDcreatedate, EndTime);
            }
            if (empAuthorityDTO.getDeptIdList().size() > 0) {
                lqwreports.in(Twworkreports::getDeptid, empAuthorityDTO.getDeptIdList());
            }
            lqwreports.eq(Twworkreports::getIapprovestatus, 1)
                    .orderByDesc(Twworkreports::getDcreatedate);

            List<Twworkreports> lsReports = workreportsMapper.selectList(lqwreports);
            if (lsReports.size() > 0) {
                for (Twworkreports item : lsReports) {
                    TaskDto taskDto = new TaskDto();
                    taskDto.setAssign(userId);
                    taskDto.setBusinessId(item.getId().toString());
                    taskDto.setBusinessName(item.getCustomername());
                    taskDto.setGroupId("");
                    taskDto.setGroupName("工作报告审批");
                    taskDto.setNodeId("");
                    taskDto.setProcessInstanceId("");
                    taskDto.setProcessName("");
                    taskDto.setRootUserId(item.getEmployid().toString());
                    taskDto.setRootUserName(item.getEmployname());
                    taskDto.setStartTime(item.getDcreatedate());
                    taskDto.setTaskCreateTime(item.getDstartdate());
                    taskDto.setTaskId("");
                    taskDto.setTaskName("工作报告审批");

                    records.add(taskDto);
                }
            }

            //排序
            records = records.stream().sorted(Comparator.comparing(TaskDto::getStartTime).reversed()).collect(Collectors.toList());

            //分页
            int total = records.size();
            int pageSize = mytodoDTO.getPageSize();
            //int pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            int pageNo = mytodoDTO.getPageNum();
            List<TaskDto> subTaskList =
                    records.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

            pageResultDto.setRecords(subTaskList);
            pageResultDto.setTotal(Long.valueOf(total));
        }

        return Json.success(pageResultDto);
    }
}