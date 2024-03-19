package com.hgzp.service.system;

import com.hgzp.core.model.Tbdept;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbdeptServiceI extends IMyService<Tbdept> {


    /**
     * getChildDeptId
     * 方法功能: 获取某个部门的子部门id，包含当前节点
     * @author wangwk
     * @date 2023/8/23 14:27
     * @param deptId
     * @return java.util.List<java.lang.Long>
     */
    List<Long> getChildDeptId(Long deptId);


    /**
     * getParentDeptId
     * 方法功能:  获取某个部门的父部门id，不包含当前节点
     * @author wangwk
     * @date 2023/8/23 14:31
     * @param deptId
     * @return java.util.List<java.lang.Long>
     */
    List<Long> getParentDeptId(Long deptId);
}
