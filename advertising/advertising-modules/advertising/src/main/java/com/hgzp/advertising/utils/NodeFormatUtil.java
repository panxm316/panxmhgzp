package com.hgzp.advertising.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.advertising.pagemodel.flow.NodeVo;
import com.hgzp.advertising.pagemodel.flow.UserVo;
import com.hgzp.advertising.service.flow.IProcessInstanceAssignUserRecordService;
import com.hgzp.advertising.service.flow.IProcessInstanceRecordService;
import com.hgzp.advertising.service.flow.IRemoteService;
import com.hgzp.common.flowable.constants.NodeStatusEnum;
import com.hgzp.common.flowable.constants.NodeTypeEnum;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.constants.ProcessInstanceConstant;
import com.hgzp.common.flowable.dto.R;
import com.hgzp.common.flowable.dto.flow.Node;
import com.hgzp.common.flowable.dto.flow.NodeUser;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.model.ProcessInstanceAssignUserRecord;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.common.flowable.utils.NodeUtil;
import com.hgzp.core.page.Json;
import com.hgzp.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 节点格式化显示工具
 */
public class NodeFormatUtil {

    /**
     * 格式化流程节点显示
     *
     * @param node
     * @param completeNodeSet
     * @param continueNodeSet
     * @param processInstanceId
     * @param paramMap
     */
    public static List<NodeVo> formatProcessNodeShow(Node node,
                                                     Set<String> completeNodeSet,
                                                     Set<String> continueNodeSet,
                                                     String processInstanceId,
                                                     Map<String, Object> paramMap) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();


        List<NodeVo> list = new ArrayList();

        if (!NodeUtil.isNode(node)) {
            return list;
        }

        String name = node.getNodeName();
        Integer type = node.getType();


        //SELF_SELECT


        NodeVo nodeVo = new NodeVo();
        nodeVo.setId(node.getId());
        nodeVo.setName(name);
        nodeVo.setType(type);
        nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
        if (completeNodeSet.contains(node.getId())) {
            nodeVo.setStatus(NodeStatusEnum.YJS.getCode());

        }
        if (continueNodeSet.contains(node.getId())) {
            nodeVo.setStatus(NodeStatusEnum.JXZ.getCode());

        }

        {

            nodeVo.setPlaceholder(node.getPlaceHolder());

        }


        List<UserVo> userVoList = new ArrayList<>();
        if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

            Integer assignedType = node.getAssignedType();

            boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            nodeVo.setSelectUser(selfSelect);
            if (selfSelect) {
                nodeVo.setMultiple(node.getMultiple());
            }

