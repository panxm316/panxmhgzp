package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * TaskQuotaUpdateDTO
 * 创建人：lhl
 * 类描述：批量更新任务额度请求对象
 * 创建日期：2023/11/11 14:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskQuotaUpdateDTO extends BaseDTO {
    /**
     * 配额（单位万元） 如 35
     */
    @NotNull(message = "配额不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private BigDecimal dQuota;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 配额记录id列表
     */
    List<Long> idList;


}


