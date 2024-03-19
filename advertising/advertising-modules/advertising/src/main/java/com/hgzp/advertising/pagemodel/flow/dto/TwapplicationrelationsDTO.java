package com.hgzp.advertising.pagemodel.flow.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.model.Twapplicationrelations;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 审批流程关联表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwapplicationrelationsDTO extends BaseDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 流程类型
     */
    private String flowtype;

    /**
     * 业务主id(比如orderid)
     */
    private Long mainid;
    /**
     * 业务从id(比如orderitemid)
     */
    private Long childid;
    /**
     * 审批流程id
     */
    private String applicationid;
    /**
     * 审批类型
     */
    private String approvetype;
    /**
     * 新客户id
     */
    private Long newcustomerid;

    /**
     * 新经营主体id
     */
    private Long newbusinessentityid;

    public static TwapplicationrelationsDTO parseToDTO(Twapplicationrelations entity) {
        TwapplicationrelationsDTO TwapplicationrelationsDTO = new TwapplicationrelationsDTO();
        BeanUtils.copyProperties(entity, TwapplicationrelationsDTO);
        return TwapplicationrelationsDTO;
    }

    public static Twapplicationrelations parseToEntity(TwapplicationrelationsDTO dto) {
        Twapplicationrelations entity = new Twapplicationrelations();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
