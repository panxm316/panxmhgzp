package com.hgzp.advertisingsys.pagemodel.flow.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.model.Tbflow;
import lombok.*;
import lombok.experimental.Accessors;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 流程设置 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-02-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowVO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 基础流程名称
     */
    private String sname;
    /**
     * 流程关键字
     */
    private String skey;
    /**
     * 描述
     */
    private String sdesc;
    /**
     * 排序
     */
    private Integer isort;
    /**
     * 是否启用
     */
    private Boolean bactive;
    /**
     * 是否启用
     */
    private Boolean buse;

    public static FlowVO parseToVO(Tbflow entity) {
        FlowVO  flowVO = new FlowVO();
        BeanUtils.copyProperties(entity, flowVO);
        return flowVO;
    }

    public static Tbflow parseToEntity(FlowVO vo) {
        Tbflow entity = new Tbflow();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
