package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限行为表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbroleaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色与菜单关联id
     */
    private Long rolemenuid;

    /**
     * 操作代码
     */
    private String skeycode;

    /**
     * 名称
     */
    private String sname;

    /**
     * 是否所有权限
     */
    private Boolean ball;

    /**
     * 按钮url
     */
    private String sfunctionurl;

    /**
     * 按钮组，用于同组赋权
     */
    private String sgroupkey;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;
}
