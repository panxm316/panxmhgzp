package com.hgzp.advertising.service.flow.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.hgzp.advertising.service.flow.*;
import com.hgzp.advertising.utils.DataUtil;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.dto.*;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.page.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hgzp.core.model.Process;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RemoteServiceImpl implements IRemoteService {


    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    @Resource
    private IProcessInstanceNodeRecordService processNodeRecordService;
    @Resource
    private IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Resource
    private IProcessService processService;



    /**
     * 根据部门id获取部门列表
     *
     * @param deptIdList
     * @return
     */
    @Override
    public Json<List<DeptDto>> queryDeptList(List<String> deptIdList) {
        List<com.hgzp.common.flowable.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<DeptDto> collect = allDept.stream().filter(w -> deptIdList.contains(w.getId())).collect(Collectors.toList());
        return Json.success(collect);
    }

    /**
     * 保存待办任务
     *
     * @param messageDto
     * @return
     */
    @Override
    public Json saveMessage(MessageDto messageDto) {

        ApiStrategyFactory.getStrategy().sendMsg(messageDto);
        return Json.success();
    }
    /**
     * 根据角色id集合查询用户id集合
     *
     * @param roleIdList
     * @return
     */
    @Override
    public Json<List<String>> queryUserIdListByRoleIdList(List<String> roleIdList) {

        List<String> userIdList = ApiStrategyFactory.getStrategy().loadUserIdListByRoleIdList(roleIdList);


        return Json.success(userIdList);
    }



    /**
     * 检查是否是所有的父级
     *
     * @param checkParentDto
     * @return
     */
    @Override
    public Json<Boolean> checkIsAllParent(CheckParentDto checkParentDto) {

        String parentId = checkParentDto.getParentId();
        List<String> deptIdList = checkParentDto.getDeptIdList();
        //查询子级包括自己
        List<com.hgzp.common.flowable.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.hgzp.common.flowable.dto.third.DeptDto> childrenDeptList = DataUtil.selectChildrenByDept(parentId,allDept);


        List<String> childrenDeptIdList = childrenDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        childrenDeptIdList.remove(parentId);

        List<String> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(childrenDeptIdList, String.class));

        return Json.success(remainIdList.isEmpty());
    }

    /**
     * 根据部门id集合查询用户id集合
     *
     * @param depIdList
     * @return
     */
    @Override
    public Json<List<String>> queryUserIdListByDepIdList(List<String> depIdList) {

        List<String> userIdList = ApiStrategyFactory.getStrategy().loadUserIdListByDeptIdList(depIdList);
        return Json.success(userIdList);
    }

    /**
     * 检查是否是所有的子级
     *
     * @param checkChildDto
     * @return
     */
    @Override
    public Json<Boolean> checkIsAllChild(CheckChildDto checkChildDto) {
        String childId = checkChildDto.getChildId();
        List<String> deptIdList = checkChildDto.getDeptIdList();
        //查询父级包括自己

        List<com.hgzp.common.flowable.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.hgzp.common.flowable.dto.third.DeptDto> parentDeptList = DataUtil.selectParentByDept(childId, allDept);

        List<String> parentDeptIdList = parentDeptList.stream().map(w -> w.getId()).collect(Collectors.toList());
        parentDeptIdList.remove(childId);

        List<String> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(parentDeptIdList, String.class));

        return Json.success(remainIdList.isEmpty());
    }

    /**
     * 获取用户的信息-包括扩展字段
     *
     * @param userId
     * @return
     */
    @Override
    public Json<Map<String, Object>> queryUserAllInfo(String userId) {

        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);

        Map<String, Object> map = BeanUtil.beanToMap(user, "id", "name", "phone", "gender", "deptId", "entryDate"
                );

        return Json.success(map);
    }

    /**
     * 根据当前用户查询包括自己部门在内的上级部门对象
     *
     * @param userId
     * @return
     */
    @Override
    public Json<List<DeptDto>> queryParentDepListByUserId(String userId) {
        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);
        String deptId = user.getDeptId();

        List<com.hgzp.common.flowable.dto.third.DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
        List<com.hgzp.common.flowable.dto.third.DeptDto> deptList = DataUtil.selectParentByDept(deptId,allDept);

        return Json.success(deptList);
    }

    @Override
    public Json<DeptDto> queryDeptByUserId(String userId) {
        DeptDto deptDto = ApiStrategyFactory.getStrategy().queryDeptByUserId(userId);
        return Json.success(deptDto);
    }

    @Override
    public Json<List<String>> replaceHolidayUserId(List<String> userIds){
        List<String> replaceHolidayUserId = ApiStrategyFactory.getStrategy().replaceHolidayUserId(userIds);
        return Json.success(replaceHolidayUserId);
    }

    /**
     * 开始节点事件
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public Json startNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto) {
        return processNodeRecordService.start(recordParamDto);
    }

    /**
     * 流程创建了
     *
     * @param processInstanceRecordParamDto
     * @return
     */
    @Override
    public Json createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
        ProcessInstanceRecord entity = BeanUtil.copyProperties(processInstanceRecordParamDto,
                ProcessInstanceRecord.class);


        Process oaForms = processService.getByFlowId(processInstanceRecordParamDto.getFlowId());

        FlowTypes flowType = FlowTypes.getTypeByKey(oaForms.getGroupId());

        entity.setName(oaForms.getName());
        entity.setLogo(oaForms.getLogo());
        entity.setUserId(processInstanceRecordParamDto.getUserId());
        entity.setFlowId(processInstanceRecordParamDto.getFlowId());
        entity.setProcessInstanceId(processInstanceRecordParamDto.getProcessInstanceId());
        entity.setGroupId(flowType.getKey());
        entity.setGroupName(flowType.getName());
        entity.setStatus(NodeStatusEnum.JXZ.getCode());

        processInstanceRecordService.save(entity);

        return Json.success();
    }

    /**
     * 完成节点事件
     *
     * @param recordParamDto
     * @return
     */
    @Override
    public Json endNodeEvent(ProcessInstanceNodeRecordParamDto recordParamDto) {
        return processNodeRecordService.complete(recordParamDto);
    }

    /**
     * 开始设置执行人
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public Json startAssignUser(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.addAssignUser(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 任务结束事件
     *
     * @param processInstanceAssignUserRecordParamDto
     * @return
     */
    @Override
    public Json taskEndEvent(ProcessInstanceAssignUserRecordParamDto processInstanceAssignUserRecordParamDto) {
        return processNodeRecordAssignUserService.completeTaskEvent(processInstanceAssignUserRecordParamDto);
    }

    /**
     * 实例结束
     *
     * @param processInstanceParamDto
     * @return
     */
    @Override
    public Json endProcess(ProcessInstanceParamDto processInstanceParamDto) {
        return processInstanceService.end(processInstanceParamDto);
    }


    /**
     * 流程审批结束之后回调
     * 方法功能: 流程审批结束之后对应的流程处理结果
     * @author wangwk
     * @date 2023/10/31 13:45
     * @param processInstanceParamDto
     * @return void
     */
    @Override
    public void handlerProcessResult(ProcessInstanceParamDto processInstanceParamDto) {
        processInstanceService.handlerProcessResult(processInstanceParamDto);
    }


        /**
         * 查询流程管理员
         *
         * @param flowId
         * @return
         */
    @Override
    public Json<String> queryProcessAdmin(String flowId) {
        Process process = processService.getByFlowId(flowId);
        return Json.success(process.getAdminId());
    }


}
