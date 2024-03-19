package com.hgzp.advertisingsys.controller.flow;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionVo;
import com.hgzp.advertisingsys.pagemodel.flow.vo.FlowConditionEx;
import com.hgzp.advertisingsys.service.flow.TbflowServiceI;
import com.hgzp.advertisingsys.service.flow.TbflowconditionServiceI;
import com.hgzp.core.emnus.ConditionTableName;
import com.hgzp.core.emnus.ConditionTypes;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Tbflowcondition;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FlowConditionController
 * 创建人：songly
 * 类描述：工作流条件controller
 * 创建日期：2023/10/12 10:41
 * @folder flow/FlowConditionController
 */
@Validated
@RestController
@RequestMapping("/flow/flowcondition")
public class FlowConditionController extends BaseController {
    @Autowired
    private TbflowconditionServiceI flowServiceI;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    /**
     * 获取流程类型（流程组）下拉列表
     * 方法功能: 获取流程类型（流程组）下拉列表
     * 修改为从数据库获取
     * @author suny
     * @date 2023/10/12 14:47
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>     *
     */
    @GetMapping("/getFlowTypesCombo")
    public Json<List<DataCombo>> getFlowTypesCombo() {
        List<Tbflow> tbflows = tbflowServiceI.getFlowTypeValid();
        List<DataCombo> comboList = tbflows.stream()
                .map(item -> new DataCombo(item.getSkey(), item.getSname()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }
    /**
     * 获取条件类型下拉列表
     * 方法功能: 获取条件类型下拉列表
     * @author songly
     * @date 2023/10/20 13:11
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping("/getFlowConditionTypesCombo")
    public Json<List<DataCombo>> getFlowConditionTypesCombo() {
        List<ConditionTypes> enumList = Arrays.asList(ConditionTypes.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }
    /**
     * 获取ConditionTable对应的名称
     * 方法功能: 获取ConditionTable对应的名称
     * @author songly
     * @date 2023/10/20 13:51
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping("/getConditionTableNameCombo")
    public Json<List<DataCombo>> getConditionTableNameCombo() {
        List<ConditionTableName> enumList = Arrays.asList(ConditionTableName.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }
    /**
     * 根据Id获取工作流条件
     * 方法功能: 根据Id获取工作流条件
     * @author songly
     * @date 2023/10/12 10:27
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbflowcondition>
     */
    @GetMapping(value = "/getFlowConditionById")
    public Json<Tbflowcondition> getFlowConditionById(@NotNull(message = "ID不可为空") String id) {
        Tbflowcondition flowCondition = flowServiceI.getById(id);
        return ObjectUtil.isNotNull(flowCondition) ? Json.success(flowCondition) : Json.fail();
    }

    /**
     * 获取工作流条件列表
     * 方法功能:  分页 获取工作流条件列表
     * @author songly
     * @date 2023/10/12 10:32
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbflowcondition>>
     */
    @GetMapping(value = "/getFlowConditionPageList")
    public Json<List<FlowConditionVo>> getFlowConditionPageList(PageRequest pageRequest, FlowConditionVo vo) {
        Page page = getPage(pageRequest);
        IPage<FlowConditionVo> pages = flowServiceI.getFlowConditionPageList(page, vo);
        return Json.success(pages);
    }
    /**
     * 根据类型组获取条件信息
     * 方法功能: 根据类型组获取条件信息
     * @author songly
     * @date 2023/10/13 15:27
     * @param sflowtype
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbflowcondition>>
     */
    @GetMapping(value = "/getFlowConditionList")
    public Json<List<FlowConditionEx>> getFlowConditionList(@NotBlank(message = "流程类型组不能为空！") String sflowtype) {
        List<FlowConditionEx> lsCondidion = flowServiceI.getFlowConditionList(sflowtype);
        return Json.success(lsCondidion);
    }

    /**
     * 修改工作流条件
     * 方法功能:修改工作流条件
     *
     * @param newFlowCondition
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 14:23
     */
    @PostMapping(value = "/updateFlowCondition")
    public Json updateFlowCondition(@RequestBody Tbflowcondition newFlowCondition) {
        if(flowServiceI.isExistFlowCondition(newFlowCondition)){
            return Json.fail("已存在同名条件");
        }
        innerInterceptor.recoredLog();
        return flowServiceI.updateById(newFlowCondition) ? Json.success() : Json.fail();
    }

    /**
     * 据id删除工作流条件
     * 方法功能: 据id删除工作流条件
     * @author songly
     * @date 2023/10/12 10:34
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping(value = "/deleteFlowConditionById")
    public Json deleteFlowConditionById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (flowServiceI.removeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

  /**
   * 添加工作流条件
   * 方法功能: 添加工作流条件
   * @author songly
   * @date 2023/10/12 10:36
   * @param flowcondition
   * @return com.hgzp.core.page.Json
   */
    @PostMapping(value = "/saveFlowCondition")
    public Json saveFlowCondition(@RequestBody Tbflowcondition flowcondition) {
        if(flowServiceI.isExistFlowCondition(flowcondition)){
            return Json.fail("已存在同名条件");
        }
        innerInterceptor.recoredLog();
        return flowServiceI.save(flowcondition) ? Json.success() : Json.fail();
    }

    /**
     * 获取最大序号
     * 方法功能: 获取最大序号
     * @author songly
     * @date 2023/10/12 10:42
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getFlowConditionMaxSort")
    public Json<Integer> getFlowConditionMaxSort() {
        Integer maxSort = flowServiceI.getMaxSort();
        return Json.success(maxSort);
    }


}
