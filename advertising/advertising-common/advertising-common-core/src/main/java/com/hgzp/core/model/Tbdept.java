package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Data
@LogData(alias = "部门表")
@NoArgsConstructor
public class Tbdept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sdeptname")
    private Long id;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String sdeptname;

    /**
     * 父部门id
     */
    @LogData(alias = "父部门id", mappedBy = "Tbdept", mappedByColumn = "sdeptname")
    private Long parentid;

    /**
     * 部门深度
     */
    private Integer idepth;

    /**
     * 域组织映射
     */
    private String sndomainou;

    /**
     * 是否有默认角色标志
     */
    private Boolean bflagrole;

    /**
     * 是否导航节点
     */
    private Boolean bflagroot;

    /**
     * 部门别名
     */
    private String sdeptalias;

    /**
     * 内外部部门标志
     */
    private Boolean binner;

    /**
     * cas部门id
     */
    private String sguidcas;

    /**
     * cas部门是否推送
     */
    private Boolean bcaspush;

    /**
     * 数据版本号
     */
    private Integer idataversion;

    /**
     * 序号
     */
    @LogData
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData
    private Boolean buse;

    public Tbdept(Long id) {
        this.id = id;
    }
}
