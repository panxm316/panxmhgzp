package com.hgzp.advertising.pagemodel.system.dto;

import com.hgzp.core.model.Twmymenu;
import lombok.*;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.util.List;

/**
 * <p>
 * 我的菜单 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-22
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MymenuDTO {
    private List<Twmymenu> lsSelectmenu;
//    /**
//     * 菜单表id
//     */
//    private Long menuid;
//    /**
//     * 序号
//     */
//    private Integer isort;

//    public static MymenuDTO parseToDTO(Twmymenu entity) {
//        MymenuDTO  mymenuDTO = new MymenuDTO();
//        BeanUtils.copyProperties(entity, mymenuDTO);
//        return mymenuDTO;
//    }
//
//    public static Twmymenu parseToEntity(MymenuDTO dto) {
//        Twmymenu entity = new Twmymenu();
//        BeanUtils.copyProperties(dto, entity);
//        if (entity.getId() == null) { // 主动生成id
//            entity.setId(IdWorker.getId());
//        }
//        return entity;
//    }
}
