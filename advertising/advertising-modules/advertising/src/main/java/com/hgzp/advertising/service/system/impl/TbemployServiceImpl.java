package com.hgzp.advertising.service.system.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hgzp.advertising.pagemodel.system.vo.EmployVO;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.advertising.service.system.TbroleServiceI;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.model.Tbrole;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.service.system.impl.BaseTbemployServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hgzp.core.constant.system.AdminFlagConstants.*;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbemployServiceImpl extends BaseTbemployServiceImpl implements TbemployServiceI {
    @Autowired
    TbroleServiceI tbroleService;
    @Autowired
    TbdeptServiceI tbdeptService;

    @Override
    public EmployVO getEmployInfo() throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Tbemploy dbEmp = this.getById(user.getUserid());
        if (dbEmp != null) {
            EmployVO employ = new EmployVO();
            BeanUtils.copyProperties(dbEmp, employ);
            Tbdept dept = tbdeptService.getById(dbEmp.getDeptid());
            if (dept != null) {
                employ.setDeptName(dept.getSdeptname());
            }
            List<Long> empIds = new ArrayList<>();
            empIds.add(user.getUserid());
            Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIds);
            List<Tbrole> tbroles = roleListGroupByUserId.get(user.getUserid());

            String roleNames = tbroles.stream()
                    .map(Tbrole::getSname)
                    .collect(Collectors.joining(","));
            String roleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employ.setRoleNames(roleNames);
            return employ;
        }
        return null;
    }

    /**
     * getDeptLeaderIdList
     * 方法功能: 查找部门id下的所有领导id集合
     *
     * @param deptId 部门id
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/10/13 13:59
     */
    @Override
    public List<Long> getDeptLeaderIdList(Long deptId) {
        //先找本级  再找兼职本级的负责人，  如果没有再找上级
        List<Tbemploy> tbemployList = this.lambdaQuery()
                .eq(Tbemploy::getBlead, true)
                .eq(Tbemploy::getDeptid, deptId)
                .list();
        if (tbemployList.size() > 0) {
            return tbemployList.stream().map(Tbemploy::getId).collect(Collectors.toList());
        }
        //兼职
        tbemployList = this.lambdaQuery()
                .eq(Tbemploy::getBlead, true)
                .like(Tbemploy::getParttimedeptid, deptId)
                .list();
        if (tbemployList.size() > 0) {
            return tbemployList.stream().map(Tbemploy::getId).collect(Collectors.toList());
        }

        //上级
        Tbdept tbdept = tbdeptService.getById(deptId);
        tbemployList = this.lambdaQuery()
                .eq(Tbemploy::getBlead, true)
                .eq(Tbemploy::getDeptid, tbdept.getParentid())
                .list();
        List<Long> leaderIdList = tbemployList.stream().map(Tbemploy::getId).collect(Collectors.toList());

        return leaderIdList;
    }

    /**
     * 按关键词查询部门、人员信息树
     * 方法功能:按关键词查询部门、人员信息树，并按权限显示
     *
     * @param query
     * @param accessNodes        可访问的人员和部门id列表，限当前部门，不含父部门，以英文逗号（,）分隔
     * @param bIgnorePermissions 是否忽略权限，true：忽略时展示全部人员
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/27 15:01
     */
    @Override
    public List<TreeModel> getEmpTree(TreeQuery query, List<Long> accessNodes, Boolean bIgnorePermissions) throws Exception {
        LoginUser curUser = WebUtil.getLoginUser();
        Set<Long> accessDepts = new HashSet<>();
        Set<Long> accessEmps = new HashSet<>();
        Set<Long> selectedIds = convertToSet(query.getContainsIds());
        splitIdSet(selectedIds, accessDepts, accessEmps);
        splitIdSet(new HashSet<>(accessNodes), accessDepts, accessEmps);
        // 据权限设置，获取可访问的部门和人员
        checkPermissions(bIgnorePermissions, curUser, accessDepts, accessEmps);

        Set<Long> excludeNodes = convertToSet(query.getExcludeIds());
        excludeNodes.addAll(excludeNodes.stream().flatMap(dept -> tbdeptService.getChildDeptId(dept).stream()).collect(Collectors.toList()));
        splitIdSet(excludeNodes, excludeNodes, excludeNodes);
        Set<Long> rdeptIds = (Set<Long>) CollUtil.subtract(accessDepts, excludeNodes);
        Set<Long> rEmpIds = (Set<Long>) CollUtil.subtract(accessEmps, excludeNodes);

        List<TreeModel> results = getResults(query, bIgnorePermissions, curUser, rdeptIds, rEmpIds, excludeNodes);
        return query.isShowRoot() ? tbdeptService.addRootDeptWithoutCheck(results) : results;
    }

    /**
     * 滤掉人员树中的非销售的员工
     * 方法功能:滤掉 List<TreeModel> 中的 非 Saleman 的 Employ（属性为Bsalesman）
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/27 16:20
     */
    @Override
    public List<TreeModel> filterNonSalesperson(List<TreeModel> treeModelList) {
        // 注掉部分是之前没bug但部门节点没滤干净的版本：暂时保留，2个月内没bug再删 - 12.14
        if (CollectionUtil.isEmpty(treeModelList)) {
            return new ArrayList<>();
        }
        List<Long> nodeIds = treeModelList.stream()
                .map(TreeModel::getId)
                .collect(Collectors.toList());
        List<Tbemploy> tbemploys = this.lambdaQuery()
                .in(Tbemploy::getId, nodeIds)
                .eq(Tbemploy::getBuse, true)
                .list();
//        List<Tbdept> tbdepts = tbdeptService.lambdaQuery()
//                .in(Tbdept::getId, nodeIds)
//                .eq(Tbdept::getBuse, true)
//                .list();
        List<Long> salesmanEmpIds = tbemploys.stream()
                .filter(Tbemploy::getBsalesman)
                .map(Tbemploy::getId)
                .collect(Collectors.toList());
        //修改：过滤掉非业务员工后没有子节点的部门应不予显示
        //  emp定为叶子节点，固定parentId；其余的考虑使用parentId、id对比，滤掉没有emp的部门分支
        List<TreeModel> emps = treeModelList.stream().filter(treeModel -> salesmanEmpIds.contains(treeModel.getId())).collect(Collectors.toList());
        return reGenEmpRelatedNodes(treeModelList, emps);
//        List<TreeModel> results = treeModelList.stream()
//                .filter(treeModel -> tbemploys.isEmpty() || salesmanEmpIds.contains(treeModel.getId()))
//                .collect(Collectors.toList());
//        results.addAll(tbdeptService.deptConvertTreeModel(tbdepts, TreeModel.UNIT, false));
//        return nodeIds.contains(0L) ? tbdeptService.addRootDeptWithoutCheck(results) : results;
    }

    /**
     * 非递归实现，滤掉‘非业务员相关’的节点
     * 方法功能:非递归实现，滤掉非销售人员相关节点；思路是从业务员向上捋，直到根节点（0L），记得过滤前加个根节点（0L）进来
     * @author yanz
     * @date 2023/12/14 10:10
     * @param treeModelList
     * @param children
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    private List<TreeModel> reGenEmpRelatedNodes(List<TreeModel> treeModelList, List<TreeModel> children) {
        Set<Long> parentIds = new HashSet<>();
        do {
            parentIds.clear();
            parentIds.addAll(children.stream()
                    .map(TreeModel::getParentId)
                    .collect(Collectors.toSet()));

            parentIds.removeAll(children.stream()
                    .map(TreeModel::getId)
                    .collect(Collectors.toSet()));

            if (parentIds.size() == 1 && parentIds.contains(0L)) {
                break;
            }

            List<TreeModel> newChildren = treeModelList.stream()
                    .filter(treeModel -> parentIds.contains(treeModel.getId()))
                    .collect(Collectors.toList());

            children.addAll(newChildren);
            children = new ArrayList<>(new HashSet<>(children));

        } while (!parentIds.isEmpty());

        return children;
    }

    /**
     * 据传入员工id列表，查询员工列表
     * 方法功能: 据传入员工id列表，查询员工列表，条件限制为 getBinner = true, getBuse = true
     *
     * @param empIds       包含的员工id列表
     * @param notInIdList  不包含的员工id列表
     * @param selectIdList 已选择的员工id列表
     * @author yanz
     * @date 2023/10/27 15:02
     */
    @Override
    public List<Tbemploy> getEmpsListByEmpIds(Collection<Long> empIds, Collection<Long> notInIdList, Collection<Long> selectIdList) {
        List<Tbemploy> empList;
        if (empIds.isEmpty()) {
            empList = new ArrayList<>();
        }
        empList = this.lambdaQuery()
                .in(empIds != null && empIds.size() > 0, Tbemploy::getId, empIds)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .notIn(notInIdList != null && notInIdList.size() > 0, Tbemploy::getId, notInIdList)
                .orderByAsc(Tbemploy::getIsort)
                .list();
        if (selectIdList == null || selectIdList.isEmpty()) {
            return empList;
        }
        empList.addAll(this.lambdaQuery()
                .in(selectIdList != null && selectIdList.size() > 0, Tbemploy::getId, selectIdList)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .notIn(notInIdList != null && notInIdList.size() > 0, Tbemploy::getId, notInIdList)
                .orderByAsc(Tbemploy::getIsort)
                .list());
        return empList;
    }

    /**
     * 将数据库员工对象转换成树对象
     * 方法功能:将数据库员工对象转换成树对象，有兼职的员工在每个兼职部门下生成一个树对象
     *
     * @param tbempList
     * @param iconSkin
     * @param showCheck
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/10/24 16:43
     */
    @Override
    public List<TreeModel> empConvertTreeModel(List<Tbemploy> tbempList, String iconSkin, boolean showCheck) throws Exception {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbemploy tbemp : tbempList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbemp.getId());
            treeModel.setIconSkin(iconSkin);
            treeModel.setParentId(tbemp.getDeptid());
            treeModel.setName(tbemp.getSusername());
            treeModel.setNocheck(!showCheck);
            tbdeptService.deptConvertTreeModel(Arrays.asList(tbdeptService.getById(tbemp.getDeptid())), TreeModel.UNIT, false).forEach(treeModelList::add);
            treeModelList.add(treeModel);
            // 仅领导用户考虑兼职
            Boolean blead = tbemp.getBlead() != null ? tbemp.getBlead() : false;
            blead = tbemp.getBadminflag() != 0 || blead;
            String partTimeDepts = tbemp.getParttimedeptid();
            if (StrUtil.isNotBlank(partTimeDepts) && blead) {
                for (String deptId : partTimeDepts.split(",")) {
                    TreeModel ptTreemodel = new TreeModel();
                    ptTreemodel.setId(tbemp.getId());
                    ptTreemodel.setIconSkin(iconSkin);
                    ptTreemodel.setParentId(Long.parseLong(deptId));
                    ptTreemodel.setName(tbemp.getSusername());
                    ptTreemodel.setNocheck(!showCheck);
                    treeModelList.add(ptTreemodel);
                    // 兼职对应父节点
                    List<Long> ptDeptIds = tbdeptService.getParentDeptId(Long.parseLong(deptId));
                    ptDeptIds.add(Long.parseLong(deptId));
                    List<Tbdept> ptDepts = new ArrayList<>();
                    for (Long ptDeptId : ptDeptIds) {
                        ptDepts.add(tbdeptService.getById(ptDeptId));
                    }
                    tbdeptService.deptConvertTreeModel(ptDepts, TreeModel.UNIT, false).forEach(treeModelList::add);
                }
            }
        }
        treeModelList = treeModelList.stream().distinct().collect(Collectors.toList());
        return treeModelList;
    }

    /**
     * 据bleader，取得管理的部门
     * 方法功能:据bleader，取得当前用户管理的部门（当前及以下）；非负责人（false）设所在部门
     *
     * @param employee
     * @return java.lang.String
     * @author yanz
     * @date 2023/10/30 12:45
     */
    @Override
    public String getAuthedDeptIdsByLeader(Tbemploy employee) {
        String authedDeptIds;
        if (employee.getBlead()) {
            //负责人 - 当前部门、兼职部门 及这两者下属所有子部门
            List<Long> childIds = new ArrayList<>();
            childIds.add(employee.getDeptid());
            if (!employee.getParttimedeptid().isEmpty()) {
                Stream.of(employee.getParttimedeptid().split(","))
                        .map(Long::parseLong)
                        .forEach(childIds::add);
            }
            childIds = childIds.stream()
                    .map(tbdeptService::getChildDeptId)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            authedDeptIds = String.join(",", childIds.stream().map(String::valueOf).toArray(String[]::new));

        } else {
            //普通用户 - 只看自己（所在部门）
            authedDeptIds = String.valueOf(employee.getDeptid());
        }
        return authedDeptIds;
    }

    /***
     * 通过部门id列表查询所有人员id
     * 方法功能: 通过部门id列表查询所有人员id，条件限制为 getBinner = true, getBuse = true
     *
     * @author yanz
     * @date 2023/11/3 15:56
     * @param queryDeptsIds
     * @return java.util.List<java.lang.Long>
     */
    public List<Long> getEmpIdsByDeptIds(List<Long> queryDeptsIds) {
        if (CollUtil.isEmpty(queryDeptsIds)) {
            return new ArrayList<>();
        }

        List<Tbemploy> list = this.lambdaQuery()
                .in(Tbemploy::getDeptid, queryDeptsIds)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .list().stream().collect(Collectors.toList());

        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(Tbemploy::getId).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    // 给getEmpTree用的
    private Set<Long> getEmpIdsByDeptIds(Set<Long> queryDeptsIds) {
        if (CollUtil.isEmpty(queryDeptsIds)) {
            return new HashSet<>();
        }

        Set<Tbemploy> set = (Set<Tbemploy>) this.lambdaQuery()
                .in(Tbemploy::getDeptid, queryDeptsIds)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .list().stream().collect(Collectors.toSet());

        if (CollUtil.isNotEmpty(set)) {
            return set.stream().map(Tbemploy::getId).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }


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
    @Override
    public List<Tbemploy> getEmpsListByDeptIds(Collection<Long> deptIdList, Collection<Long> notInIdList, Collection<Long> selectIdList) {
        List<Tbemploy> empList;
        if (deptIdList == null) {
            empList = new ArrayList<>();
        }
        empList = this.lambdaQuery()
                .in(deptIdList != null && deptIdList.size() > 0, Tbemploy::getDeptid, deptIdList)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .notIn(notInIdList != null && notInIdList.size() > 0, Tbemploy::getDeptid, notInIdList)
                .orderByAsc(Tbemploy::getIsort)
                .list();
        if (selectIdList == null || selectIdList.isEmpty()) {
            return empList;
        }
        empList.addAll(this.lambdaQuery()
                .in(selectIdList != null && selectIdList.size() > 0, Tbemploy::getDeptid, selectIdList)
                .eq(Tbemploy::getBinner, true)
                .eq(Tbemploy::getBuse, true)
                .notIn(notInIdList != null && notInIdList.size() > 0, Tbemploy::getDeptid, notInIdList)
                .orderByAsc(Tbemploy::getIsort)
                .list());
        return empList;
    }

    private Set<Long> getAuthedIds(LoginUser curUser) {
        return StrUtil.isNotBlank(curUser.getAuthedDeptIds()) ? Arrays.stream(curUser.getAuthedDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toSet()) : new HashSet<>();
    }

    private Set<Long> getManagedDepts(LoginUser curUser) {
        return StrUtil.isNotBlank(curUser.getSmanagedepts()) ? Arrays.stream(curUser.getSmanagedepts().split(",")).map(Long::parseLong).collect(Collectors.toSet()) : new HashSet<>();
    }


    private Set<Long> convertToSet(String str) {
        return StrUtil.isNotBlank(str) ? Arrays.stream(str.split(",")).map(Long::parseLong).collect(Collectors.toSet()) : new HashSet<>();
    }

    private void checkPermissions(Boolean bIgnorePermissions, LoginUser curUser, Set<Long> accessDepts, Set<Long> accessEmps) {
        // 管理、兼职管理、忽略权限：可访问所有
        if (bIgnorePermissions || ADMIN == curUser.getAdminflag() || PARTTIME_ADMIN == curUser.getAdminflag()) {
            accessDepts.addAll(tbdeptService.getRootDepts().stream().flatMap(dept -> tbdeptService.getChildDeptId(dept.getId()).stream()).collect(Collectors.toList()));
            accessEmps.addAll(getEmpIdsByDeptIds(accessDepts));
        } else {
            // 非管理员：可访问自己、所在部门、兼职部门
            accessDepts.addAll(getAuthedIds(curUser));
            if (curUser.getBlead() || DEPT_ADMIN == curUser.getAdminflag()) {
                accessDepts.addAll(getManagedDepts(curUser));
            } else {
                accessEmps.add(curUser.getUserid());
            }
        }
    }

    private List<TreeModel> getResults(TreeQuery query, Boolean bIgnorePermissions, LoginUser curUser, Set<Long> rdeptIds, Set<Long> rEmpIds, Set<Long> excludeNodes) throws Exception {
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            // 查询关键字
            return getQueryResult(query, rdeptIds, rEmpIds, excludeNodes, bIgnorePermissions || curUser.getBlead() || NORMAL_USER != curUser.getAdminflag(), curUser.getUserid()).stream().collect(Collectors.toList());
        } else {
            // 不查询，展示所有结果
            return getResult(rdeptIds, excludeNodes, bIgnorePermissions || curUser.getBlead() || NORMAL_USER != curUser.getAdminflag(), curUser.getUserid()).stream().collect(Collectors.toList());
        }
    }

    /**
     * 展示全部结果
     * 方法功能:展示全部结果
     *
     * @param rdeptIds
     * @param excludeNodes
     * @param isAdmin      是否领导/管理员
     * @param userId
     * @return java.util.Set<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/11/6 15:45
     */
    private Set<TreeModel> getResult(Set<Long> rdeptIds, Set<Long> excludeNodes, boolean isAdmin, Long userId) throws Exception {
        // 排除节点
        rdeptIds.removeAll(excludeNodes);
        Set<Long> rEmpIds = getEmpIdsByDeptIds(rdeptIds);
        rEmpIds.removeAll(excludeNodes);
        // 部门集合
        Set<Tbdept> rDepts = tbdeptService.getDeptByIds(new ArrayList<>(rdeptIds)).stream().collect(Collectors.toSet());
        // 部门的上级部门
        Set<Long> rDeptIdParentIdss = rDepts.stream().map(Tbdept::getId).collect(Collectors.toSet());

        rDeptIdParentIdss.addAll(rDeptIdParentIdss.stream().flatMap(deptId -> tbdeptService.getParentDeptId(deptId).stream()).collect(Collectors.toSet()));
        rDepts = tbdeptService.getDeptByIds(new ArrayList<>(rDeptIdParentIdss)).stream().collect(Collectors.toSet());
        // 管理员查询范围与普通人员不一样
        Set<Tbemploy> rEmps = isAdmin ? getEmpByIds(rEmpIds) : Arrays.asList(getById(userId)).stream().collect(Collectors.toSet());
        Set<TreeModel> result = new HashSet<>();
        // 结果
        result.addAll(tbdeptService.deptConvertTreeModel(rDepts.stream().collect(Collectors.toList()), TreeModel.UNIT, false));
        result.addAll(empConvertTreeModel(rEmps.stream().collect(Collectors.toList()), TreeModel.UNIT, true));
        return result;
    }

    /**
     * 按关键词查询
     * 方法功能:按关键词查询
     *
     * @param query
     * @param rdeptIds     权限内部门
     * @param rEmpIds      权限内员工
     * @param excludeNodes
     * @param isAdmin
     * @param userId
     * @return java.util.Set<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/11/6 15:34
     */
    private Set<TreeModel> getQueryResult(TreeQuery query, Set<Long> rdeptIds, Set<Long> rEmpIds, Set<Long> excludeNodes, boolean isAdmin, Long userId) throws Exception {
        Set<Long> queryNaviDeptIds = new HashSet<>();
        Set<Long> queryAuthedDeptIds = new HashSet<>();
        Set<Tbdept> deptList = tbdeptService.getDeptByIds(new ArrayList<>(rdeptIds)).stream().collect(Collectors.toSet());
        // 查询部门
        for (Tbdept dept : deptList) {
            if (dept.getSdeptname().contains(query.getQueryKey())) {
                queryAuthedDeptIds.add(dept.getId());
                queryNaviDeptIds.addAll(tbdeptService.getParentDeptId(dept.getId()));
            }
        }
        Set<Long> queryEmpIds = new HashSet<>(rEmpIds);
        if (isAdmin) {
            queryEmpIds.addAll(getEmpIdsByDeptIds(rdeptIds));
        } else {
            queryEmpIds.add(userId);
        }
        // 查询人员
        Set<Tbemploy> queryEmps = new HashSet<>(this.lambdaQuery()
                .like(Tbemploy::getSusername, query.getQueryKey())
                .in(Tbemploy::getId, queryEmpIds)
                .list());
        queryEmpIds = queryEmps.stream().map(Tbemploy::getId).collect(Collectors.toSet());
        queryEmpIds.addAll(getEmpIdsByDeptIds(queryAuthedDeptIds));
        queryEmpIds.removeAll(excludeNodes);
        queryEmps = getEmpByIds(queryEmpIds);
        // 人员的导航节点
        for (Tbemploy emp : queryEmps) {
            queryNaviDeptIds.addAll(tbdeptService.getParentDeptId(emp.getDeptid()));
        }
        Set<Tbdept> queryNaviDepts = new HashSet<>(tbdeptService.getDeptByIds(new ArrayList<>(queryNaviDeptIds)));
        Set<TreeModel> queryResult = new HashSet<>();
        // 汇总结果
        queryResult.addAll(tbdeptService.deptConvertTreeModel(new ArrayList<>(queryNaviDepts), TreeModel.UNIT, false));
        queryResult.addAll(empConvertTreeModel(new ArrayList<>(queryEmps), TreeModel.UNIT, true));
        return queryResult;
    }

    private Set<Tbemploy> getEmpByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return new HashSet<>();
        }
        return this.lambdaQuery().in(Tbemploy::getId, new HashSet<>(ids)).list().stream().collect(Collectors.toSet());
    }

    /***
     * 剥离部门、人员
     * 方法功能:剥离传入Id List成：部门（带子部门）的Id List、人员的Id List
     * @author yanz
     * @date 2023/10/31 15:11
     * @param ids
     * @param deptIds
     * @param empIds
     * @return void
     */
    private void splitIdSet(Set<Long> ids, Set<Long> deptIds, Set<Long> empIds) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        getEmpsListByEmpIds(ids, null, null).forEach(tbemploy -> empIds.add(tbemploy.getId()));
        deptIds.addAll(CollUtil.subtract(ids, empIds).stream().map(tbdeptService::getChildDeptId).flatMap(Collection::stream).collect(Collectors.toSet()));
    }


}
