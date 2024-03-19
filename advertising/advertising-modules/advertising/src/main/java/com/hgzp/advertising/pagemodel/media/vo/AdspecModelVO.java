package com.hgzp.advertising.pagemodel.media.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tbadspec;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AdspecModelVO
 * 创建人：CGD
 * 类描述：广告规格vo类
 * 创建日期：2023/9/19 13:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdspecModelVO extends BaseQueryInfo {

    /**
     * id
     */
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 类型
     */
    private String stype;

    /**
     * 规格名称
     */
    private String sname;

    /**
     * 宽
     */
    private BigDecimal nwidth;

    /**
     * 高
     */
    private BigDecimal nheight;

    /**
     * 面积
     */
    private BigDecimal narea;

    /**
     * 格子数
     */
    private Integer ipknum;
    /**
     * 是否分类广告
     */
    private Boolean bclassified;

    /**
     * 是否大分类广告
     */
    private Boolean bbigclassified;
    /**
     * 显示设置
     */
    private String sspecdisplay;

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
     * 是否启用
     */
    private Boolean buse;


    public AdspecModelVO(Tbadspec tbadspec){
        BeanUtils.copyProperties(tbadspec, this);
    }
}

