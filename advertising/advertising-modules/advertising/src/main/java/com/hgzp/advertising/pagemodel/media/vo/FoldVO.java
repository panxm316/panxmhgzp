package com.hgzp.advertising.pagemodel.media.vo;

import com.hgzp.core.model.Tbfold;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * FoldModelDTO
 * 创建人：suny
 * 类描述：叠次列表对象
 * 创建日期：2023/9/19 10:41
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoldVO extends BaseQueryInfo {
    private Long id;

    /**
     * 叠次名称
     */
    private String sname;

    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 媒体类型名称
     */
    private String mediatypename;
    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 版数
     */
    private Integer pagecount;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 启用标志
     */
    private Boolean buse;

    public FoldVO(Tbfold tbfold){
        BeanUtils.copyProperties(tbfold, this);
    }
}
