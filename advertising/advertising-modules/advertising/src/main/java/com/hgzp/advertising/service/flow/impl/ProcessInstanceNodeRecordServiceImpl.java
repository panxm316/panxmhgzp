package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.flow.IProcessInstanceNodeRecordService;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.dto.ProcessInstanceNodeRecordParamDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessInstanceNodeRecord;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.flow.ProcessInstanceNodeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 流程节点记录 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceNodeRecordServiceImpl extends ServiceImpl<ProcessInstanceNodeRecordMapper, ProcessInstanceNodeRecord> implements IProcessInstanceNodeRecordService {
    /**
     * 节点开始
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public Json start(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {

        ProcessInstanceNodeRecord processInstanceNodeRecord = BeanUtil.copyProperties(processInstanceNodeRecordParamDto, ProcessInstanceNodeRecord.class);
        processInstanceNodeRecord.setStartTime(new Date());
        processInstanceNodeRecord.setStatus(NodeStatusEnum.JXZ.getCode());

        this.save(processInstanceNodeRecord);
        return Json.success();
    }

    /**
     * 节点结束
     *
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    @Override
    public Json complete(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto) {

        log.info("节点结束---{}", JSON.toJSONString(processInstanceNodeRecordParamDto));

        //TODO 完成节点和完成任务要区分下
        this.lambdaUpdate()
                .set(ProcessInstanceNodeRecord::getStatus,NodeStatusEnum.YJS.getCode())
                .set(ProcessInstanceNodeRecord::getEndTime,new Date())
                .eq(ProcessInstanceNodeRecord::getProcessInstanceId, processInstanceNodeRecordParamDto.getProcessInstanceId())
                .eq(ProcessInstanceNodeRecord::getNodeId, processInstanceNodeRecordParamDto.getNodeId()).update(new ProcessInstanceNodeRecord());
        return Json.success();
    }
}
