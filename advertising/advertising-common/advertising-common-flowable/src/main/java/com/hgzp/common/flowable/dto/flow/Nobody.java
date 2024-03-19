package com.hgzp.common.flowable.dto.flow;

import lombok.Data;

import java.util.List;

/**
 * 审批人为空时
 */
@Data
public class Nobody {
    /**
     * com.hgzp.common.constants.ProcessInstanceConstant
     *      USER_TASK_NOBODY_ 开头的
     */
    private String handler;

    private List<NodeUser> assignedUser;

}
