package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 角色范围表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tbroleactionscope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 按钮行为id
     */
    private Long roleactionid;

    /**
     * 作用域id
     */
    private Long sscopeid;

    /**
     * 相关业务id(相关需要权限控制的id)
     */
    private Long sbusinessid;

    /**
     * 是否所有权限
     */
    private Boolean ball;
}
