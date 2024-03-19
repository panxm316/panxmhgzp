package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 付款方式
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "付款方式")
public class Tbpaymethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 方式名称
     */
    @LogData(alias = "付款方式名称")
    private String sname;

    /**
     * 方式描述
     */
    @LogData(alias = "描述")
    private String sdesc;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;

    /**
     * 是否默认
     */
    @LogData(alias = "默认")
    private Boolean bdefault;
}
