package com.hgzp.advertising.pagemodel.finance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostemploy;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * <p>
 * 成本表(人员) DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostEmployDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, detail.class})
    private Long id;
    /**
     * 创建者id
     */
    @NotNull(message = "创建者id不能为空", groups = {edit.class, detail.class})
    private Long createempid;
    /**
     * 创建者名称
     */
    @NotBlank(message = "创建者名称不能为空", groups = {edit.class, detail.class})
    private String createempname;
    /**
     * 导入时间
     */
    @NotNull(message = "导入时间不能为空", groups = {edit.class, detail.class})
    private Date dcreatetime;
    /**
     * 记账日期
     */
    private Date daccountdate;
    /**
     * 业务日期
     */
    private Date dbusinessdate;
    /**
     * 凭据单号
     */
    private String scredential;
    /**
     * 摘要
     */
    private String sdescription;
    /**
     * 借方名称
     */
    private String sborrowername;
    /**
     * 贷方名称
     */
    private String slendername;
    /**
     * 金额类型
     */
    @NotBlank(message = "金额类型不能为空", groups = {add.class, edit.class, detail.class})
    private String scredentialtype;
    /**
     * 金额
     */
    private BigDecimal namount;
    /**
     * 分配金额
     */
    private BigDecimal namountallcate;
    /**
     * 导入历史ID
     */
    @NotNull(message = "导入历史ID不能为空", groups = {edit.class, detail.class})
    private Long bankaccounthistoryid;
    /**
     * 最后操作员id
     */
    private Long lastoperatorid;
    /**
     * 最后操作员
     */
    private String lastoperator;
    /**
     * 最后修改日期
     */
    private Date dlastoperatedate;
    /**
     * 并发标记
     */
    @NotNull(message = "并发标记不能为空", groups = {add.class, edit.class, detail.class})
    private Long version;

    public static CostEmployDTO parseToDTO(Twcostemploy entity) {
        CostEmployDTO TwcostemployDTO = new CostEmployDTO();
        BeanUtils.copyProperties(entity, TwcostemployDTO);
        return TwcostemployDTO;
    }

    public static Twcostemploy parseToEntity(CostEmployDTO dto) {
        Twcostemploy entity = new Twcostemploy();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
