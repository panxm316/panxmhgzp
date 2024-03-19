package com.hgzp.advertising.pagemodel.flow;

import lombok.Data;

import java.util.Map;

@Data
public class NodeFormatParamVo {

    private String flowId;
    private String processInstanceId;
    private String taskId;

    private Map<String,Object> paramMap;

    private Boolean isAllProcess;

}
