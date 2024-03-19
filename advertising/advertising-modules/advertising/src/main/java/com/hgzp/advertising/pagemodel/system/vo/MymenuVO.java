package com.hgzp.advertising.pagemodel.system.vo;

import java.util.Date;
import com.hgzp.core.model.Twmymenu;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 我的菜单 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-22
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MymenuVO extends Twmymenu {
    /**
     * 菜单表名称
     */
    private String menuName;
    public static MymenuVO parseToVO(Twmymenu entity) {
        MymenuVO mymenuVO = new MymenuVO();
        BeanUtils.copyProperties(entity, mymenuVO);
        return mymenuVO;
    }

    public static Twmymenu parseToEntity(MymenuVO vo) {
        Twmymenu entity = new Twmymenu();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
