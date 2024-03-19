
package com.hgzp.advertising.service.flow;


import com.hgzp.common.flowable.dto.*;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.core.page.Json;

import java.util.List;
import java.util.Map;

/**
 * 远程调用的接口
 */
public interface IRemoteService {
    /**
     * 根据部门id获取部门列表
     * @param deptIdList
     * @return
     */
    Json<List<DeptDto>> queryDeptList(List<String> deptIdList);
    /**
     * 保存待办任务
     * @param messageDto
     * @return
     */
    Json saveMessage(MessageDto messageDto);
    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     * @return
     */
    Json<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList);



    /**
     * 检查是否是所有的父级
     * @param checkParentDto
     * @return
     */
    Json<Boolean> checkIsAllParent(CheckParentDto checkParentDto);

    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    Json<List<String>> queryUserIdListByDepIdList(List<String> depIdList);

    /**
     * 检查是否是所有的子级
     * @param checkChildDto
     * @return
     */
    Json<Boolean> checkIsAllChild(CheckChildDto checkChildDto);

    /**
     * 获取用户的信息-包括扩展字段
     * @param userId
     * @return
     */
    Json<Map<String,Object>> queryUserAllInfo(String userId);

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     * @param userId
     * @return
     */
    Json<List<DeptDto>> queryParentDepListByUserId(String userId);

    Json<List<String>> replaceHolidayUserId(List<String> userIds);

    /**
     * 开始节点事件
     * @param recordParamDto
     * @return
     */
    Json startNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto);

    /**
     * 流程创建了
     * @param processInstanceRecordParamDto
     * @return
     */
    Json createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto);



    /**
     * 完成节点事件
     * @param recordParamDto
     * @return
     */
    Json endNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto);

    /**
     * 开始设置执行人
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    Json startAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);

    /**
     * 任务结束事件
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    Json taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto);

    /**
     * 实例结束
     *
     * @param processInstanceParamDto
     * @return
     */
    Json endProcess(ProcessInstanceParamDto processInstanceParamDto);


    /**
     * 流程审批结束之后回调
     * 方法功能: 流程审批结束之后对应的流程处理结果
     * @author wangwk
     * @date 2023/10/31 13:45
     * @param processInstanceParamDto
     * @return void
     */
    void handlerProcessResult(ProcessInstanceParamDto processInstanceParamDto);

    /**
     * 查询流程管理员
     *
     * @param flowId
     * @return
     */
    Json<String> queryProcessAdmin(String flowId);

    /**
     * 根据用户id获取部门信息，包括单位管理员
     * @param userId
     * @return
     */
    Json<DeptDto> queryDeptByUserId(String userId);
}
