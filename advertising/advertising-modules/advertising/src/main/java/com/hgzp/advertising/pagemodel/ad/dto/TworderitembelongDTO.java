package com.hgzp.advertising.pagemodel.ad.dto;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hgzp.core.model.Tworderitembelong;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 订单刊期归属表 DTO 数据传输对象
 * </p>
 *
 * @author wangxk
 * @since 2024-01-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class TworderitembelongDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {add.class, edit.class, detail.class})
    private Long id;
    /**
     * 订单明细id
     */
    @NotNull(message = "订单明细id不能为空", groups = {add.class, edit.class, detail.class})
    private Long orderitemid;
    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空", groups = {add.class, edit.class, detail.class})
    private String sordernum;
    /**
     * 合同号
     */
    @NotBlank(message = "合同号不能为空", groups = {add.class, edit.class, detail.class})
    private String scontractnum;
    /**
     * 创建者id
     */
    @NotNull(message = "创建者id不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private Long createempid;
    /**
     * 创建者名称
     */
    @NotBlank(message = "创建者名称不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private String createempname;
    /**
     * 创建日期
     */
    @NotNull(message = "创建日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date createtime;
    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private Long deptid;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private String deptname;
    /**
     * 业务id
     */
    @NotNull(message = "业务id不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private Long employid;
    /**
     * 业务员名称
     */
    @NotBlank(message = "业务员名称不能为空", groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private String employname;
    /**
     * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
     */
    @NotNull(message = "类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）不能为空",
            groups = {add.class, edit.class, detail.class, savePreOrder.class})
    private Integer employtype;
    /**
     * 业绩比例
     */
    @NotNull(message = "业绩比例不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal archievementrate;
    /**
     * 任务比例
     */
    @NotNull(message = "任务比例不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal taskrate;
    /**
     * 佣金比例
     */
    @NotNull(message = "佣金比例不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal commissionrate;
    /**
     * 是否主业务员
     */
    @NotNull(message = "是否主业务员不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean bprimary;
    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {add.class, edit.class, detail.class})
    private String sremark;

    public static TworderitembelongDTO parseToDTO(Tworderitembelong entity) {
        TworderitembelongDTO TworderitembelongDTO = new TworderitembelongDTO();
        BeanUtils.copyProperties(entity, TworderitembelongDTO);
        return TworderitembelongDTO;
    }

    public static Tworderitembelong parseToEntity(TworderitembelongDTO dto) {
        Tworderitembelong entity = new Tworderitembelong();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) {
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
