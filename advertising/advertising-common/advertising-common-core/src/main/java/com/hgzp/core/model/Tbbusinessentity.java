package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 经营主体
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Getter
@Setter
@LogData(alias = "经营主体")
public class Tbbusinessentity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "经营主体名称")
    private String sname;

    /**
     * 收款方银行账号
     */
    @LogData(alias = "收款方银行账号")
    private String sbankaccount;

    /**
     * 收款方电话
     */
    @LogData(alias = "收款方电话")
    private String sphone;

    /**
     * 收款方地址
     */
    @LogData(alias = "收款方地址")
    private String saddress;

    /**
     * 税目
     */
    @LogData(alias = "税目")
    private String taxitems;

    /**
     * 税率
     */
    @LogData(alias = "税率")
    private String taxrate;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;

    /**
     * 是否默认
     */
    @LogData(alias = "默认")
    private Boolean bdefault;
}
