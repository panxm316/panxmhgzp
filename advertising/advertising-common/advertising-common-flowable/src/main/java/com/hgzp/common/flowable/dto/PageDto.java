package com.hgzp.common.flowable.dto;

import lombok.Data;

@Data
public class PageDto {

    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页的数量
     */
    private Integer pageSize;
    /** 开始时间 */
    private String startTime;
    /** 截止时间 */
    private String endTime;
    /** 任务结束时间标志 */
    private boolean bendFlag;

}
