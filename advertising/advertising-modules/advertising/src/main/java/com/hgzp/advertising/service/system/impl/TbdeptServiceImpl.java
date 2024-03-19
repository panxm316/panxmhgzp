package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Twtasks;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.mapper.system.TbdeptMapper;
import com.hgzp.mapper.system.TbdeptexMapper;
import com.hgzp.service.system.impl.BaseTbdeptServiceImpl;
import com.hgzp.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbdeptServiceImpl extends BaseTbdeptServiceImpl implements TbdeptServiceI {
    @Autowired
    TbdeptMapper tbdeptMapper;
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
    @Override
    public List<TreeModel> getDeptTree(TreeQuery query, boolean showUnuseDept) throws Exception {
        HttpServletRequest httpServletRequest = WebUtil.getHttpServletRequest();
        EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO) httpServletRequest.getAttribute(SecurityConstants.ROLE_PERMISSION);
        List<TreeModel> result = new ArrayList<>();
        List<Long> authedNodes = empAuthorityDTO.getDeptIdList();
        List<Long> parentDeptIdsOfAuthedNodes = authedNodes.stream().map(this::getParentDeptId).flatMap(List::stream).distinct().collect(Collectors.toList());
        // 全部权限
        if (authedNodes.isEmpty()) {
            List<Long> deptIds = getRootDepts().stream().map(Tbdept::getId).collect(Collectors.toList());
            authedNodes.addAll(deptIds.stream().map(this::getChildDeptId).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
            parentDeptIdsOfAuthedNodes = Collections.emptyList();
        }

        //根据关键字查询出符合的部门id
        List<Long> queryDeptId = StrUtil.isNotBlank(query.getQueryKey()) ? getQueryDeptId(query.getQueryKey()) : new ArrayList<>();
        if (!queryDeptId.isEmpty()) {
            queryDeptId.add(0L);
        }

        //要排除的部门
        List<Long> excludeIdList = StrUtil.isNotBlank(query.getExcludeIds()) ?
                Arrays.stream(query.getExcludeIds().split(","))
                        .map(Long::valueOf)
                        .map(this::getChildDeptId)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList()) : Collections.emptyList();

        //已经选择的部门
        List<Long> selectIdList = StrUtil.isNotBlank(query.getContainsIds()) ?
                Arrays.stream(query.getContainsIds().split(",")).map(Long::valueOf).collect(Collectors.toList()) : new ArrayList<>();

        // 已授权部门并入已选择部门
        selectIdList.addAll(authedNodes);

        if (!queryDeptId.isEmpty() && !selectIdList.isEmpty()) {
            selectIdList = CollUtil.intersectionDistinct(selectIdList, queryDeptId).stream().collect(Collectors.toList());
        }

        List<Long> selectIdParentIdList = selectIdList.stream().map(this::getParentDeptId).flatMap(Collection::stream).collect(Collectors.toList());
        selectIdList.addAll(selectIdParentIdList);
        selectIdList.removeAll(excludeIdList);

        List<Tbdept> tbdeptList = getDeptByIds(selectIdList);
        List<Tbdept> parentDeptListOfAuthedNodes = getDeptByIds(parentDeptIdsOfAuthedNodes);
        if (!showUnuseDept) {
            // 仅保留已启用部门
            tbdeptList = tbdeptList.stream().filter(Tbdept::getBuse).collect(Collectors.toList());
            parentDeptListOfAuthedNodes = parentDeptListOfAuthedNodes.stream().filter(Tbdept::getBuse).collect(Collectors.toList());
        }
        // 分开authedNodes的父节点，授权节点父部门转TreeModel时设置false选中
        tbdeptList.removeAll(parentDeptListOfAuthedNodes);

        result.addAll(deptConvertTreeModel(parentDeptListOfAuthedNodes, TreeModel.UNIT, false));
        result.addAll(deptConvertTreeModel(tbdeptList, TreeModel.UNIT, true));

        return query.isShowRoot() ? addRootDept(result) : result;
    }

    private Set<Long> convertToSet(String str) {
        return StrUtil.isNotBlank(str) ? Arrays.stream(str.split(",")).map(Long::parseLong).collect(Collectors.toSet()) : new HashSet<>();
    }

    /**
     * getQueryDeptId
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/8/29 13:03
     */
    public List<Long> getQueryDeptId(String querykey) {
        List<Long> resultIdList = new ArrayList<>();
        List<Long> idList = this.lambdaQuery()
                .like(Tbdept::getSdeptname, querykey)
                .list()
                .stream()
                .map(Tbdept::getId)
                .collect(Collectors.toList());

        for (Long id : idList) {
            List<Long> parentDeptId = getParentDeptId(id);
            resultIdList.addAll(parentDeptId);
            resultIdList.add(id);
        }

        return resultIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * getQueryDeptIdWithoutParent
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/8/29 13:03
     */
    public List<Long> getQueryDeptIdWithoutParent(String querykey) {
        List<Long> idList = this.lambdaQuery()
                .like(Tbdept::getSdeptname, querykey)
                .list()
                .stream()
                .map(Tbdept::getId)
                .collect(Collectors.toList());
        return idList.stream().distinct().collect(Collectors.toList());
    }

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
    public List<Tbdept> getDeptListByQuery(Collection<Long> containsIdList, boolean showUnuseDept, Collection<Long> notInIdList, Collection<Long> selectIdList) {
        List<Tbdept> deptList = this.lambdaQuery()
                .and(containsIdList.size() > 0, i ->
                        i.in(containsIdList.size() > 0, Tbdept::getId, containsIdList)
                                .or()
                                .in(selectIdList.size() > 0, Tbdept::getId, selectIdList))
                // .in(containsIdList.size() > 0, Tbdept::getId, containsIdList)
                .notIn(notInIdList.size() > 0, Tbdept::getId, notInIdList)
                .eq(Tbdept::getBinner, true)
                .eq(!showUnuseDept, Tbdept::getBuse, true)
                .orderByAsc(Tbdept::getIsort)
                .list();
        return deptList;
    }

    /**
     * deptConvertTreeModel
     * 方法功能:  将数据库部门对象转换成树对象
     *
     * @param tbdeptList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/18 13:41
     */
    public List<TreeModel> deptConvertTreeModel(List<Tbdept> tbdeptList, String iconSkin, boolean showCheck) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbdept tbdept : tbdeptList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbdept.getId());
            treeModel.setIconSkin(iconSkin);
            treeModel.setParentId(tbdept.getParentid());
            treeModel.setName(tbdept.getSdeptname());
            treeModel.setNocheck(!showCheck);
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    /**
     * 部门树添加[全部部门]节点
     * 方法功能:部门树添加[全部部门]节点，不带选中框
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author yanz
     * @date 2023/11/7 15:57
     */
    public List<TreeModel> addRootDeptWithoutCheck(List<TreeModel> treeModelList) {
        for (TreeModel treeModel : treeModelList) {
            if (treeModel.getParentId() == null) {
                treeModel.setParentId(0L);
            }
        }
        //添加一个根节点
        TreeModel root = new TreeModel();
        root.setId(0L);
        root.setName("所有部门");
        root.setChecked(false);
        root.setNocheck(true);
        root.setIconSkin(TreeModel.UNIT);
        treeModelList.add(root);
        return treeModelList;
    }

    /**
     * addRootDept
     * 方法功能: 部门树添加[全部部门]节点
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/29 13:17
     */
    public List<TreeModel> addRootDept(List<TreeModel> treeModelList) {
        for (TreeModel treeModel : treeModelList) {
            if (treeModel.getParentId() == null) {
                treeModel.setParentId(0L);
            }
        }
        //添加一个根节点
        TreeModel root = new TreeModel();
        root.setId(0L);
        root.setName("所有部门");
        root.setChecked(false);
        root.setNocheck(false);
        root.setIconSkin(TreeModel.UNIT);
        treeModelList.add(root);
        return treeModelList;
    }

    /**
     * 取得所有根部门
     * 方法功能:取得所有根部门
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     * @author yanz
     * @date 2023/10/28 15:14
     */
    @Override
    public List<Tbdept> getRootDepts() {
        List<Tbdept> rootDeptsList = this.lambdaQuery()
                .eq(Tbdept::getParentid, 0)
                .eq(Tbdept::getBinner, true)
                .eq(Tbdept::getBuse, true)
                .orderByAsc(Tbdept::getIsort)
                .list();
        return rootDeptsList;
    }

    /***
     * 按 id 集合取得部门列表
     * 方法功能:按 id 集合取得部门列表
     *
     * @author yanz
     * @date 2023/11/3 13:07
     * @param ids
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     */
    @Override
    public List<Tbdept> getDeptByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().in(Tbdept::getId, ids).list();
    }

    @Override
    public List<TreeModel> getLeafDeptTree(TreeQuery query) throws Exception {
        LambdaQueryWrapper<Tbdept> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbdept::getBuse, true);
        List<Tbdept> tbdeptList = tbdeptMapper.selectList(lqw);
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbdept tbdept : tbdeptList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbdept.getId());
            treeModel.setIconSkin(TreeModel.UNIT);
            treeModel.setParentId(tbdept.getParentid());
            treeModel.setName(tbdept.getSdeptname());
            if( isLeafNode(tbdept.getId()) ) {
                treeModel.setNocheck(false);
            } else {
                treeModel.setNocheck(true);
            }
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    public boolean isLeafNode(Long deptId) {
        LambdaQueryWrapper<Tbdept> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbdept::getParentid, deptId);
        long  count = tbdeptMapper.selectCount(lqw);
        if( count > 0 )
            return false;
        return true;
    }

}
