package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 菜单范围关联表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Data
@NoArgsConstructor
public class Tbmenuactionscope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 菜单按钮id
     */
    private Long menuactionid;

    /**
     * 范围id
     */
    private Long scopeid;

    public Tbmenuactionscope(Long menuactionid, Long scopeid) {
        this.menuactionid = menuactionid;
        this.scopeid = scopeid;
    }

}
