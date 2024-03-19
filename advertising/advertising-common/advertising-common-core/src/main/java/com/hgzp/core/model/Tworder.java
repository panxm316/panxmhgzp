package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单表
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
@LogData(alias = "订单表")
public class Tworder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单编号
     */
    @LogData(alias = "订单编号")
    private String sordernum;

    /**
     * 广告项目id
     */
    @LogData(alias = "广告项目id", mappedBy = "twadproject", mappedByColumn = "sname")
    private Long adprojectid;

    /**
     * 广告项目名称
     */
    private String adprojectname;

    /**
     * 合同号
     */
    private String scontractnum;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long createempid;

    /**
     * 创建者名称
     */
    private String createempname;

    /**
     * 创建日期
     */
    private Date createtime;

    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;

    /**
     * 经营主体id
     */
    @LogData(alias = "经营主体id", mappedBy = "tbbusinessentity", mappedByColumn = "sname")
    private Long businessentityid;

    /**
     * 经营主体名称
     */
    private String businessentityname;

    /**
     * 客户id
     */
    @LogData(alias = "客户id", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;

    /**
     * 联系人
     */
    @LogData(alias = "联系人")
    private String scontacts;

    /**
     * 联系人地址
     */
    private String saddress;

    /**
     * 联系人电话
     */
    private String smobilephone;

    /**
     * 邮编
     */
    private String spostcode;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门名称
     */
    private String deptname;

    /**
     * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
     */
    private Integer iorderkind;

    /**
     * 主业务员id
     */
    @LogData(alias = "主业务员id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 主业务员名称
     */
    private String employname;

    /**
     * 代理公司id
     */
    private Long agencytid;

    /**
     * 代理公司名称
     */
    private String agencyname;

    /**
     * 代理公司业务员id
     */
    @LogData(alias = "代理公司业务员id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long agencyemployid;

    /**
     * 代理公司业务员名称
     */
    private String agencyemployname;

    /**
     * 内容生产方id
     */
    private Long agentid;

    /**
     * 内容生产方
     */
    private String agentname;

    /**
     * 内容生产方业务员id
     */
    private Long agentemployid;

    /**
     * 内空生产方业务员名称
     */
    private String agentemployname;

    /**
     * 行业id
     */
    @LogData(alias = "行业id", mappedBy = "tbadindustry", mappedByColumn = "sname")
    private Long adindustyid;

    /**
     * 行业名称
     */
    private String adindustryname;

    /**
     * 广告类型id
     */
    @LogData(alias = "广告类型id", mappedBy = "tbadtype", mappedByColumn = "sname")
    private Long adtypeid;

    /**
     * 广告类型名称
     */
    private String adtypename;

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
     * 预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    @LogData(alias = "预约审批状态")
    private Integer ipreapprovestatus;

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
     * 负责人意见
     */
    private String sopinion;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    @LogData(alias = "审核状态")
    private Integer iapprovestatus;

    /**
     * 是否特殊订单(0 否 1-是)
     */
    private Boolean bspecial;

    /**
     * 特殊原因
     */
    private String sspecialreason;

    /**
     * 关联订单id(用于预约补刊)
     */
    @LogData(alias = "关联订单id(用于预约补刊)")
    private Long relateorderid;

    /**
     * 特刊项目id
     */
    @LogData(alias = "特刊项目id")
    private Long specialprojectid;

    /**
     * 特刊项目名称
     */
    @LogData(alias = "特刊项目名称")
    private String specialprojectname;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
