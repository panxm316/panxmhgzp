package com.hgzp.advertising.controller.flow;

import com.hgzp.advertising.service.flow.IRemoteService;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.core.annotation.InnerAuth;
import com.hgzp.core.page.Json;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * RemoteController
 * 创建人：wwk
 * 类描述：远程请求控制器controller
 * 创建日期：2023/8/23 13:09
 * @folder flow/RemoteController
 */
@Slf4j
@RestController
@RequestMapping("/flow/remote")
public class RemoteController {
    @Resource
    private IRemoteService remoteService;

    /**
     * 根据部门id获取部门列表
     * 方法功能: 根据部门id获取部门列表
     * @author wwk
     * @date 2023/10/27 11:22
     * @param deptIdList
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.common.flowable.dto.third.DeptDto>>
     */
    @InnerAuth
    @PostMapping("queryDeptList")
    public Json<List<DeptDto>> queryDeptList(@RequestBody List<String> deptIdList){
        return remoteService.queryDeptList(deptIdList);
    }

    /**
     * 保存待办任务
     * 方法功能: 保存待办任务
     * @author wwk
     * @date 2023/10/27 11:22
     * @param messageDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("saveMessage")
    public Json saveMessage(@RequestBody MessageDto messageDto) {
        return remoteService.saveMessage(messageDto);
    }

    /**
     * 根据角色id集合查询用户id集合
     * 方法功能: 根据角色id集合查询用户id集合
     * @author wwk
     * @date 2023/10/27 11:23
     * @param roleIdList
     * @return com.hgzp.core.page.Json<java.util.List<java.lang.String>>
     */
    @InnerAuth
    @PostMapping("queryUserIdListByRoleIdList")
    public Json<List<String>> queryUserIdListByRoleIdList(@RequestBody List<String> roleIdList) {
        return remoteService.queryUserIdListByRoleIdList(roleIdList);
    }

    /**
     * 检查是否是所有的父级
     * 方法功能: 检查是否是所有的父级
     * @author wwk
     * @date 2023/10/27 11:23
     * @param checkParentDto
     * @return com.hgzp.core.page.Json<java.lang.Boolean>
     */
    @InnerAuth
    @PostMapping("checkIsAllParent")
    public Json<Boolean> checkIsAllParent(@RequestBody CheckParentDto checkParentDto) {
        return remoteService.checkIsAllParent(checkParentDto);
    }

    /**
     * 根据部门id集合查询用户id集合
     * 方法功能: 根据部门id集合查询用户id集合
     * @author wwk
     * @date 2023/10/27 11:23
     * @param depIdList
     * @return com.hgzp.core.page.Json<java.util.List<java.lang.String>>
     */
    @InnerAuth
    @PostMapping("queryUserIdListByDepIdList")
    public Json<List<String>> queryUserIdListByDepIdList(@RequestBody List<String> depIdList) {
        return remoteService.queryUserIdListByDepIdList(depIdList);
    }

    /**
     * 检查是否是所有的子级
     * 方法功能: 检查是否是所有的子级
     * @author wangwk
     * @date 2023/10/27 11:24
     * @param checkChildDto
     * @return com.hgzp.core.page.Json<java.lang.Boolean>
     */
    @InnerAuth
    @PostMapping("checkIsAllChild")
    public Json<Boolean> checkIsAllChild(@RequestBody CheckChildDto checkChildDto) {
        return remoteService.checkIsAllChild(checkChildDto);
    }

    /**
     * 获取用户的信息-包括扩展字段
     * 方法功能: 获取用户的信息-包括扩展字段
     * @author wangwk
     * @date 2023/10/27 11:24
     * @param userId
     * @return com.hgzp.core.page.Json<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @InnerAuth
    @GetMapping("queryUserAllInfo")
    public Json<Map<String, Object>> queryUserAllInfo(String userId) {
        return remoteService.queryUserAllInfo(userId);
    }

    /**
     * 根据用户id查询部门信息包括部门主任
     * 方法功能:  根据用户id查询部门信息包括部门主任
     * @author wangwk
     * @date 2023/10/13 16:50
     * @param userId
     * @return com.hgzp.core.page.Json<com.hgzp.common.flowable.dto.third.DeptDto>
     */
    @InnerAuth
    @GetMapping("queryDeptByUserId")
    public Json<DeptDto> queryDeptByUserId(String userId) {
        return remoteService.queryDeptByUserId(userId);
    }

