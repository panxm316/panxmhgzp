package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.common.flowable.dto.ProcessInstanceAssignUserRecordParamDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessInstanceAssignUserRecord;
import com.hgzp.core.page.Json;

/**
 * <p>
 * 流程节点记录-执行人 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessInstanceAssignUserRecordService extends IService<ProcessInstanceAssignUserRecord> {
    /**
     * 设置执行人
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    Json addAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);

    /**
     * 任务完成通知
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    Json completeTaskEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);


}
