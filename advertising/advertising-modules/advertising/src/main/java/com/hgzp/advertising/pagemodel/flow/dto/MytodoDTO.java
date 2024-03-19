package com.hgzp.advertising.pagemodel.flow.dto;

import com.hgzp.common.flowable.dto.PageDto;
import lombok.Data;

/**
 MytodoDTO
 创建人：songly
 类描述：我的待办请求参数
 创建日期：2024/3/6 10:39
 */
@Data
public class MytodoDTO  {
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
    private String loginUserId;
}