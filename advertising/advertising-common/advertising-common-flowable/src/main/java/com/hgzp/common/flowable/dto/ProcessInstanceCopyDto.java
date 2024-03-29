package com.hgzp.common.flowable.dto;

import lombok.Data;

import java.util.Date;
@Data
public class ProcessInstanceCopyDto {



    /**
     * 当前节点时间
     */
    private Date nodeTime;

    /**
     * 发起人
     */
    private String startUserId;

    /**
     * 流程id
     */
    private String flowId;

    /**
     * 实例id
     */
    private String processInstanceId;

    /**
     * 节点id
     */
    private String nodeId;


    /**
     * 节点 名称
     */
    private String nodeName;

    /**
     * 表单数据
     */
    private String formData;

    /**
     * 抄送人id
     */
    private String userId;
}
