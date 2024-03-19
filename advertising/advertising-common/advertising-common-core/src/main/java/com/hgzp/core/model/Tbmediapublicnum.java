package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 媒体刊期
 * </p>
 *
 * @author wwk
 * @since 2023-09-07
 */
@Getter
@Setter
public class Tbmediapublicnum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 媒体id
     */
    @LogData(showColumn = "medianame")
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 发布日期
     */
    @LogData(alias = "发布日期")
    private Date dpublishtime;

    /**
     * 发布期号
     */
    @LogData(alias = "发布期号")
    private String spublishno;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;
}
