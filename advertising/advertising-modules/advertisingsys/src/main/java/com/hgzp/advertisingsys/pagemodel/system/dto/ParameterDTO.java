package com.hgzp.advertisingsys.pagemodel.system.dto;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hgzp.core.model.Twparameter;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 系统参数表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-02-23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParameterDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {add.class, edit.class, detail.class})
    private Long id;
    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空", groups = {add.class, edit.class, detail.class})
    private String skey;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {add.class, edit.class, detail.class})
    private String sname;
    /**
     * 属性值
     */
    @NotBlank(message = "属性值不能为空", groups = {add.class, edit.class, detail.class})
    private String svalue;
    /**
     * 说明
     */
    @NotBlank(message = "说明不能为空", groups = {add.class, edit.class, detail.class})
    private String sdiscription;
    /**
     * 启用
     */
    @NotNull(message = "启用不能为空", groups = {add.class, edit.class, detail.class})
    private Boolean buse;

    public static ParameterDTO parseToDTO(Twparameter entity) {
        ParameterDTO parameterDTO = new ParameterDTO();
        BeanUtils.copyProperties(entity, parameterDTO);
        return parameterDTO;
    }

    public static Twparameter parseToEntity(ParameterDTO dto) {
        Twparameter entity = new Twparameter();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
