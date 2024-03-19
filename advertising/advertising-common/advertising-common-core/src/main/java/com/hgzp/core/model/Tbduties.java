package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 人员职务表
 * </p>
 *
 * @author wwk
 * @since 2023-08-28
 */
@Getter
@Setter
@LogData(alias = "人员职务")
public class Tbduties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 职务名称
     */
    @LogData(alias = "职务名称")
    private String sname;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
