package com.hgzp.advertising.controller.system;


import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * DeptController
 * 创建人：wangwk
 * 类描述：部门设置controller
 * 创建日期：2023/8/18 13:51
 *
 * @folder system/DeptController
 */
@Validated
@RestController
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    TbdeptServiceI tbdeptService;

    /**
     * 查询所有部门树
     * 方法功能: 查询所有部门树 ，用布尔型showInactiveDepts来控制是否显示“未启用”部门，true显示，false不显示
     *
     * @param query             查询条件
     * @param showInactiveDepts 是否显示“未启用”部门，true显示，false不显示
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author yanz
     * @date 2023/11/9 10:25
     */
    @GetMapping("/getDeptTree")
    public Json<List<TreeModel>> getDeptTree(TreeQuery query, Boolean showInactiveDepts) throws Exception {
        List<TreeModel> sysDeptTree = tbdeptService.getDeptTree(query, showInactiveDepts == null ? false : showInactiveDepts);
        return Json.success(sysDeptTree);
    }

    /**
     * 报表配置部门信息树型列表
     * 方法功能: 报表配置部门信息树型列表
     *
     * @param query             查询条件
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/23
     */
    @GetMapping("/getLeafDeptTree")
    public Json<List<TreeModel>> getLeafDeptTree(TreeQuery query) throws Exception {
        List<TreeModel> sysDeptTree = tbdeptService.getLeafDeptTree(query);
        return Json.success(sysDeptTree);
    }

}