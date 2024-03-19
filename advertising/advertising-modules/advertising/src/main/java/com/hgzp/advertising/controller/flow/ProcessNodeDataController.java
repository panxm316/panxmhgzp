package com.hgzp.advertising.controller.flow;

import com.hgzp.advertising.service.flow.IProcessNodeDataService;
import com.hgzp.common.flowable.dto.ProcessNodeDataDto;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.core.annotation.InnerAuth;
import com.hgzp.core.page.Json;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * ProcessNodeDataController
 * 创建人：wwk
 * 类描述：流程节点数据controller
 * 创建日期：2023/8/23 13:09
 * @folder flow/ProcessNodeDataController
 */
@RestController
@RequestMapping("/flow/processNodeData")
public class ProcessNodeDataController {

    @Resource
    private IProcessNodeDataService processNodeDataService;

    /**
     * 保存节点数据
     * 方法功能: 保存节点数据
     * @author wwk
     * @date 2023/10/27 11:21
     * @param processNodeDataDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("saveNodeData")
    public Json saveNodeData(@RequestBody ProcessNodeDataDto processNodeDataDto){
        return processNodeDataService.saveNodeData(processNodeDataDto);
    }

    /**
     * 获取节点数据
     * 方法功能:
     * @author wwk
     * @date 2023/10/27 11:21
     * @param flowId
     * @param nodeId
     * @return com.hgzp.core.page.Json<java.lang.String>
     */
    @InnerAuth
    @GetMapping("getNodeData")
    public Json<String> getNodeData(String flowId,String nodeId){
        return processNodeDataService.getNodeData(flowId, nodeId);
    }

}
