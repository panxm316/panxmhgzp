package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbmenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父id
     */
    private Long parentid;

    /**
     * 名称
     */
    private String sname;

    /**
     * 深度
     */
    private Integer idepth;

    /**
     * url
     */
    private String sfunctionurl;

    /**
     * 前端组件名称
     */
    private String scomponent;

    /**
     * 前端路由地址
     */
    private String srouterpath;

    /**
     * 菜单所属模块
     */
    private String sclass;

    /**
     * 菜单分组名称
     */
    private String sgroup;

    /**
     * 图片
     */
    private String simageurl;

    /**
     * 菜单权限使用别名
     */
    private String sothername;

    /**
     * 是否需要在菜单列表中显示1显示0不显示(只赋权使用)
     */
    private Boolean bmenushow;

    /**
     * 是否在菜单权限使用
     */
    private Boolean broleflag;

    /**
     * 备注(用于记录菜单使用范围)
     */
    private String sremark;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;
}
