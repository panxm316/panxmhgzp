package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统参数表
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Getter
@Setter
public class Twparameter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 关键字
     */
    private String skey;

    /**
     * 名称
     */
    private String sname;

    /**
     * 属性值
     */
    private String svalue;

    /**
     * 说明
     */
    private String sdiscription;

    /**
     * 启用
     */
    private Boolean buse;
}

