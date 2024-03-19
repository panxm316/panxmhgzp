package com.hgzp.advertising.pagemodel.finance.dto;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hgzp.core.model.Twcommission;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 佣金计提明细表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 刊期id
     */
    private Long orderitemid;
    /**
     * 刊期编码
     */
    private Long orderitemcode;
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
    private Date dcreatetime;
    /**
     * 业务员部门id
     */
    @NotNull(message = "业务员部门id不能为空", groups = {add.class, edit.class, detail.class})
    private Long deptid;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {add.class, edit.class, detail.class})
    private String deptname;
    /**
     * 业务员id
     */
    @NotNull(message = "业务员id不能为空", groups = {add.class, edit.class, detail.class})
    private Long employid;
    /**
     * 业务员名称
     */
    @NotBlank(message = "业务员名称不能为空", groups = {add.class, edit.class, detail.class})
    private String employname;
    /**
     * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
     */
    private Integer employtype;
    /**
     * 是否主业务员
     */
    private Boolean bprimary;
    /**
     * 客户id(与业务员相关的)
     */
    private Long customerid;
    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {add.class, edit.class, detail.class})
    private String customername;
    /**
     * 业绩金额
     */
    @NotNull(message = "业绩金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal amountachievement;
    /**
     * 业绩比例
     */
    @NotNull(message = "业绩比例不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal archievementrate;
    /**
     * 成本金额
     */
    @NotNull(message = "成本金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal namountcost;
    /**
     * 风险金比例%
     */
    @NotNull(message = "风险金比例%不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal nrateofrisk;
    /**
     * 佣金参数组id
     */
    private Long commissionrategroupid;
    /**
     * 佣金参数明细id
     */
    private Long commissionrateitemid;
    /**
     * 计提比例
     */
    @NotNull(message = "计提比例不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal ncommissionrate;
    /**
     * 计提金额
     */
    @NotNull(message = "计提金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal ncommission;
    /**
     * 备注
     */
    private String sremark;
    /**
     * 标记(0-计算1-确认 2-发放 3-撤销)
     */
    private Integer icommissionstatus;
    /**
     * 是否冲抵0否 1是
     */
    private Boolean bcancel;
    /**
     * 如果冲抵，则冲抵记录的id
     */
    private Long relatedid;
    /**
     * 确认日期
     */
    private Date dconfirmdate;
    /**
     * 确认者
     */
    private Long confirmempid;
    /**
     *
     */
    private String confirmempname;
    /**
     * 确认说明
     */
    private String sconfirmremark;
    /**
     * 发放日期
     */
    private Date dpaydate;
    /**
     * 发放者
     */
    private Long payempid;
    /**
     *
     */
    private String payempname;
    /**
     * 发放说明
     */
    private String spayremark;
    /**
     * 并发标记
     */
    private Long version;

    public static CommissionDTO parseToDTO(Twcommission entity) {
        CommissionDTO commissionDTO = new CommissionDTO();
        BeanUtils.copyProperties(entity, commissionDTO);
        return commissionDTO;
    }

    public static Twcommission parseToEntity(CommissionDTO dto) {
        Twcommission entity = new Twcommission();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
