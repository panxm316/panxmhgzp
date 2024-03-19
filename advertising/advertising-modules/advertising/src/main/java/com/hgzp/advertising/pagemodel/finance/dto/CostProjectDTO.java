package com.hgzp.advertising.pagemodel.finance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twcostproject;
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
 * 成本表(项目) DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostProjectDTO extends BaseDTO {

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
     * 项目代码
     */
    @NotBlank(message = "项目代码不能为空", groups = {add.class, edit.class, detail.class})
    private String sprojectcode;
    /**
     * 活动名称
     */
    @NotBlank(message = "活动名称不能为空", groups = {add.class, edit.class, detail.class})
    private String sprojectname;
    /**
     * 科目名称
     */
    @NotBlank(message = "科目名称不能为空", groups = {add.class, edit.class, detail.class})
    private String ssubjectname;
    /**
     * 科目代码
     */
    @NotBlank(message = "科目代码不能为空", groups = {add.class, edit.class, detail.class})
    private String ssubjectcode;
    /**
     * 期初余额借方
     */
    private String sfirstborrower;
    /**
     * 期初余额贷方
     */
    private String sfirstlender;
    /**
     * 本期发生额借方
     */
    private BigDecimal ncurrentborrower;
    /**
     * 分配金额
     */
    private BigDecimal namountallcate;
    /**
     * 本期发生额贷方
     */
    private BigDecimal ncurrentlender;
    /**
     * 本年累计借方
     */
    private BigDecimal nyearborrower;
    /**
     * 本年累计贷方
     */
    private BigDecimal nyearlender;
    /**
     * 期末余额借方
     */
    private BigDecimal nendborrower;
    /**
     * 期末余额贷方
     */
    private BigDecimal nendlender;
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

    public static CostProjectDTO parseToDTO(Twcostproject entity) {
        CostProjectDTO TwcostprojectDTO = new CostProjectDTO();
        BeanUtils.copyProperties(entity, TwcostprojectDTO);
        return TwcostprojectDTO;
    }

    public static Twcostproject parseToEntity(CostProjectDTO dto) {
        Twcostproject entity = new Twcostproject();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
