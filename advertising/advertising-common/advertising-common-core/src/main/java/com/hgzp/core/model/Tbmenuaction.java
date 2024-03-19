package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单行为表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbmenuaction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜单id
     */
    private Long menuid;

    /**
     * 名称
     */
    private String sname;

    /**
     * 操作代码
     */
    private String skeycode;

    /**
     * 按钮url
     */
    private String sfunctionurl;

    /**
     * 设置中是否可以修改1可修改0不可修改
     */
    private Boolean bmodify;

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
