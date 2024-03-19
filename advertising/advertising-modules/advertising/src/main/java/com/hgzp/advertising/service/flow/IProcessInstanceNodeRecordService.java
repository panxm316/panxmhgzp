package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.common.flowable.dto.ProcessInstanceNodeRecordParamDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessInstanceNodeRecord;
import com.hgzp.core.page.Json;

/**
 * <p>
 * 流程节点记录 服务类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessInstanceNodeRecordService extends IService<ProcessInstanceNodeRecord> {
    /**
     * 节点开始
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    Json start(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto);

    /**
     * 节点结束
     * @param processInstanceNodeRecordParamDto
     * @return
     */
    Json complete(ProcessInstanceNodeRecordParamDto processInstanceNodeRecordParamDto);


}
