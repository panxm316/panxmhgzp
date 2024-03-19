package com.hgzp.advertising.pagemodel.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessInstanceDTO {
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 审批结果
     */
    private Boolean result;

    /**
     * 表单数据
     */
    private String formData;
}
