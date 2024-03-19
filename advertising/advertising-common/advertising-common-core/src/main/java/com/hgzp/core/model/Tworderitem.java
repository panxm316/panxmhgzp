package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author wwk
 * @since 2023-11-17
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
@Getter
@Setter
@LogData(alias = "订单明细表")
public class Tworderitem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @LogData(showColumn = "sordernum")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单id
     */
    @LogData(alias = "订单id", mappedBy = "tworder", mappedByColumn = "sordernum")
    private Long orderid;

    /**
     * 订单号
     */
    @LogData(alias = "订单号")
    private String sordernum;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id", mappedBy = "tbemploy", mappedByColumn = "susername")
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
    private Date createtime;

    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    @LogData(alias = "录入方式")
    private Integer ibooktype;

    /**
     * 媒体类型key
     */
    @LogData(alias = "媒体类型id", mappedBy = "tbmediatype", mappedByColumn = "sname")
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    @LogData(alias = "mediatypename")
    private String mediatypename;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体id", mappedBy = "tbmedia", mappedByColumn = "sname")
    private Long mediaid;

    /**
     * 媒体名称
     */
    @LogData(alias = "媒体名称")
    private String medianame;

    /**
     * 预见报开始日期
     */
    @LogData(alias = "预见报开始日期")
    private Date prestartdate;

    /**
     * 预见报结束日期
     */
    @LogData(alias = "预见报结束日期")
    private Date preenddate;

    /**
     * 叠次id
     */
    @LogData(alias = "叠次id", mappedBy = "tbfold", mappedByColumn = "sname")
    private Long foldid;

    /**
     * 叠次名称
     */
    @LogData(alias = "叠次名称")
    private String foldname;

    /**
     * 叠次版本id
     */
    @LogData(alias = "叠次版本id", mappedBy = "tbfoldareaver", mappedByColumn = "sname")
    private Long foldareaverid;

    /**
     * 叠次版本名称
     */
    @LogData(alias = "叠次版本名称")
    private String foldareavername;

    /**
     * 广告形式id
     */
    @LogData(alias = "广告形式id", mappedBy = "tbaddisplayform", mappedByColumn = "sname")
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    @LogData(alias = "广告形式名称")
    private String addisplayformname;

    /**
     * 版面类别id
     */
    @LogData(alias = "版面类别id", mappedBy = "tbpagesort", mappedByColumn = "sname")
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    @LogData(alias = "版面类别名称")
    private String pagesortname;

    /**
     * 色彩id
     */
    @LogData(alias = "色彩id", mappedBy = "tbadcolor", mappedByColumn = "sname")
    private Long adcolorid;

    /**
     * 色彩名称
     */
    @LogData(alias = "色彩名称")
    private String adcolorname;

    /**
     * 规格id
     */
    @LogData(alias = "规格id", mappedBy = "tbadspec", mappedByColumn = "sname")
    private Long adspecid;

    /**
     * 规格名称
     */
    @LogData(alias = "规格名称")
    private String adspecname;

    /**
     * 宽
     */
    private BigDecimal nwidth;

    /**
     * 高
     */
    private BigDecimal nheight;

    /**
     * 星期id
     */
    private Long weeksettingid;

    /**
     * 星期名称
     */
    @LogData(alias = "星期名称")
    private String weeksettingname;

    /**
     * 版面id
     */
    @LogData(alias = "版面id")
    private Long foldpageplanid;

    /**
     * 广告标题
     */
    @LogData(alias = "广告标题")
    private String sadtitle;

    /**
     * 广告内容
     */
    @LogData(alias = "广告内容")
    private String sadcontent;

    /**
     * 版面名称
     */
    @LogData(alias = "版面名称")
    private String foldpageplanname;

    /**
     * 刊例价
     */
    private BigDecimal baseprice;

    /**
     * 价格明细表id
     */
    private Long priceitemid;


    /**
     * 标准价格
     */
    private BigDecimal normalprice;

    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;

    /**
     * 已收金额
     */
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;
    /**
     * 折扣率
     */
    private BigDecimal ndiscountrate;

    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;

    /**
     * 业绩时间(用于统计)
     */
    @LogData(alias = "业绩时间")
    private Date dachievementdate;

    /**
     * 成本金额
     */
    private BigDecimal namountcost;

    /**
     * 加刊审批状态
     */
    @LogData(alias = "加刊审批状态")
    private Integer iaddapprovestatus;

    /**
     * 改刊审批状态
     */
    @LogData(alias = "改刊审批状态")
    private Integer ichangeapprovestatus;

    /**
     * 停刊审批状态
     */
    @LogData(alias = "停刊审批状态")
    private Integer istopapprovestatus;

    /**
     * 折扣审批状态
     */
    @LogData(alias = "折扣审批状态")
    private Integer idiscountapprovestatus;

    /**
     * 订单审批状态
     */
    @LogData(alias = "订单审批状态")
    private Integer iapprovestatus;

    /**
     * 发布状态
     */
    private Integer ipublishstatus;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;

    /**
     * 是否删除
     */
    @LogData(alias = "是否删除")
    private Boolean bdelete;

    /**
     * 是否填报欠款原因
     */
    private Boolean breportreason;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 刊期编号(自增列)
     */
    private Long iitemcode;

    /**
     * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
     */
    @LogData(alias = "刊发检测状态(0-正常 1-未刊发 2-刊发错误")
    private Integer ipublishcheckstatus;

    /**
     * 刊发检测报告内容
     */
    private String spublishcheckcontent;
    /**
     * 关联订单id(用于预改刊刊)
     */
    private Long relateorderid;

    /**
     * 分类广告预排文件
     */
    @LogData(alias = "分类广告预排文件")
    private String sfilename;

    /**
     * 并发标记
     */
    @Version
    private Long version;
}
