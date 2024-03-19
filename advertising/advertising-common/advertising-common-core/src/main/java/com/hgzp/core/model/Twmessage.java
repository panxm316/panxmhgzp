package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Getter
@Setter
public class Twmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息标题
     */
    private String stitle;

    /**
     * 消息内容
     */
    private String scontent;

    /**
     * 消息参数
     */
    private String sparam;

    /**
     * 消息类型(TodoTask:工作流待办任务)
     */
    private String stype;

    /**
     * 创建时间
     */
    private Date dcreatetime;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 接收人id
     */
    private Long receiveempid;

    /**
     * 是否已读
     */
    private Boolean breaded;

    /**
     * 是否删除
     */
    private Boolean bdeleted;

    /**
     * 流程id
     */
    private String sflowid;

    /**
     * 流程实例id
     */
    private String sprocessinstanceid;

    /**
     * 唯一id
     */
    private String suniqueid;

    /**
     * 流程创建日期
     */
    private Date dprocessinstancecreate;
}
