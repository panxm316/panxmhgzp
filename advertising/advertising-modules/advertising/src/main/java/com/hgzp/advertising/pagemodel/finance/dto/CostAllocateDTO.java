package com.hgzp.advertising.pagemodel.finance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostallocate;
import lombok.*;
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * <p>
 * 成本分配明细表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CostAllocateDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {add.class, edit.class, detail.class})
    private Long id;
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
    private Date dcreatetime;
    /**
     * 成本表id
     */
    @NotNull(message = "成本表id不能为空", groups = {add.class, edit.class, detail.class})
    private Long costid;
    /**
     * 成本类型(employ,project)
     */
    @NotBlank(message = "成本类型(employ,project)不能为空", groups = {add.class, edit.class, detail.class})
    private String costtype;
    /**
     * 分配金额
     */
    @NotNull(message = "分配金额不能为空", groups = {add.class, edit.class, detail.class})
    private BigDecimal namountallcate;
    /**
     * 订单明细id
     */
    @NotNull(message = "订单明细id不能为空", groups = {add.class, edit.class, detail.class})
    private Long orderitemid;
    /**
     * 说明
     */
    @NotBlank(message = "说明不能为空", groups = {add.class, edit.class, detail.class})
    private String sdescription;
    /**
     * 状态(0-待提交 1-已提交 2-已确认）
     */
    @NotNull(message = "状态(0-待提交 1-已提交 2-已确认）不能为空", groups = {add.class, edit.class, detail.class})
    private Integer istatus;
    /**
     * 最后操作员id
     */
    @NotNull(message = "最后操作员id不能为空", groups = {add.class, edit.class, detail.class})
    private Long lastoperatorid;
    /**
     * 最后操作员
     */
    @NotBlank(message = "最后操作员不能为空", groups = {add.class, edit.class, detail.class})
    private String lastoperator;
    /**
     * 最后修改日期
     */
    @NotNull(message = "最后修改日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date dlastmodifydate;
    /**
     * 并发标记
     */
    @NotNull(message = "并发标记不能为空", groups = {add.class, edit.class, detail.class})
    private Long version;

    public static CostAllocateDTO parseToDTO(Twcostallocate entity) {
        CostAllocateDTO TwcostallocateDTO = new CostAllocateDTO();
        BeanUtils.copyProperties(entity, TwcostallocateDTO);
        return TwcostallocateDTO;
    }

    public static Twcostallocate parseToEntity(CostAllocateDTO dto) {
        Twcostallocate entity = new Twcostallocate();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
