package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.common.flowable.dto.ProcessNodeDataDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessNodeData;
import com.hgzp.core.page.Json;

/**
 * <p>
 * 流程节点数据 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
public interface IProcessNodeDataService extends IService<ProcessNodeData> {

    /**
     * 保存流程节点数据
     * @param processNodeDataDto
     * @return
     */
    Json saveNodeData(ProcessNodeDataDto processNodeDataDto);

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    Json<String> getNodeData(String flowId,String nodeId);

}
