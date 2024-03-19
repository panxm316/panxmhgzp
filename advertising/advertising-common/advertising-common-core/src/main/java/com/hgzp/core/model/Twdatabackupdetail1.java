package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 数据轧账明细表示例（广告明细）
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
@Getter
@Setter
@LogData(alias = "数据轧账明细表示例（广告明细）")
public class Twdatabackupdetail1 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增列
     */
    @TableId(value = "id", type = IdType.AUTO)
    @LogData(alias = "自增列")
    private Long id;

    /**
     * 数据备份组表id
     */
    @LogData(alias = "数据备份组表id")
    private Long databackupgroupid;

    /**
     * 订单id
     */
    @LogData(alias = "订单id")
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
     * 广告项目id
     */
    @LogData(alias = "广告项目id")
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    @LogData(alias = "广告项目名称")
    private String adprojectname;

    /**
     * 订单创建者id
     */
    @LogData(alias = "订单创建者id")
    private Long ordercreateempid;

    /**
     * 订单创建者名称
     */
    @LogData(alias = "订单创建者名称")
    private String ordercreateempname;

    /**
     * 订单创建日期
     */
    @LogData(alias = "订单创建日期")
    private Date ordercreatetime;

    /**
     * 订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    @LogData(alias = "订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊")
    private Integer orderibooktype;

    /**
     * 经营主体id
     */
    @LogData(alias = "经营主体id")
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    @LogData(alias = "经营主体名称")
    private String businessentityname;

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
     * 联系人
     */
    @LogData(alias = "联系人")
    private String scontacts;

    /**
     * 地址
     */
    @LogData(alias = "地址")
    private String saddress;

    /**
     * 联系人电话
     */
    @LogData(alias = "联系人电话")
    private String smobilephone;

    /**
     * 邮编
     */
    @LogData(alias = "邮编")
    private String spostcode;

    /**
     * 部门id
     */
    @LogData(alias = "部门id")
    private Long deptid;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
     */
    @LogData(alias = "订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告")
    private Integer iorderkind;

    /**
     * 主业务员id
     */
    @LogData(alias = "主业务员id")
    private Long employid;

    /**
     * 主业务员名称
     */
    @LogData(alias = "主业务员名称")
    private String employname;

    /**
     * 代理公司id
     */
    @LogData(alias = "代理公司id")
    private Long agencytid;

    /**
     * 代理公司名称
     */
    @LogData(alias = "代理公司名称")
    private String agencyname;

    /**
     * 代理公司业务员id
     */
    @LogData(alias = "代理公司业务员id")
    private Long agencyemployid;

    /**
     * 代理公司业务员名称
     */
    @LogData(alias = "代理公司业务员名称")
    private String agencyemployname;

    /**
     * 内容生产方id
     */
    @LogData(alias = "内容生产方id")
    private Long agentid;

    /**
     * 内容生产方
     */
    @LogData(alias = "内容生产方")
    private String agentname;

    /**
     * 内容生产方业务员id
     */
    @LogData(alias = "内容生产方业务员id")
    private Long agentemployid;

    /**
     * 内空生产方业务员名称
     */
    @LogData(alias = "内空生产方业务员名称")
    private String agentemployname;

    /**
     * 行业id
     */
    @LogData(alias = "行业id")
    private Long adindustyid;

    /**
     * 行业名称
     */
    @LogData(alias = "行业名称")
    private String adindustryname;

    /**
     * 广告类型id
     */
    @LogData(alias = "广告类型id")
    private Long adtypeid;

    /**
     * 广告类型名称
     */
    @LogData(alias = "广告类型名称")
    private String adtypename;

    /**
     * 广告标题
     */
    @LogData(alias = "广告标题")
    private String sorderadtitle;

    /**
     * 广告内容
     */
    @LogData(alias = "广告内容")
    private String sorderadcontent;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean borderuse;

    /**
     * 是否删除
     */
    @LogData(alias = "是否删除")
    private Boolean borderdelete;

    /**
     * 负责人意见
     */
    @LogData(alias = "负责人意见")
    private String sopinion;

    /**
     * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    @LogData(alias = "审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）")
    private Integer iorderapprovestatus;

    /**
     * 是否特殊订单(0 否 1-是)
     */
    @LogData(alias = "是否特殊订单(0 否 1-是)")
    private Boolean bspecial;

    /**
     * 特殊原因
     */
    @LogData(alias = "特殊原因")
    private String sspecialreason;

    /**
     * 广告明细主键
     */
    @LogData(alias = "广告明细主键")
    private Long orderitemid;

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
    private Date createtime;

    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    @LogData(alias = "录入方式 0-正常 1-广告预约 2-快速预约 3-补刊")
    private Integer ibooktype;

    /**
     * 媒体类型key
     */
    @LogData(alias = "媒体类型key")
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    @LogData(alias = "媒体类型名称")
    private String mediatypename;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体id")
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
    @LogData(alias = "叠次id")
    private Long foldid;

    /**
     * 叠次名称
     */
    @LogData(alias = "叠次名称")
    private String foldname;

    /**
     * 叠次版本id
     */
    @LogData(alias = "叠次版本id")
    private Long foldareaverid;

    /**
     * 叠次版本名称
     */
    @LogData(alias = "叠次版本名称")
    private String foldareavername;

    /**
     * 广告形式id
     */
    @LogData(alias = "广告形式id")
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    @LogData(alias = "广告形式名称")
    private String addisplayformname;

    /**
     * 版面类别id
     */
    @LogData(alias = "版面类别id")
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    @LogData(alias = "版面类别名称")
    private String pagesortname;

    /**
     * 色彩id
     */
    @LogData(alias = "色彩id")
    private Long adcolorid;

    /**
     * 色彩名称
     */
    @LogData(alias = "色彩名称")
    private String adcolorname;

    /**
     * 规格id
     */
    @LogData(alias = "规格id")
    private Long adspecid;

    /**
     * 规格名称
     */
    @LogData(alias = "规格名称")
    private String adspecname;

    /**
     * 宽
     */
    @LogData(alias = "宽")
    private BigDecimal nwidth;

    /**
     * 高
     */
    @LogData(alias = "高")
    private BigDecimal nheight;

    /**
     * 星期id
     */
    @LogData(alias = "星期id")
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
    @LogData(alias = "刊例价")
    private BigDecimal baseprice;

    /**
     * 价格明细表id
     */
    @LogData(alias = "价格明细表id")
    private Long priceitemid;

    /**
     * 标准价格
     */
    @LogData(alias = "标准价格")
    private BigDecimal normalprice;

    /**
     * 应收金额
     */
    @LogData(alias = "应收金额")
    private BigDecimal amountreceivable;

    /**
     * 已收金额
     */
    @LogData(alias = "已收金额")
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    @LogData(alias = "欠款金额")
    private BigDecimal amountarrearage;

    /**
     * 折扣率
     */
    @LogData(alias = "折扣率")
    private BigDecimal ndiscountrate;

    /**
     * 业绩金额
     */
    @LogData(alias = "业绩金额")
    private BigDecimal amountachievement;

    /**
     * 业绩时间
     */
    @LogData(alias = "业绩时间")
    private Date dachievementdate;

    /**
     * 成本金额
     */
    @LogData(alias = "成本金额")
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
     * 发布状态0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架
     */
    @LogData(alias = "发布状态0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架")
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
     * 是否推送填报欠款原因
     */
    @LogData(alias = "是否推送填报欠款原因")
    private Boolean breportreason;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 刊期编码(自增列)
     */
    @LogData(alias = "刊期编码(自增列)")
    private Long iitemcode;

    /**
     * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
     */
    @LogData(alias = "刊发检测状态(0-正常 1-未刊发 2-刊发错误")
    private Integer ipublishcheckstatus;

    /**
     * 刊发检测报告
     */
    @LogData(alias = "刊发检测报告")
    private String spublishcheckcontent;
}
