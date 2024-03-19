package com.hgzp.advertising.pagemodel.statistic.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twdatabackupgroup;
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
 * 数据轧账总表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabackupGroupDTO extends BaseDTO {

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
    @NotBlank(message = "名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String databackupname;
    /**
     * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
     */
    private String datatype;
    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Date dstartdate;
    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Date denddate;
    /**
     * 记录数
     */
    private Integer nrecordsize;
    /**
     * 明细表名
     */
    private String sdetailtablename;
    /**
     * 说明
     */
    private String sdescription;

    public static DatabackupGroupDTO parseToDTO(Twdatabackupgroup entity) {
        DatabackupGroupDTO databackupgroupDTO = new DatabackupGroupDTO();
        BeanUtils.copyProperties(entity, databackupgroupDTO);
        return databackupgroupDTO;
    }

    public static Twdatabackupgroup parseToEntity(DatabackupGroupDTO dto) {
        Twdatabackupgroup entity = new Twdatabackupgroup();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getId() == null) { // 主动生成id
            entity.setId(IdWorker.getId());
        }
        return entity;
    }
}
