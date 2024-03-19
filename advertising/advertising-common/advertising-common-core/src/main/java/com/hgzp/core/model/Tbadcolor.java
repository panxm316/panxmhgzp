package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告色彩
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "广告色彩")
public class Tbadcolor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "色彩名称")
    private String sname;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型i
     */
    private String mediatypename;

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
}
