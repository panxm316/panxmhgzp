package com.hgzp.advertising.service.system;

import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.service.system.BaseTbdeptServiceI;

import java.util.Collection;
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
     * 根据拦截器权限获取部门树
     * 方法功能: 根据拦截器权限获取部门树（前台用）。（在后台用部门树基础上改的）
     *
     * @param query         查询条件
     * @param showUnuseDept 是否显示未启用的部门
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/11/9 10:26
     */
    List<TreeModel> getDeptTree(TreeQuery query, boolean showUnuseDept) throws Exception;

    /**
     * getQueryDeptId
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/8/29 13:03
     */
    List<Long> getQueryDeptId(String querykey);

    /**
     * getQueryDeptIdWithoutParent
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点（不含父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/8/29 13:03
     */
    List<Long> getQueryDeptIdWithoutParent(String querykey);

    /**
     * getDeptListByQuery
     * 方法功能: 查询部门列表
     *
     * @param containsIdList 包含的部门id列表
     * @param showUnuseDept  是否显示未启用部门
     * @param notInIdList    不包含的部门id列表
     * @param selectIdList   已选择的部门id列表
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     * @author wangwk
     * @date 2023/8/31 14:33
     */
    List<Tbdept> getDeptListByQuery(Collection<Long> containsIdList, boolean showUnuseDept, Collection<Long> notInIdList, Collection<Long> selectIdList);

    /**
     * deptConvertTreeModel
     * 方法功能:  将数据库部门对象转换成树对象
     *
     * @param tbdeptList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/18 13:41
     */
    List<TreeModel> deptConvertTreeModel(List<Tbdept> tbdeptList, String iconSkin, boolean showCheck);

    /**
     * addRootDept
     * 方法功能: 部门树添加[全部部门]节点
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/29 13:17
     */
    List<TreeModel> addRootDept(List<TreeModel> treeModelList);

    /**
     * 部门树添加[全部部门]节点，不带选中框
     * 方法功能:部门树添加[全部部门]节点，不带选中框
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/11/7 15:57
     */
    List<TreeModel> addRootDeptWithoutCheck(List<TreeModel> treeModelList);

    /**
     * 取得所有根部门
     * 方法功能:取得所有根部门
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     * @author yanz
     * @date 2023/10/28 15:14
     */
    List<Tbdept> getRootDepts();

    /***
     * 按 id 集合取得部门列表
     * 方法功能:按 id 集合取得部门列表
     * @author yanz
     * @date 2023/11/3 11:37
     * @param ids
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     */
    List<Tbdept> getDeptByIds(List<Long> ids);

    /**
     * 部门信息树型列表
     * 方法功能: 部门信息树型列表
     *
     * @param query             查询条件
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author lhl
     * @date 2024/1/23
     */
    List<TreeModel> getLeafDeptTree(TreeQuery query) throws Exception;

}
