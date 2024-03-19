package com.hgzp.advertising.pagemodel.ad.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.model.Tworderitemfiles;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.page.BaseDTO;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * <p>
 * 订单明细文件表（用于分类广告文件） DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-02-27
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Accessors(chain = true)
public class TworderitemfilesDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {add.class, edit.class, detail.class})
    private Long id;
    /**
     * 广告表id
     */
    @NotNull(message = "广告表id不能为空", groups = {add.class, edit.class, detail.class})
    private Long orderid;
    /**
     * 广告明细表id
     */
    @NotNull(message = "广告明细表id不能为空", groups = {add.class, edit.class, detail.class})
    private Long orderitemid;
    /**
     * 文件格式
     */
    @NotBlank(message = "文件格式不能为空", groups = {add.class, edit.class, detail.class})
    private String sfileformat;
    /**
     * 统一文件ID
     */
    @NotBlank(message = "统一文件ID不能为空", groups = {add.class, edit.class, detail.class})
    private String sfileid;
    /**
     * 文件大小
     */
    @NotBlank(message = "文件大小不能为空", groups = {add.class, edit.class, detail.class})
    private String sfilesize;
    /**
     * 源文件名
     */
    @NotBlank(message = "源文件名不能为空", groups = {add.class, edit.class, detail.class})
    private String soriginalfile;
    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    @NotBlank(message = "文件格式类型(Photo、Video、Audio、Office、Application)不能为空", groups = {add.class, edit.class, detail.class})
    private String sfiletype;
    /**
     * 创建日期
     */
    @NotNull(message = "创建日期不能为空", groups = {add.class, edit.class, detail.class})
    private Date dcreatetime;
    /**
     * 创建者ID
     */
    @NotNull(message = "创建者ID不能为空", groups = {add.class, edit.class, detail.class})
    private Long employid;
    /**
     * 文件描述
     */
    @NotBlank(message = "文件描述不能为空", groups = {add.class, edit.class, detail.class})
    private String sdescription;

    public static TworderitemfilesDTO parseToDTO(Tworderitemfiles entity) {
        TworderitemfilesDTO TworderitemfilesDTO = new TworderitemfilesDTO();
        BeanUtils.copyProperties(entity, TworderitemfilesDTO);
        return TworderitemfilesDTO;
    }

    public static Tworderitemfiles parseToEntity(TworderitemfilesDTO dto) {
        Tworderitemfiles entity = new Tworderitemfiles();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
