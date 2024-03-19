package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 付款时间设置
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbpaysort implements Serializable {

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
     * 备注
     */
    private String sremark;

    /**
     * 描述
     */
    private String sdesc;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 付款时间关键字 刊前付款 bepub 刊后付款 afpub
     */
    private String skey;

    /**
     * 是否默认
     */
    private Boolean bdefault;
}
