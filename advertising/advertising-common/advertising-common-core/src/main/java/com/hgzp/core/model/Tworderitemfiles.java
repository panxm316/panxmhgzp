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
 * 订单明细文件表（用于分类广告文件）
 * </p>
 *
 * @author muyn
 * @since 2024-02-27
 */
@Getter
@Setter
@LogData(alias = "订单明细文件表")
public class Tworderitemfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 广告表id
     */
    @LogData(alias = "广告表id")
    private Long orderid;

    /**
     * 广告明细表id
     */
    @LogData(alias = "广告明细表id")
    private Long orderitemid;

    /**
     * 文件格式
     */
    @LogData(alias = "文件格式")
    private String sfileformat;

    /**
     * 统一文件ID
     */
    @LogData(alias = "统一文件ID")
    private String sfileid;

    /**
     * 文件大小
     */
    @LogData(alias = "文件大小")
    private String sfilesize;

    /**
     * 源文件名
     */
    @LogData(alias = "源文件名")
    private String soriginalfile;

    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    @LogData(alias = "文件格式类型(Photo、Video、Audio、Office、Application)")
    private String sfiletype;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 创建者ID
     */
    @LogData(alias = "创建者ID")
    private Long employid;

    /**
     * 文件描述
     */
    @LogData(alias = "文件描述")
    private String sdescription;
}
