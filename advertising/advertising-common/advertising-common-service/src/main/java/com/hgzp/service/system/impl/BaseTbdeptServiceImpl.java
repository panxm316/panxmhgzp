package com.hgzp.service.system.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbdeptex;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.system.TbdeptMapper;
import com.hgzp.mapper.system.TbdeptexMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.service.system.BaseTbdeptServiceI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbdeptServiceImpl extends MyServiceImpl<TbdeptMapper, Tbdept> implements BaseTbdeptServiceI {

    public static final int MAX_IDEPTH = 5;

    @Autowired
    TbdeptMapper tbdeptMapper;
    @Autowired
    TbdeptexMapper tbdeptexMapper;

    /**
     * deptConvertTreeModel
     * 方法功能:  将数据库部门对象转换成嵌套的树对象
     * @author wangwk
     * @date 2023/8/18 13:41
     * @param tbdeptList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    private List<TreeModel> deptConvertTreeModel(List<Tbdept> tbdeptList){
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbdept tbdept : tbdeptList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbdept.getId());
            treeModel.setParentId(tbdept.getParentid());
            treeModel.setName(tbdept.getSdeptname());
            treeModelList.add(treeModel);
        }
        return TreeModel.buildTreeModel(treeModelList);
    }


    /**
     * getChildDeptId
     * 方法功能: 获取某个部门的子部门id，包含当前节点
     * @author wangwk
     * @date 2023/8/23 14:27
     * @param deptId
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public List<Long> getChildDeptId(Long deptId){
        Tbdept tbdept = tbdeptMapper.selectById(deptId);
        if(tbdept == null){
            return new ArrayList<>();
        }
        //子节点id
        List<Long> childIdList = new QueryChainWrapper<>(tbdeptexMapper)
                .eq("parentid" + tbdept.getIdepth(), tbdept.getId())
                .list()
                .stream()
                .map(Tbdeptex::getId)
                .collect(Collectors.toList());
        return childIdList;
    }

    /**
     * getParentDeptId
     * 方法功能:  获取某个部门的父部门id，不包含当前节点
     * @author wangwk
     * @date 2023/8/23 14:31
     * @param deptId
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public List<Long> getParentDeptId(Long deptId){
        Tbdeptex tbdeptex = tbdeptexMapper.selectById(deptId);
        List<Long> parentIdList = new ArrayList<>();
        for (int i = 1; i <= MAX_IDEPTH; i++) {
            Long parentId = ReflectUtil.invoke(tbdeptex, "getParentid" + i);
            if (parentId != 0 && !deptId.equals(parentId)) {
                parentIdList.add(parentId);
            }
        }
        return parentIdList;
    }




}
