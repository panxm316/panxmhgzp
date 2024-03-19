package com.hgzp.advertising.pagemodel.statistic.dto;

import java.util.Date;

import com.hgzp.core.model.Tbreportmodelgroup;
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
 * 报表模板组设置 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportmodelGroupDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
    private Date dcreatetime;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {add.class, edit.class, detail.class})
    private String reportname;
    /**
     * 汇总类型(dept,media,all)
     */
    @NotBlank(message = "汇总类型(dept,media,all)不能为空", groups = {add.class, edit.class, detail.class})
    private String reporttype;
    /**
     * 应用于(菜单路径)
     */
    @NotBlank(message = "应用于(菜单路径)不能为空", groups = {add.class, edit.class, detail.class})
    private String applyto;
    /**
     * 启用
     */
    @NotNull(message = "启用不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean buse;
    /**
     * 说明
     */
    private String sdescription;

    public static ReportmodelGroupDTO parseToDTO(Tbreportmodelgroup entity) {
        ReportmodelGroupDTO TbreportmodelgroupDTO = new ReportmodelGroupDTO();
        BeanUtils.copyProperties(entity, TbreportmodelgroupDTO);
        return TbreportmodelgroupDTO;
    }

    public static Tbreportmodelgroup parseToEntity(ReportmodelGroupDTO dto) {
        Tbreportmodelgroup entity = new Tbreportmodelgroup();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
