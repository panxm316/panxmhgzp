package com.hgzp.advertising.pagemodel.customer.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 ProcessInstanceVo
 创建人：songly
 类描述：审批历史流程类
 创建日期：2023/11/11 9:44
 */
@Data
public class ProcessInstanceVo  {
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
}