package com.hgzp.advertising.pagemodel.statistic.vo;

import com.hgzp.core.model.Tbreportmodelitem;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 报表模板明细设置 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportmodelItemVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 报表模板组id
     */
    private Long reportmodelgroupid;
    /**
     * 类型(dept,media)
     */
    private String reporttype;
    /**
     * 表头名称
     */
    private String sheadername;
    /**
     * 部门类型(0-总部 1-区域)
     */
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

    public static ReportmodelItemVO parseToVO(Tbreportmodelitem entity) {
        ReportmodelItemVO TbreportmodelitemVO = new ReportmodelItemVO();
        BeanUtils.copyProperties(entity, TbreportmodelitemVO);
        return TbreportmodelitemVO;
    }

    public static Tbreportmodelitem parseToEntity(ReportmodelItemVO vo) {
        Tbreportmodelitem entity = new Tbreportmodelitem();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
