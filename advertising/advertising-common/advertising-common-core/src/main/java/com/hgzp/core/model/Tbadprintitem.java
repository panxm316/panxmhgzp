package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 开票项目
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "开票项目")
public class Tbadprintitem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "开票项目名称")
    private String sname;

    /**
     * 税收编码
     */
    @LogData(alias = "税收编码")
    private String staxcode;

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
    private Boolean bdefault;
}
