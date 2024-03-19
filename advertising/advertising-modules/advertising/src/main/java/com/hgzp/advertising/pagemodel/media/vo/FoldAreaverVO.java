package com.hgzp.advertising.pagemodel.media.vo;
import com.hgzp.core.model.Tbfoldareaver;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 FoldAreaverModelVo
 创建人：songly
 类描述：叠次版本查询
 创建日期：2023/9/18 15:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoldAreaverVO extends BaseQueryInfo {
    /**
     * id
     */
    private Long id;

    /**
     * 叠次版本名称
     */
    private String sname;

    /**
     * 叠次id
     */
    private Long foldid;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

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
    /**
     * 叠次名称
     */
    private String foldname;
    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 媒体Id
     */
    private Long mediaid;
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    public FoldAreaverVO(Tbfoldareaver tbfoldareaver){
        BeanUtils.copyProperties(tbfoldareaver, this);
    }
}
