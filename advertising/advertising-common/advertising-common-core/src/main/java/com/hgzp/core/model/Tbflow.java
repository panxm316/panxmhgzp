package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 流程设置
 * </p>
 *
 * @author muyn
 * @since 2024-02-16
 */
@Getter
@Setter
@LogData(alias = "流程设置")
public class Tbflow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 基础流程名称
     */
    @LogData(alias = "基础流程名称")
    private String sname;

    /**
     * 流程关键字
     */
    @LogData(alias = "流程关键字")
    private String skey;

    /**
     * 描述
     */
    @LogData(alias = "描述")
    private String sdesc;

    /**
     * 排序
     */
    @LogData(alias = "排序")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "是否启用")
    private Boolean bactive;

    /**
     * 是否启用
     */
    @LogData(alias = "是否有效")
    private Boolean buse;
}
