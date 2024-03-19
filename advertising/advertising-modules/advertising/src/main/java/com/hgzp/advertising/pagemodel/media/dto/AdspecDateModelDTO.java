package com.hgzp.advertising.pagemodel.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * AdspecDateModelDTO
 * 创建人：CGD
 * 类描述：TODO
 * 创建日期：2023/9/20 13:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdspecDateModelDTO {
    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;
    /**
     * 原结束日期
     */
    private Date olddenddate;

}

