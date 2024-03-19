package com.hgzp.advertisingsys.service.system;

import cn.hutool.core.lang.Dict;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.system.BaseTbdeptServiceI;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbdeptServiceI extends BaseTbdeptServiceI {


    /**
     * getSysDeptTree
     * 方法功能: 根据人员类型获取后台设置部门树
     * @author wangwk
     * @date 2023/8/18 13:42
     * @param query 查询条件
     * @param showUnuseDept 是否显示未启用的部门
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    List<TreeModel> getSysDeptTree(TreeQuery query, boolean showUnuseDept) throws Exception;


    /**
     * saveDept
     * 方法功能: 新增部门
     * @author wangwk
     * @date 2023/8/18 13:54
     * @param tbdept 部门实体
     * @return void
     */
    void saveDept(Tbdept tbdept);

    /**
     * updateDept
     * 方法功能:  更新部门信息
     * @author wangwk
     * @date 2023/8/18 13:57
     * @param tbdept 部门实体
     * @return void
     */
    void updateDept(Tbdept tbdept);

    /**
     * batchUdpateDept
     * 方法功能:  批量调整部门
     * @author wangwk
     * @date 2023/8/30 10:29
     * @param ids  要调整的部门id，多个用逗号隔开
     * @param pid  调整到的部门id
     * @return void
     */
    void batchUdpateDept(String ids, Long pid);

    /**
     * deleteDept
     * 方法功能: 部门删除
     * @author wangwk
     * @date 2023/8/21 13:59
     * @param ids 部门id， 多个用逗号隔开
     * @return void
     */
    void deleteDept(String ids);

    /**
     * deleteManageDept
     * 方法功能: 删除管理部门
     *
     * @param ids
     * @return void
     * @author yanz
     * @date 2024/3/13 16:21
     */
    void deleteManageDept(String ids);

    /**
     * getParentDeptBuse
     * 方法功能: 获取父部门的buse
     * @author wangwk
     * @date 2023/8/28 9:33
     * @param id 部门id
     * @return java.lang.Boolean
     */
    Boolean getParentDeptBuse(Long id);

    /**
     * importDeptAndEmpData
     * 方法功能:  excel 导入人员部门
     * @author wangwk
     * @date 2023/9/4 12:54
     * @param file
     * @return void
     */
    void importDeptAndEmpData(File file) throws Exception;
    /**
     * getOrgTreeData
     * 方法功能: 查询所有的组织架构 并树形显示
     * @author suny
     * @date 2023/10/13 16:07
     * @param deptId 部门id
     * @param type 只查询部门架构
     * @param showLeave 是否显示离职员工
     * @return java.lang.Object 组织架构树数据
     */
    Json<Dict> getOrgTreeData(String deptId, String type, Boolean showLeave);
}
