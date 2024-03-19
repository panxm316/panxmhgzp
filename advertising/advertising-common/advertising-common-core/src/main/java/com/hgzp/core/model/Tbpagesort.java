package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 版面类别
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "版面类别")
public class Tbpagesort implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 版面类别名称
     */
    @LogData(alias = "类别名称")
    private String sname;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
    private String mediatypename;

    /**
     * 叠次ID
     */
    @LogData(alias = "叠次", mappedBy = "Tbfold", mappedByColumn = "sname")
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "是否启用")
    private Boolean buse;

}
