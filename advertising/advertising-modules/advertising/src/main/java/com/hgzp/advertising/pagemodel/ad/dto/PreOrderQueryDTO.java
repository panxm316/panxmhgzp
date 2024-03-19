package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 广告预约查询DTO
 *
 * @author wangxk
 * @since 2023-12-29
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PreOrderQueryDTO extends BaseQueryInfo {


    /**
     * 录入方式 1-广告预约 2-快速预约
     */
    @NotBlank(message = "录入方式不可为空")
    @Pattern(regexp = "^[12]$", message = "录入方式只能为1或2")
    private String bookType;
}
