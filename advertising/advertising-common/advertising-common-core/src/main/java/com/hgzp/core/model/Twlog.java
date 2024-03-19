package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author wwk
 * @since 2023-08-19
 */
@Getter
@Setter
public class Twlog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 日志类型（新增、修改或删除）
     */
    private String slogtype;

    /**
     * 操作员id
     */
    private Long employid;

    /**
     * 操作员姓名
     */
    private String employname;

    /**
     * 操作员IP
     */
    private String soperatorip;

    /**
     * 操作员Mac
     */
    private String soperatormac;

    /**
     * 操作时间
     */
    private Date doperatordate;

    /**
     * 备注
     */
    private String sremark;

}
