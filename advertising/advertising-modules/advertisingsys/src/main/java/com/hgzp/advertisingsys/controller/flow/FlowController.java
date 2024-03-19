package com.hgzp.advertisingsys.controller.flow;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.flow.TbflowServiceI;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import com.hgzp.advertisingsys.service.system.TbemployServiceI;
import com.hgzp.advertisingsys.service.system.TbroleServiceI;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * FlowController
 * 创建人：suny
 * 类描述：获取数据流相关数据及流程设置
 * 创建日期：2023/10/14 9:04
 *
 * @测试：
 * @folder flow/FlowController
 */
@Validated
@RestController
@RequestMapping("/flowset")
public class FlowController extends BaseController {
    @Autowired
    private TbdeptServiceI tbdeptServiceI;
    @Autowired
    private TbroleServiceI tbroleService;
    @Autowired
    private TbemployServiceI tbemployService;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 查询组织架构树
     * 方法功能: 查询组织架构树
     *
     * @param deptId    部门id
     * @param type      查询类型
     * @param showLeave 是否显示离职员工
     * @return java.lang.Object
     * @author suny
     * @date 2023/10/14 9:09
     */
    @GetMapping("/getOrgTreeData")
    public Json getOrgTreeData(@RequestParam String deptId,
                               String type,
                               @RequestParam(defaultValue = "false") Boolean showLeave) {
        if ("dept".equals(type)) {
            return tbdeptServiceI.getOrgTreeData(deptId, type, showLeave);
        } else if ("role".equals(type)) {
            return tbroleService.getRoleForFlow();
        } else {
            return tbemployService.getOrgTreeData(deptId, type, showLeave);//添加人员信息
        }
    }

    /**
     * 模糊搜索用户
     * 方法功能: 模糊搜索用户 - 工作流用
     *
     * @param userName 用户名/拼音/首字母
     * @return java.lang.Object 匹配到的用户
     * @author yanz
     * @date 2023/10/14 13:53
     */
    @GetMapping("tree/user/search")
    public Json getOrgTreeUser(@RequestParam String userName) {
        return tbemployService.getOrgTreeUser(userName.trim());
    }

    /**
     * 获取流程设置分页列表
     * 方法功能:
     *
     * @param pageRequest
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbflow>>
     * @author songly
     * @date 2024/2/16 14:04
     */
    @GetMapping("/getFlowTypePageList")
    public Json<List<Tbflow>> getFlowTypePageList(PageRequest pageRequest, BaseQueryInfo queryInfo) {
        Page page = getPage(pageRequest);
        IPage<Tbflow> flowList = tbflowServiceI.getFlowPageList(page, queryInfo);
        return Json.success(flowList);
    }

    /**
     * 获取流程设置
     * 方法功能:
     *
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbflow>>
     * @author songly
     * @date 2024/2/16 10:52
     */
    @GetMapping("/getFlowTypeList")
    public Json<List<Tbflow>> getFlowTypeList(BaseQueryInfo queryInfo) {
        List<Tbflow> flowList = tbflowServiceI.getFlowList(queryInfo);
        return Json.success(flowList);
    }

    /**
     * 根据Id获取流程设置
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbflowcondition>
     * @author songly
     * @date 2024/2/16 11:11
     */
    @GetMapping(value = "/getFlowTypeById")
    public Json<Tbflow> getFlowTypeById(@NotNull(message = "ID不可为空") String id) {
        Tbflow flowInfo = tbflowServiceI.getById(id);
        return ObjectUtil.isNotNull(flowInfo) ? Json.success(flowInfo) : Json.fail();
    }

    /**
     * 根据流程key获取流程设置
     * 方法功能:
     *
     * @param flowKey
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbflowcondition>
     * @author songly
     * @date 2024/2/16 11:11
     */
    @GetMapping(value = "/getFlowTypeByKey")
    public Json<Tbflow> getFlowTypeByKey(@NotNull(message = "flowKey不可为空") String flowKey) {
        Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(flowKey);
        return ObjectUtil.isNotNull(flowInfo) ? Json.success(flowInfo) : Json.fail();
    }

    /**
     * 保存流程设置
     * 方法功能:
     *
     * @param tbflow
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/2/16 10:52
     */
    @PostMapping("/saveFlowType")
    public Json SaveFlowType(@RequestBody Tbflow tbflow) {
        if (tbflowServiceI.isExistFlow(tbflow)) {
            return Json.fail("已存在同名流程");
        }
        innerInterceptor.recoredLog();
        return tbflowServiceI.save(tbflow) ? Json.success() : Json.fail();
    }

    /**
     * 更新流程设置
     * 方法功能:
     *
     * @param tbflow
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/2/16 10:52
     */
    @PostMapping("/updateFlowType")
    public Json UpdateFlowType(@RequestBody Tbflow tbflow) {
        if (tbflowServiceI.isExistFlow(tbflow)) {
            return Json.fail("已存在同名流程");
        }
        innerInterceptor.recoredLog();
        return tbflowServiceI.updateById(tbflow) ? Json.success() : Json.fail();
    }

    /**
     * 根据Id删除流程
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/2/16 10:51
     */
    @PostMapping("/deleteFlowTypeById")
    public Json DeleteTypeFlowById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (tbflowServiceI.removeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 获取流程最大序号
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author songly
     * @date 2024/2/16 10:51
     */
    @GetMapping("/getFlowTypeMaxSort")
    public Json<Integer> getFlowTypeMaxSort() {
        Integer maxSort = tbflowServiceI.getMaxSort();
        return Json.success(maxSort);
    }
}
