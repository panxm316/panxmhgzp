package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 叠次信息
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "叠次信息")
public class Tbfold implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 叠次名称
     */
    @LogData(alias = "叠次名称")
    private String sname;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体名称", mappedBy = "tbmedia", mappedByColumn = "sname")
    private Long mediaid;

    /**
     * 版数
     */
    @LogData(alias = "版数")
    private Integer pagecount;

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
