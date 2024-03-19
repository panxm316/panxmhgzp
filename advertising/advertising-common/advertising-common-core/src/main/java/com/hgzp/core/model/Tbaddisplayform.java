package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告形式
 * </p>
 *
 * @author wwk
 * @since 2023-11-06
 */
@Getter
@Setter
@LogData(alias = "广告形式信息")
public class Tbaddisplayform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 广告形式名称
     */
    @LogData(alias = "广告形式名称")
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
    private String sremark;
}
