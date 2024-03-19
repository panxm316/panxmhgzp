package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 媒体类型
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "媒体类型")
public class Tbmediatype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 媒体类型名称
     */
    @LogData(alias = "媒体类型名称")
    private String sname;

    /**
     * 媒体类型关键字
     */
    @LogData(alias = "媒体类型关键字")
    private String skey;

    /**
     * 描述
     */
    private String sdesc;

    /**
     * 是否默认
     */
    private Boolean bdefault;

    /**
     * 排序
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
