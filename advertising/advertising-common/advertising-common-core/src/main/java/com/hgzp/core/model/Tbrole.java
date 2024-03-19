package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbrole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String sname;

    /**
     * 是否全部权限
     */
    private Boolean ball;

    /**
     * 说明
     */
    private String sdesc;

    /**
     * 角色类型：0-普通，1-子报管理员，2-超级管理员
     */
    private Integer iroletype;

    /**
     * 是否单独赋权
     */
    private Boolean bselfrole;


    /**
     * 是否有效
     */
    private Boolean buse;

    /**
     * 序号
     */
    private Integer isort;
}
