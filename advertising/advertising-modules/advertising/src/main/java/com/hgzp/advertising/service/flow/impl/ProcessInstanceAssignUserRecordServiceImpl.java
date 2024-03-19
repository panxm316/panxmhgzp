package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.flow.IProcessInstanceAssignUserRecordService;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.dto.ProcessInstanceAssignUserRecordParamDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessInstanceAssignUserRecord;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.flow.ProcessInstanceAssignUserRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程节点记录-执行人 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceAssignUserRecordServiceImpl extends ServiceImpl<ProcessInstanceAssignUserRecordMapper, ProcessInstanceAssignUserRecord> implements IProcessInstanceAssignUserRecordService {
    /**
     * 设置执行人
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public Json addAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        if (StrUtil.isNotBlank(processInstanceAssignUserRecordParamDto.getApproveDesc())) {
            List<ProcessInstanceAssignUserRecord> list = this.lambdaQuery()
                    .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                    .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime)
                    .list();
            if (CollUtil.isNotEmpty(list)) {
                ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = list.get(0);
                processInstanceAssignUserRecord.setApproveDesc(processInstanceAssignUserRecordParamDto.getApproveDesc());
                processInstanceAssignUserRecord.setTaskType(processInstanceAssignUserRecordParamDto.getTaskType());
                processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
                processInstanceAssignUserRecord.setEndTime(new Date());
                this.updateById(processInstanceAssignUserRecord);
            }

        }

        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = BeanUtil.copyProperties(processInstanceAssignUserRecordParamDto, ProcessInstanceAssignUserRecord.class);
        processInstanceAssignUserRecord.setStartTime(new Date());
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.JXZ.getCode());
        processInstanceAssignUserRecord.setApproveDesc("");
        processInstanceAssignUserRecord.setTaskType("");
        this.save(processInstanceAssignUserRecord);

        return Json.success();
    }

    /**
     * 任务完成通知
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public Json completeTaskEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        List<ProcessInstanceAssignUserRecord> list = this.lambdaQuery()
                .eq(ProcessInstanceAssignUserRecord::getTaskId, processInstanceAssignUserRecordParamDto.getTaskId())
                .eq(ProcessInstanceAssignUserRecord::getUserId, processInstanceAssignUserRecordParamDto.getUserId())
                .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceAssignUserRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceAssignUserRecord::getStatus, NodeStatusEnum.JXZ.getCode())
                .orderByDesc(ProcessInstanceAssignUserRecord::getCreateTime)
                .list();
        ProcessInstanceAssignUserRecord processInstanceAssignUserRecord = list.get(0);
        processInstanceAssignUserRecord.setStatus(NodeStatusEnum.YJS.getCode());
        processInstanceAssignUserRecord.setApproveDesc(processInstanceAssignUserRecordParamDto.getApproveDesc());
        processInstanceAssignUserRecord.setEndTime(new Date());
        processInstanceAssignUserRecord.setData(processInstanceAssignUserRecordParamDto.getData());
        processInstanceAssignUserRecord.setLocalData(processInstanceAssignUserRecordParamDto.getLocalData());
        processInstanceAssignUserRecord.setTaskType("COMPLETE");
        this.updateById(processInstanceAssignUserRecord);
        return Json.success();
    }
}
