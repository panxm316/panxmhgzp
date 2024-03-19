package com.hgzp.common.flowable.dto.flow;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Node {

    private String id;
    /**
     * 父节点id
     */
    private String parentId;
    /**
     * （画线时）前驱结点id
     */
    private String headId;
    /**
     * （画线时）后继结点id
     */
    private String tailId;
    /**
     * 流程图中节点展示的文本
     */
    private String placeHolder;
    /**
     * 拒绝处理
     */
    private Refuse refuse;
    /**
     * 当前节点类型，对应 com.hgzp.common.flowable.constants.NodeTypeEnum
     */
    private Integer type;

    private String nodeName;

    private Boolean error;
    /**
     * 子节点 - 嵌套Node
     */
    private Node childNode;
    /**
     * 审批类型，对应 com.hgzp.common.flowable.constants.ProcessInstanceConstant.AssignedTypeClass
     */
    private Integer assignedType;
    /**
     * 是否多选
     */
    private Boolean multiple;
    /**
     * 多人审批时采用的审批方式
     *          com.hgzp.common.flowable.constants.ProcessInstanceConstant
     *              MULTIPLE_MODE_ 开头那些
     */
    private Integer multipleMode;
    /**
     * 第几级主管审批
     */
    private Integer deptLeaderLevel;
    private String formUserId;
    /**
     * 审批人是表单部门时  用户类型
     *      com.hgzp.common.constants.ProcessInstanceConstant.AssignedTypeFormDeptUserTypeClass
     */
    private String deptUserType;
    private String formUserName;
    /**
     * 节点涉及的用户列表（暂定）
     */
    private List<NodeUser> nodeUserList;
    /**
     * 网关（条件分支/并行分支节点）独有 - 分支列表，一个 Node 是一个分支
     */
    private List<Node> conditionNodes;
    /**
     * 本节点表单项的权限列表
     */
    private Map<String,String> formPerms;
    /**
     * 审批人为空（指定处理策略、处理人）
     */
    private Nobody nobody;
    /**
     * 条件组关系 - 真且假或
     */
    private Boolean groupMode;
    /**
     * 条件列表 - 网关独有
     */
    private List<GroupCondition> conditionList;

}
