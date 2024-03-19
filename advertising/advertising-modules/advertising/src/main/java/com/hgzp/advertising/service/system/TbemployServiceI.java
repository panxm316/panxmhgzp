package com.hgzp.advertising.service.system;

import com.hgzp.advertising.pagemodel.system.vo.EmployVO;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.service.system.BaseTbemployServiceI;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 人员表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbemployServiceI extends BaseTbemployServiceI {
    /**
     * getEmployInfo
     * 方法功能: 获取当前人员信息，包括部门和角色名称
     *
     * @param
     * @return com.hgzp.advertising.pagemodel.system.vo.EmployVO
     * @author suny
     * @date 2023/9/12 15:36
     */
    EmployVO getEmployInfo() throws Exception;

    /**
     * getDeptLeaderIdList
     * 方法功能: 查找部门id下的所有领导id集合
     *
     * @param deptId 部门id
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/10/13 13:59
     */
    List<Long> getDeptLeaderIdList(Long deptId);

    /**
     * 按关键词查询部门、人员信息树
     * 方法功能:按关键词查询部门、人员信息树，并按权限显示
     *
     * @param query              查询条件
     * @param accessNodes        可访问的人员和部门id列表，限当前部门，不含父部门，以英文逗号（,）分隔
     * @param bIgnorePermissions 是否忽略权限，true：忽略权限，false：需要权限
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/24 13:23
     */
    List<TreeModel> getEmpTree(TreeQuery query, List<Long> accessNodes, Boolean bIgnorePermissions) throws Exception;


    /**
     * 滤掉人员树中的非销售员工
     * 方法功能:滤掉 List<TreeModel> 中的 非 Saleman 的 Employ（属性为Bsalesman）
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/27 16:20
     */
    List<TreeModel> filterNonSalesperson(List<TreeModel> treeModelList);

    /**
     * 据传入员工id列表，查询员工列表
     * 方法功能: 据传入员工id列表，查询员工列表，条件限制为 getBinner = true, getBuse = true
     *
     * @param empIds       包含的员工id列表
     * @param notInIdList  不包含的员工id列表
     * @param selectIdList 已选择的员工id列表
     * @return java.util.List<com.hgzp.core.model.Tbemploy>
     * @author yanz
     * @date 2023/10/24 17:14
     */
    List<Tbemploy> getEmpsListByEmpIds(Collection<Long> empIds, Collection<Long> notInIdList, Collection<Long> selectIdList);

    /**
     * 据传入部门id列表，查询员工列表
     * 方法功能: 据传入员工id列表，查询员工列表，条件限制为 getBinner = true, getBuse = true
     *
     * @param deptIdList   包含的部门id列表
     * @param notInIdList  不包含的部门id列表
     * @param selectIdList 已选择的部门id列表
     * @return java.util.List<com.hgzp.core.model.Tbemploy>
     * @author yanz
     * @date 2023/10/28 17:12
     */
    List<Tbemploy> getEmpsListByDeptIds(Collection<Long> deptIdList, Collection<Long> notInIdList, Collection<Long> selectIdList);

    /**
     * 通过部门id列表查询所有人员id
     * 方法功能: 通过部门id列表查询所有人员id，条件限制为 getBinner = true, getBuse = true
     *
     * @param queryDeptsIds
     * @return java.util.List<java.lang.Long>
     * @author yanz
     * @date 2023/10/26 10:52
     */
    List<Long> getEmpIdsByDeptIds(List<Long> queryDeptsIds);

    /**
     * 将数据库员工对象转换成树对象
     * 方法功能:将数据库员工对象转换成树对象
     *
     * @param tbempList
     * @param iconSkin
     * @param showCheck
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/24 16:43
     */
    List<TreeModel> empConvertTreeModel(List<Tbemploy> tbempList, String iconSkin, boolean showCheck) throws Exception;

    /**
     * 据bleader，取得当前用户管理的部门
     * 方法功能:据bleader，取得当前用户管理的部门（当前及以下）；非负责人（false）设所在部门
     *
     * @param employee
     * @return java.lang.String
     * @author yanz
     * @date 2023/10/30 12:45
     */
    String getAuthedDeptIdsByLeader(Tbemploy employee);


}
