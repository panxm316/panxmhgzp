package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.flow.IProcessNodeDataService;
import com.hgzp.common.flowable.dto.ProcessNodeDataDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.model.ProcessNodeData;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.flow.ProcessNodeDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 流程节点数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessNodeDataServiceImpl extends ServiceImpl<ProcessNodeDataMapper, ProcessNodeData> implements IProcessNodeDataService {
    /**
     * 保存流程节点数据
     *
     * @param processNodeDataDto
     * @return
     */
    @Override
    public Json saveNodeData(ProcessNodeDataDto processNodeDataDto) {

        ProcessNodeData processNodeData= BeanUtil.copyProperties(processNodeDataDto, ProcessNodeData.class);
        this.save(processNodeData);



        return Json.success();
    }

    /***
     * 获取节点数据
     * @param flowId
     * @param nodeId
     * @return
     */
    @Override
    public Json<String> getNodeData(String flowId, String nodeId) {
        ProcessNodeData processNodeData = this.lambdaQuery().eq(ProcessNodeData::getFlowId, flowId).eq(ProcessNodeData::getNodeId, nodeId).one();
        return Json.success("", processNodeData==null?null:processNodeData.getData());
    }
}
