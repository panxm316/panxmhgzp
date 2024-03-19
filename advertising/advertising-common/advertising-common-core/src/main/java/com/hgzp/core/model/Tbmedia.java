package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 媒体信息
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "媒体信息")
public class Tbmedia implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 媒体名称
     */
    @LogData(alias = "媒体名称")
    private String sname;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
    @LogData(alias = "媒体类型")
    private String mediatypename;

    /**
     * 媒体编码
     */
    @LogData(alias = "媒体编码")
    private String scode;

    /**
     * 媒体序列号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