    /**
     * 根据用户id查询上级部门列表
     * 方法功能: 根据用户id查询上级部门列表
     * @author wangwk
     * @date 2023/10/27 11:25
     * @param userId
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.common.flowable.dto.third.DeptDto>>
     */
    @InnerAuth
    @SneakyThrows
    @GetMapping("queryParentDepListByUserId")
    public Json<List<DeptDto>> queryParentDepListByUserId(String userId) {

        return remoteService.queryParentDepListByUserId(userId);
    }

    /**
     * 开始节点事件
     * 方法功能: 开始节点事件
     * @author wangwk
     * @date 2023/10/27 11:25
     * @param recordParamDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("startNodeEvent")
    public Json startNodeEvent(@RequestBody ProcessInstanceNodeRecordParamDto recordParamDto) {
        return remoteService.startNodeEvent(recordParamDto);
    }

    /**
     * 流程创建
     * 方法功能: 流程创建
     * @author wangwk
     * @date 2023/10/27 11:25
     * @param processInstanceRecordParamDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("createProcessEvent")
    public Json createProcessEvent(@RequestBody ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        return remoteService.createProcessEvent(processInstanceRecordParamDto);
    }

    /**
     * 结束节点事件
     * 方法功能: 结束节点事件
     * @author wangwk
     * @date 2023/10/27 11:25
     * @param recordParamDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("endNodeEvent")
    public Json endNodeEvent(@RequestBody ProcessInstanceNodeRecordParamDto recordParamDto) {
        return remoteService.endNodeEvent(recordParamDto);
    }

   /**
    * 开始设置执行人
    * 方法功能: 开始设置执行人
    * @author wangwk
    * @date 2023/10/27 11:26
    * @param processInstanceAssignUserRecordParamDto
    * @return com.hgzp.core.page.Json
    */
    @InnerAuth
    @PostMapping("startAssignUser")
    public Json startAssignUser(@RequestBody ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return remoteService.startAssignUser(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 任务结束事件
     * 方法功能: 任务结束事件
     * @author wangwk
     * @date 2023/10/27 11:26
     * @param processInstanceAssignUserRecordParamDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("taskEndEvent")
    public Json taskEndEvent(@RequestBody ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return remoteService.taskEndEvent(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 实例结束
     * 方法功能: 实例结束
     * @author wangwk
     * @date 2023/10/27 11:26
     * @param processInstanceParamDto
     * @return com.hgzp.core.page.Json
     */
    @InnerAuth
    @PostMapping("endProcess")
    public Json endProcess(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
        Json json = remoteService.endProcess(processInstanceParamDto);
        try {
            remoteService.handlerProcessResult(processInstanceParamDto);
        } catch (Exception e) {
            log.error("流程审批结束之后回调异常", e);
        }
        return json;
    }

    /**
     * 查询流程管理员
     * 方法功能: 查询流程管理员
     * @author wangwk
     * @date 2023/10/27 11:26
     * @param flowId
     * @return com.hgzp.core.page.Json<java.lang.String>
     */
    @InnerAuth
    @GetMapping("queryProcessAdmin")
    public Json<String> queryProcessAdmin(String flowId) {
        return remoteService.queryProcessAdmin(flowId);
    }

    /**
     * 替换休假的人员
     * 方法功能: 替换休假的人员
     * @author wangwk
     * @date 2023/10/23 17:22
     * @param userIdList
     * @return com.hgzp.core.page.Json<java.util.List<java.lang.String>>
     */
    @InnerAuth
    @PostMapping("replaceHolidayUser")
    public Json<List<String>> replaceHolidayUser(@RequestBody List<String> userIdList) {
        return remoteService.replaceHolidayUserId(userIdList);
    }

}
