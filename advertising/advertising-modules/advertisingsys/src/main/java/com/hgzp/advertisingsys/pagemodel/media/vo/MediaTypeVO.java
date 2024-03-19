package com.hgzp.advertisingsys.pagemodel.media.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MediaTypeVO
 * 创建人：yanz
 * 类描述：媒体类型 VO
 * 创建日期：2023/8/31 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaTypeVO extends BaseQueryInfo {
    /**
     * 是否启用
     */
    private Boolean buse;

    public Boolean getBuse() {
        return buse;
    }


}