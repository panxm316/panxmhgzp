package com.hgzp.common.flowable.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
public class ProcessVO  {

    private String flowId;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 图标配置
     */
    private String logo;

    /**
     * 设置项
     */
    private String settings;

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 表单设置内容
     */
    private String formItems;

    /**
     * 流程设置内容
     */
    private String process;

    /**
     * 备注
     */
    private String remark;

    private Integer sort;

    /**
     * 0 正常 1=隐藏
     */
    private Boolean hidden;

    /**
     * 0 正常 1=停用
     */
    private Boolean stop;

    /**
     * 流程管理员
     */
    private String adminId;

    /**
     * 唯一性id
     */
    private String uniqueId;

    /**
     * 管理员
     */
    private String admin;

    /**
     * 范围描述显示
     */
    private String rangeShow;

    /**
     * 用户id
     */
    private Long id;
    /**
     * 逻辑删除字段
     */
    private Boolean delFlag;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    //================================================
    /**
     * 需要发起人选择的节点id
     */
    private List<String> selectUserNodeId;

    private Map<String, Object> variableMap;

    private String groupName;
}
