package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 特刊项目
 * </p>
 *
 * @author muyn
 * @since 2024-03-13
 */
@Getter
@Setter
@LogData(alias = "特刊项目")
public class Twspecialproject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "id")
    private Long id;

    /**
     * 创建人id
     */
    @LogData(alias = "创建人id")
    private Long createempid;

    /**
     * 创建人名称
     */
    @LogData(alias = "创建人名称")
    private String screatename;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
    private Date dcreatedate;

    /**
     * 名称
     */
    @LogData(alias = "名称")
    private String sname;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "结束日期")
    private Date denddate;

    /**
     * 项目说明
     */
    @LogData(alias = "项目说明")
    private String sprojectcontent;

    /**
     * 结束标记
     */
    @LogData(alias = "结束标记")
    private Boolean bprojectcomplete;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;
}
