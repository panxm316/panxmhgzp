package com.hgzp.common.flowable.dto.flow;

import lombok.Data;

/**
 * 拒绝处理
 */
@Data
public class Refuse {
    /**
     * 拒绝方式
     * 疑似对应 com.hgzp.common.flowable.constants.ProcessInstanceConstant 的
     *  USER_TASK_REFUSE_TYPE_TO_END 和 USER_TASK_REFUSE_TYPE_TO_NODE 常量，原程序里图省事会写“TO_END”
     */
    private String handler;
    /**
     * 拒绝结束流程（TO_END）时为空串，跳转节点（TO_NODE）时应该对应节点id（没追过，不确定）
     */
    private String nodeId;

}
