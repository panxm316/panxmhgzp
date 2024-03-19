package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 内容类别(暂时不用)
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbcontentcate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类别名称
     */
    private String sname;

    /**
     * 分组id
     */
    private Long scontentcategroupid;

    /**
     * 默认
     */
    private Long defaultid;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;
}
