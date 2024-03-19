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
 * 叠次版本
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "叠次版本信息")
public class Tbfoldareaver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 叠次版本名称
     */
    private String sname;

    /**
     * 叠次id
     */
    @LogData(alias = "叠次版本名称", mappedBy = "tbfold", mappedByColumn = "sname")
    private Long foldid;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

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
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
