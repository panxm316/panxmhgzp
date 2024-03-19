package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户服务表
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Getter
@Setter
@LogData(alias = "客户服务信息")
public class Twcustomerservice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "stitle")
    private Long id;

    /**
     * 标题
     */
    @LogData(alias = "标题")
    private String stitle;

    /**
     * 人员id
     */
    @LogData(alias = "人员id")
    private Long employid;

    /**
     * 人员名称
     */
    @LogData(alias = "人员名称")
    private String employname;

    /**
     * 客户id
     */
    @LogData(alias = "客户id")
    private Long customerid;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 0-客户投诉 1-客户建议 2-客户调查
     */
    @LogData(alias = "服务类型")
    private Integer iservicetype;

    /**
     * 联系人
     */
    @LogData(alias = "联系人")
    private String scontacts;

    /**
     * 联系方式
     */
    @LogData(alias = "联系方式")
    private String sphone;

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
     * 操作员id
     */
    private Long operatorid;

    /**
     * 操作员
     */
    @LogData(alias = "操作员")
    private String operatorname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 内容
     */
    @LogData(alias = "内容")
    private String scontent;

    /**
     * 结果
     */
    @LogData(alias = "结果")
    private String sresult;

    /**
     * 状态(可选可填写：已记录、已解决、已结束等)
     */
    @LogData(alias = "状态")
    private String sstatus;

    /**
     * 最后修改者id
     */
    private Long lastoperatorid;

    /**
     * 最后修改者
     */
    @LogData(alias = "最后修改者")
    private String lastoperator;

    /**
     * 最后修改日期
     */
    @LogData(alias = "最后修改日期")
    private Date dlasttime;

    /**
     * 是否删除标记
     */
    @LogData(alias = "是否删除标记")
    private Boolean bdelete;
}
