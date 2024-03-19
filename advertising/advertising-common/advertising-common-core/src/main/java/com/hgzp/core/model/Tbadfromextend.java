package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告来源扩展表
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
public class Tbadfromextend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 1级节点
     */
    private Long parentid1;

    /**
     * 2级节点
     */
    private Long parentid2;

    /**
     * 3级节点
     */
    private Long parentid3;

    /**
     * 4级节点
     */
    private Long parentid4;

    /**
     * 5级节点
     */
    private Long parentid5;

    /**
     * 1级节点名称
     */
    private String parentname1;

    /**
     * 2级节点名称
     */
    private String parentname2;

    /**
     * 3级节点名称
     */
    private String parentname3;

    /**
     * 4级节点名称
     */
    private String parentname4;

    /**
     * 5级节点名称
     */
    private String parentname5;

    /**
     * 广告来源信息表id
     */
    private Long adfromid;
}
