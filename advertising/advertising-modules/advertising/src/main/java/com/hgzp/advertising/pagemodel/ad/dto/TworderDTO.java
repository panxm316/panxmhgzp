package com.hgzp.advertising.pagemodel.ad.dto;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 订单表 DTO 数据传输对象
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
public class TworderDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, detail.class})
    private Long id;
    /**
     * 订单编号
     */
    @NotBlank(message = "订单编号不能为空", groups = {add.class, edit.class, detail.class})
    private String sordernum;
    /**
     * 广告项目id
     */
    @NotNull(message = "广告项目id不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private Long adprojectid;
    /**
     * 广告项目名称
     */
    @NotBlank(message = "广告项目名称不能为空", groups = {add.class, edit.class, detail.class})
    private String adprojectname;
    /**
     * 合同号
     */
    @NotBlank(message = "合同号不能为空", groups = {add.class, edit.class, detail.class})
    private String scontractnum;
    /**
     * 创建者id
     */
    @NotNull(message = "创建者id不能为空", groups = {add.class, edit.class, detail.class})
    private Long createempid;
    /**
     * 创建者名称
     */
    @NotBlank(message = "创建者名称不能为空", groups = {add.class, edit.class, detail.class})
    private String createempname;
    /**
     * 创建日期
     */
    @NotNull(message = "创建日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date createtime;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    @NotNull(message = "录入方式 0-正常 1-广告预约 2-快速预约 3-补刊不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ibooktype;
    /**
     * 经营主体id
     */
    @NotNull(message = "经营主体id不能为空", groups = {add.class, edit.class, detail.class})
    private Long businessentityid;
    /**
     * 经营主体名称
     */
    @NotBlank(message = "经营主体名称不能为空", groups = {add.class, edit.class, detail.class})
    private String businessentityname;
    /**
     * 客户id
     */
    @NotNull(message = "客户id不能为空", groups = {add.class, edit.class, detail.class})
    private Long customerid;
    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {add.class, edit.class, detail.class})
    private String customername;
    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空", groups = {add.class, edit.class, detail.class})
    private String scontacts;
    /**
     * 联系人地址
     */
    @NotBlank(message = "联系人地址不能为空", groups = {add.class, edit.class, detail.class})
    private String saddress;
    /**
     * 联系人电话
     */
    @NotBlank(message = "联系人电话不能为空", groups = {add.class, edit.class, detail.class})
    private String smobilephone;
    /**
     * 邮编
     */
    @NotBlank(message = "邮编不能为空", groups = {add.class, edit.class, detail.class})
    private String spostcode;
    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {add.class, edit.class, detail.class})
    private Long deptid;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {add.class, edit.class, detail.class})
    private String deptname;
    /**
     * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
     */
    @NotNull(message = "订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告不能为空", groups = {add.class, edit.class, detail.class})
    private Integer iorderkind;
    /**
     * 主业务员id
     */
    @NotNull(message = "主业务员id不能为空", groups = {add.class, edit.class, detail.class})
    private Long employid;
    /**
     * 主业务员名称
     */
    @NotBlank(message = "主业务员名称不能为空", groups = {add.class, edit.class, detail.class})
    private String employname;
    /**
     * 代理公司id
     */
    @NotNull(message = "代理公司id不能为空", groups = {add.class, edit.class, detail.class})
    private Long agencytid;
    /**
     * 代理公司名称
     */
    @NotBlank(message = "代理公司名称不能为空", groups = {add.class, edit.class, detail.class})
    private String agencyname;
    /**
     * 代理公司业务员id
     */
    @NotNull(message = "代理公司业务员id不能为空", groups = {add.class, edit.class, detail.class})
    private Long agencyemployid;
    /**
     * 代理公司业务员名称
     */
    @NotBlank(message = "代理公司业务员名称不能为空", groups = {add.class, edit.class, detail.class})
    private String agencyemployname;
    /**
     * 内容生产方id
     */
    @NotNull(message = "内容生产方id不能为空", groups = {add.class, edit.class, detail.class})
    private Long agentid;
    /**
     * 内容生产方
     */
    @NotBlank(message = "内容生产方不能为空", groups = {add.class, edit.class, detail.class})
    private String agentname;
    /**
     * 内容生产方业务员id
     */
    @NotNull(message = "内容生产方业务员id不能为空", groups = {add.class, edit.class, detail.class})
    private Long agentemployid;
    /**
     * 内空生产方业务员名称
     */
    @NotBlank(message = "内空生产方业务员名称不能为空", groups = {add.class, edit.class, detail.class})
    private String agentemployname;
    /**
     * 行业id
     */
    @NotNull(message = "行业id不能为空", groups = {add.class, edit.class, detail.class})
    private Long adindustyid;
    /**
     * 行业名称
     */
    @NotBlank(message = "行业名称不能为空", groups = {add.class, edit.class, detail.class})
    private String adindustryname;
    /**
     * 广告类型id
     */
    @NotNull(message = "广告类型id不能为空", groups = {add.class, edit.class, detail.class})
    private Long adtypeid;
    /**
     * 广告类型名称
     */
    @NotBlank(message = "广告类型名称不能为空", groups = {add.class, edit.class, detail.class})
    private String adtypename;
    /**
     * 广告标题
     */
    @NotBlank(message = "广告标题不能为空", groups = {add.class, edit.class, detail.class})
    private String sadtitle;
    /**
     * 广告内容
     */
    @NotBlank(message = "广告内容不能为空", groups = {add.class, edit.class, detail.class})
    private String sadcontent;
    /**
     * 是否有效
     */
    @NotNull(message = "是否有效不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean buse;
    /**
     * 是否删除
     */
    @NotNull(message = "是否删除不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean bdelete;
    /**
     * 预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    @NotNull(message = "预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ipreapprovestatus;
    /**
     * 加刊审批状态
     */
    @NotNull(message = "加刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer iaddapprovestatus;
    /**
     * 改刊审批状态
     */
    @NotNull(message = "改刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer ichangeapprovestatus;
    /**
     * 停刊审批状态
     */
    @NotNull(message = "停刊审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer istopapprovestatus;
    /**
     * 折扣审批状态
     */
    @NotNull(message = "折扣审批状态不能为空", groups = {add.class, edit.class, detail.class})
    private Integer idiscountapprovestatus;
    /**
     * 负责人意见
     */
    @NotBlank(message = "负责人意见不能为空", groups = {add.class, edit.class, detail.class})
    private String sopinion;
    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {add.class, edit.class, detail.class})
    private String sremark;
    /**
     * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
     */
    @NotNull(message = "审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）不能为空", groups = {add.class, edit.class, detail.class})
    private Integer iapprovestatus;
    /**
     * 是否特殊订单(0 否 1-是)
     */
    @NotNull(message = "是否特殊订单(0 否 1-是)不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean bspecial;
    /**
     * 特殊原因
     */
    @NotBlank(message = "特殊原因不能为空", groups = {add.class, edit.class, detail.class})
    private String sspecialreason;
    /**
     * 关联订单id(用于预约补刊)
     */
    private Long relateorderid;
    /**
     * 并发标记
     */
    @NotNull(message = "并发标记不能为空", groups = {add.class, edit.class, detail.class})
    private Long version;

    public static TworderDTO parseToDTO(Tworder entity) {
        TworderDTO TworderDTO = new TworderDTO();
        BeanUtils.copyProperties(entity, TworderDTO);
        return TworderDTO;
    }

    public static Tworder parseToEntity(TworderDTO dto) {
        Tworder entity = new Tworder();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
