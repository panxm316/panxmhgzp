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
 * 报表模板组设置
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@Getter
@Setter
@LogData(alias = "报表模板组设置")
public class Tbreportmodelgroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 名称
     */
    @LogData(alias = "名称")
    private String reportname;

    /**
     * 汇总类型(dept,media,all)
     */
    @LogData(alias = "汇总类型(dept,media,all)")
    private String reporttype;

    /**
     * 应用于(菜单路径)
     */
    @LogData(alias = "应用于(菜单路径)")
    private String applyto;

    /**
     * 启用
     */
    @LogData(alias = "启用")
    private Boolean buse;

    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;
}
