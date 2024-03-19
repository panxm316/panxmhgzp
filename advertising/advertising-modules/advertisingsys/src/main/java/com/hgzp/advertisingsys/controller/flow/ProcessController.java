package com.hgzp.advertisingsys.controller.flow;

import com.hgzp.common.flowable.vo.ProcessVO;
import com.hgzp.advertisingsys.service.flow.IProcessService;
import com.hgzp.core.page.Json;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.model.Process;

import javax.annotation.Resource;
import java.util.List;

/**
 * ProcessController
 * 创建人：wwk
 * 类描述：工作流controller
 * 创建日期：2023/10/12 10:41
 * @folder flow/ProcessController
 */
@RestController
@RequestMapping("/flow/process")
public class ProcessController {

    @Resource
    private IProcessService processService;

    /**
     * 获取详细数据
     * 方法功能: 获取详细数据
     * @author wangwk
     * @date 2023/10/28 10:11
     * @param flowId
     * @return com.hgzp.core.page.Json<com.hgzp.common.flowable.vo.ProcessVO>
     */
    @GetMapping("getDetail")
    public Json<ProcessVO> getDetail(String flowId) {
        return processService.getDetail(flowId);
    }

    /**
     * 创建流程
     * 方法功能: 创建流程
     * @author wangwk
     * @date 2023/10/28 10:11
     * @param process
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("create")
    public Json create(@RequestBody Process process) throws Exception {
        return processService.create(process);
    }


    /**
     * 编辑表单
     * 方法功能: 编辑表单
     * @author wangwk
     * @date 2023/10/28 10:11
     * @param flowId
     * @param type
     * @param groupId
     * @return com.hgzp.core.page.Json
     */
    @PutMapping("update/{flowId}")
    public Json update(@PathVariable String flowId,
                       @RequestParam String type,
                       @RequestParam(required = false) String groupId) {
        return processService.update(flowId, type, groupId);
    }

    /**
     * 查询所有的流程列表
     * 方法功能:  查询所有的流程列表
     * @author wangwk
     * @date 2023/10/12 16:40
     * @param groupId
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.advertisingsys.pagemodel.flow.ProcessVO>>
     */
    @GetMapping("/listProcess")
    public Json<List<ProcessVO>> listProcess(String groupId) {
        List<ProcessVO> processVOList = processService.listProcess(groupId);
        return Json.success(processVOList);
    }


}
