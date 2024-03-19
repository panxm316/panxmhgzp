package com.hgzp.common.flowable.dto;

import lombok.Data;

/**
 * 用户任务查询参数
 */
@Data
public class TaskQueryParamDto extends PageDto{
    /**    任务执行人     */
    private String assign;

    /**    任务实例id     */
    private String processInstanceId;

}
