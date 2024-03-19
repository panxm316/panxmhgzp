package com.hgzp.advertising.pagemodel.statistic.dto;

import com.hgzp.core.model.Tbreportmodelitem;
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
 * 报表模板明细设置 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportmodelItemDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 报表模板组id
     */
    @NotNull(message = "报表模板组id不能为空", groups = {add.class, edit.class, detail.class})
    private Long reportmodelgroupid;
    /**
     * 类型(dept,media)
     */
    @NotBlank(message = "类型(dept,media)不能为空", groups = {add.class, edit.class, detail.class})
    private String reporttype;
    /**
     * 表头名称
     */
    @NotBlank(message = "表头名称不能为空", groups = {add.class, edit.class, detail.class})
    private String sheadername;
    /**
     * 部门类型(0-总部 1-区域)
     */
    @NotNull(message = "部门类型(0-总部 1-区域)不能为空", groups = {add.class, edit.class, detail.class})
    private Integer idepttype;
    /**
     * 字段名
     */
    private String scolumnname;
    /**
     * 级别
     */
    private Integer ilevel;
    /**
     * 数据范围名称
     */
    private String snames;
    /**
     * 数据范围id串
     */
    private String sids;
    /**
     * 序号
     */
    private Integer isort;

    public static ReportmodelItemDTO parseToDTO(Tbreportmodelitem entity) {
        ReportmodelItemDTO TbreportmodelitemDTO = new ReportmodelItemDTO();
        BeanUtils.copyProperties(entity, TbreportmodelitemDTO);
        return TbreportmodelitemDTO;
    }

    public static Tbreportmodelitem parseToEntity(ReportmodelItemDTO dto) {
        Tbreportmodelitem entity = new Tbreportmodelitem();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