            // 用户列表
            if (StrUtil.isNotBlank(processInstanceId)) {
                IProcessInstanceAssignUserRecordService processNodeRecordAssignUserService = SpringUtil.getBean(IProcessInstanceAssignUserRecordService.class);
                List<ProcessInstanceAssignUserRecord> processInstanceAssignUserRecordList = processNodeRecordAssignUserService
                        .lambdaQuery().
                        eq(ProcessInstanceAssignUserRecord::getNodeId, node.getId())
                        .eq(ProcessInstanceAssignUserRecord::getProcessInstanceId, processInstanceId)
                        .orderByAsc(ProcessInstanceAssignUserRecord::getCreateTime)
                        .list();
                Map<String, List<ProcessInstanceAssignUserRecord>> map = processInstanceAssignUserRecordList.stream().collect(Collectors.groupingBy(w -> w.getTaskId()));

                for (Map.Entry<String, List<ProcessInstanceAssignUserRecord>> entry : map.entrySet()) {
                    List<ProcessInstanceAssignUserRecord> value = entry.getValue();
                    List<UserVo> collect = value.stream().filter(w -> !ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN.equals(w.getUserId())).map(w -> {
                        UserVo userVo = buildUser((w.getUserId()));
                        userVo.setShowTime(w.getEndTime());
                        userVo.setApproveDesc(w.getApproveDesc());
                        userVo.setStatus(w.getStatus());
                        userVo.setOperType(w.getTaskType());
                        return userVo;
                    }).collect(Collectors.toList());
                    userVoList.addAll(collect);

                }

                if (processInstanceAssignUserRecordList.isEmpty()) {
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                        //发起人自己
                        userVoList.addAll(CollUtil.newArrayList(buildRootUser(processInstanceId)));
                    }
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {
                        //发起人自选
                        Object variable = paramMap.get(StrUtil.format("{}_assignee_select", node.getId()));
                        List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

                        List<String> collect = nodeUserDtos.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (String aLong : collect) {
                            UserVo userVo = buildUser(aLong);
                            userVoList.addAll(CollUtil.newArrayList(userVo));
                        }
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
                //指定用户

                List<NodeUser> nodeUserList = node.getNodeUserList();
                List<UserVo> tempList = buildUser(nodeUserList);
                userVoList.addAll(tempList);


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_USER) {
                //表单人员
                String formUser = node.getFormUserId();

                Object o = paramMap.get(formUser);
                if (o != null) {
                    String jsonString = JSON.toJSONString(o);
                    if (StrUtil.isNotBlank(jsonString)) {
                        List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
                        List<String> userIdList =
                                nodeUserDtoList.stream().map(w -> (w.getId())).collect(Collectors.toList());
                        for (String aLong : userIdList) {
                            userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
                        }
                    }
                }


            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                //发起人自己
                userVoList.addAll(CollUtil.newArrayList(buildUser(userId)));
            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER) {
                //制定主管

                //指定主管审批
                //第几级主管审批
                Integer level = node.getDeptLeaderLevel();

                //去获取主管

                IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

                Json<List<DeptDto>> r = remoteService.queryParentDepListByUserId(userId);

                List<DeptDto> deptDtoList = r.getObj();
                if (CollUtil.isNotEmpty(deptDtoList)) {
                    if (deptDtoList.size() >= level) {
                        DeptDto deptDto = deptDtoList.get(level - 1);

                        List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                        for (String s : leaderUserIdList) {
                            UserVo userVo = buildUser(s);
                            userVoList.add(userVo);
                        }


                    }
                }

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.LEADER_TOP) {

                //指定主管审批
                //第几级主管审批
                Integer level = node.getDeptLeaderLevel();

                //去获取主管

                IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

                Json<List<DeptDto>> r = remoteService.queryParentDepListByUserId(userId);

                List<DeptDto> deptDtoList = r.getObj();


                if (CollUtil.isNotEmpty(deptDtoList)) {
                    int index = 1;
                    for (DeptDto deptDto : deptDtoList) {
                        if (level != null && level < index) {
                            break;
                        }

                        List<String> leaderUserIdList = deptDto.getLeaderUserIdList();
                        for (String s : leaderUserIdList) {
                            UserVo userVo = buildUser(s);
                            userVoList.add(userVo);
                        }


                        index++;
                    }
                }
            }


        } else if (node.getType() == NodeTypeEnum.ROOT.getValue().intValue()) {
            //发起节点
            if (StrUtil.isBlank(processInstanceId)) {
                UserVo userVo = buildUser(userId);

                userVoList.addAll(CollUtil.newArrayList(userVo));

            } else {

                IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();


                UserVo userVo = buildRootUser(processInstanceId);
                userVo.setShowTime(processInstanceRecord.getCreateTime());
                userVo.setStatus(NodeStatusEnum.YJS.getCode());
                userVoList.addAll(CollUtil.newArrayList(userVo));

            }
        } else if (node.getType() == NodeTypeEnum.CC.getValue().intValue()) {
            //抄送节点

            List<NodeUser> nodeUserList = node.getNodeUserList();

            List<UserVo> tempList = buildUser(nodeUserList);
            userVoList.addAll(tempList);

        }
        nodeVo.setUserVoList(userVoList);


        List<NodeVo> branchList = new ArrayList<>();

