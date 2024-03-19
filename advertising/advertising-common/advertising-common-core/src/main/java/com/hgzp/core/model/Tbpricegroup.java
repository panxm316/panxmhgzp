package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 价格表
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Getter
@Setter
@LogData(alias = "价格表信息")
public class Tbpricegroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 价格表名称
     */
    @LogData(alias = "价格表名称")
    private String sname;

    /**
     * 媒体类型key
     */
    @LogData(alias = "媒体类型key")
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    @LogData(alias = "媒体类型名称")
    private String mediatypename;

    /**
     * 年份
     */
    @LogData(alias = "年份")
    private String syear;

    /**
     * 是否默认
     */
    @LogData(alias = "是否默认")
    private Boolean bdefault;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用标志")
    private Boolean buse;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;
}
