package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tworder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 VO 前端页面视图对象
 * </p>
 *
 * @author wangxk
 * @since 2024-01-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class TworderVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 订单编号
     */
    private String sordernum;
    /**
     * 广告项目id
     */
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
    private Long businessentityid;
    /**
     * 经营主体名称
     */
    private String businessentityname;
    /**
     * 客户id
     */
    private Long customerid;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 联系人
     */
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
    private Long adindustyid;
    /**
     * 行业名称
     */
    private String adindustryname;
    /**
     * 广告类型id
     */
    private Long adtypeid;
    /**
     * 广告类型名称
     */
    private String adtypename;
    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 广告内容
     */
    private String sadcontent;
    /**
     * 是否有效
     */
    private Boolean buse;
    /**
     * 是否删除
     */
    private Boolean bdelete;
    /**
     * 预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    private Integer ipreapprovestatus;
    /**
     * 加刊审批状态
     */
    private Integer iaddapprovestatus;
    /**
     * 改刊审批状态
     */
    private Integer ichangeapprovestatus;
    /**
     * 停刊审批状态
     */
    private Integer istopapprovestatus;
    /**
     * 折扣审批状态
     */
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
    private Long relateorderid;
    /**
     * 并发标记
     */
    private Long version;

    // --------- 新增属性 ------------
    private List<TworderitemVO> orderitem;

    public static TworderVO parseToVO(Tworder entity) {
        TworderVO TworderVO = new TworderVO();
        BeanUtils.copyProperties(entity, TworderVO);
        return TworderVO;
    }

    public static Tworder parseToEntity(TworderVO vo) {
        Tworder entity = new Tworder();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
