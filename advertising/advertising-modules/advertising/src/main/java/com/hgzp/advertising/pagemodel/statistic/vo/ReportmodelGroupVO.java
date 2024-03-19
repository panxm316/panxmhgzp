package com.hgzp.advertising.pagemodel.statistic.vo;

import java.util.Date;

import com.hgzp.core.model.Tbreportmodelgroup;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 报表模板组设置 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportmodelGroupVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
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
     * 名称
     */
    private String reportname;
    /**
     * 汇总类型(dept,media,all)
     */
    private String reporttype;
    /**
     * 应用于(菜单路径)
     */
    private String applyto;
    /**
     * 启用
     */
    private Boolean buse;
    /**
     * 说明
     */
    private String sdescription;

    public static ReportmodelGroupVO parseToVO(Tbreportmodelgroup entity) {
        ReportmodelGroupVO reportmodelgroupVO = new ReportmodelGroupVO();
        BeanUtils.copyProperties(entity, reportmodelgroupVO);
        return reportmodelgroupVO;
    }

    public static Tbreportmodelgroup parseToEntity(ReportmodelGroupVO vo) {
        Tbreportmodelgroup entity = new Tbreportmodelgroup();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