        if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue() || type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {
            //条件分支
            List<Node> branchs = node.getConditionNodes();

            for (Node branch : branchs) {
                Node children = branch.getChildNode();
                List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, completeNodeSet, continueNodeSet, processInstanceId, paramMap);

                NodeVo p = new NodeVo();
                p.setChildren(processNodeShowDtos);

                p.setPlaceholder(branch.getPlaceHolder());
                branchList.add(p);
            }
        }
        nodeVo.setBranch(branchList);


        list.add(nodeVo);

        List<NodeVo> next = formatProcessNodeShow(node.getChildNode(), completeNodeSet, continueNodeSet, processInstanceId, paramMap);
        list.addAll(next);


        return list;
    }


    /**
     * 根据实例id
     *
     * @param processInstanceId
     * @return
     */
    private static UserVo buildRootUser(String processInstanceId) {

        IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
        String userId = processInstanceRecord.getUserId();
        UserVo userVo = buildUser(userId);
        return userVo;
    }

    /**
     * 根据用户id
     *
     * @param userId
     * @return
     */
    private static UserVo buildUser(String userId) {


        UserDto user = ApiStrategyFactory.getStrategy().getUser(userId);
        if (user == null) {
            return null;
        }

        UserVo nodeUserDto = UserVo.builder().id(userId).name(user.getName())
                .avatar(user.getAvatarUrl())
                .build();
        return nodeUserDto;
    }

    private static List<UserVo> buildUser(List<NodeUser> nodeUserList) {
        List<UserVo> userVoList = new ArrayList<>();
        //用户id
        List<String> userIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(),
                NodeUserTypeEnum.USER.getKey())).map(w -> (w.getId())).collect(Collectors.toList());
        //部门id
        List<String> deptIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(),
                NodeUserTypeEnum.DEPT.getKey())).map(w -> (w.getId())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(deptIdList)) {

            IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

            List<String> data =
                    iRemoteService.queryUserIdListByDepIdList(deptIdList.stream().map(w -> String.valueOf(w)).collect(Collectors.toList())).getObj();

            if (CollUtil.isNotEmpty(data)) {
                for (String datum : data) {
                    if (!userIdList.contains((datum))) {
                        userIdList.add(datum);
                    }
                }
            }
        }
        {
            for (String aLong : userIdList) {
                userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
            }
        }
        return userVoList;
    }


    /**
     * 整理条件分支、并行分支中的人员节点集合
     *
     * @param processNodeShowDtos
     * @return
     */
    public static List<NodeVo> formatProcessNodeVoShow(List<NodeVo> processNodeShowDtos) {
        // 整理后的节点集合
        List<NodeVo> nodeVos = new ArrayList<>();
        processNodeShowDtos.forEach(node -> {
            // 整理后并行分支、条件分支中的人员节点集合
            List<NodeVo> nodeVoChildren = new ArrayList<>();
            if (null != node.getChildren() && node.getChildren().size() > 0) {
                recursionProcessNodeVo(node.getChildren(), nodeVoChildren);
            }
            if (null != node.getBranch() && node.getBranch().size() > 0) {
                recursionProcessNodeVo(node.getBranch(), nodeVoChildren);
            }
            NodeVo nodeVo = new NodeVo();
            nodeVo.setId(node.getId());
            nodeVo.setName(node.getName());
            nodeVo.setType(node.getType());
            nodeVo.setStatus(node.getStatus());
            nodeVo.setPlaceholder(node.getPlaceholder());
            nodeVo.setSelectUser(node.getSelectUser());
            nodeVo.setMultiple(node.getMultiple());
            nodeVo.setBranch(new ArrayList<NodeVo>());
            nodeVo.setChildren(nodeVoChildren.size() > 0 ? nodeVoChildren : new ArrayList<NodeVo>());
            nodeVo.setUserVoList(node.getUserVoList());
            nodeVos.add(nodeVo);
        });
        return nodeVos;
    }

    /**
     * 递归整理并行分支、条件分支中的人员节点集合
     *
     * @param nodeVos
     * @param nodeVoChildren
     * @return
     */
    private static List<NodeVo> recursionProcessNodeVo(List<NodeVo> nodeVos, List<NodeVo> nodeVoChildren) {
        nodeVos.forEach(node -> {
            // 状态为已完成、进行中、未开始且有人员节点的节点
            if (null != node.getStatus() && (node.getStatus() == 2 || node.getStatus() == 1 || node.getStatus() == 0) && node.getUserVoList().size() > 0) {
                NodeVo nodeVo = new NodeVo();
                nodeVo.setId(node.getId());
                nodeVo.setName(node.getName());
                nodeVo.setType(node.getType());
                nodeVo.setStatus(node.getStatus());
                nodeVo.setPlaceholder(node.getPlaceholder());
                nodeVo.setSelectUser(node.getSelectUser());
                nodeVo.setMultiple(node.getMultiple());
                nodeVo.setBranch(node.getBranch());
                nodeVo.setUserVoList(node.getUserVoList());
                nodeVoChildren.add(nodeVo);
            }
            if (null != node.getChildren() && node.getChildren().size() > 0) {
                recursionProcessNodeVo(node.getChildren(), nodeVoChildren);
            }
            if (null != node.getBranch() && node.getBranch().size() > 0) {
                recursionProcessNodeVo(node.getBranch(), nodeVoChildren);
            }
        });
        return nodeVoChildren;
    }
}
