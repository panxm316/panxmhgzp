package com.hgzp.common.flowable.dto;

import lombok.Data;

import java.util.List;

@Data
public class VariableQueryParamDto {

    private String taskId;

    private List<String> keyList;

    private String executionId;

}
