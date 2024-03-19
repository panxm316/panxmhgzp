package com.hgzp.advertising.pagemodel.media.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tbpagesort;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * PagesortVO
 * 创建人：wangwk
 * 类描述：版面类别VO
 * 创建日期：2023/9/19 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagesortVO extends BaseQueryInfo {
    private Long id;

    /**
     * 版面类别名称
     */
    private String sname;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
    private String mediatypename;

    /**
     * 叠次ID
     */
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 序号
     */
    private Integer isort;
    /**
     * 是否启用
     */
    private Boolean buse;
    /**
     * 媒体id, 查询时此字段用于接收媒体id和叠次id
     */
    private Long mediaid;
    /**
     * 媒体名称
     */
    private String mediaName;

    public PagesortVO(Tbpagesort tbpagesort){
        BeanUtils.copyProperties(tbpagesort,this);
    }
}
