package com.hgzp.advertising.pagemodel.flow;

import com.hgzp.core.model.Twapplicationrelations;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 审批流程关联表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-05
 */
@Data
public class TwapplicationrelationsVO extends Twapplicationrelations {
    /**
     * 流程类型名称
     */
    private String flowtypename;
    /**
     * 审批类型名称
     */
    private String approvetypename;

    public static TwapplicationrelationsVO parseToVO(Twapplicationrelations entity) {
        TwapplicationrelationsVO TwapplicationrelationsVO = new TwapplicationrelationsVO();
        BeanUtils.copyProperties(entity, TwapplicationrelationsVO);
        return TwapplicationrelationsVO;
    }

    public static Twapplicationrelations parseToEntity(TwapplicationrelationsVO vo) {
        Twapplicationrelations entity = new Twapplicationrelations();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
