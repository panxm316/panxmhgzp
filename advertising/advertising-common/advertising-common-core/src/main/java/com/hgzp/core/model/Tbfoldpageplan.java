package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 版面计划
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Getter
@Setter
@LogData(alias = "版面计划")
public class Tbfoldpageplan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
    private String mediatypename;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 叠次ID
     */
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 叠次版本id
     */
    private Long foldareaverid;

    /**
     * 叠次版本名称
     */
    private String foldareavername;

    /**
     * 刊登日期
     */
    private Date publishdate;

    /**
     * 刊期
     */
    private Integer publishnum;

    /**
     * 版号
     */
    private Integer pagenum;

    /**
     * 版面标题
     */
    @LogData(alias = "版面标题")
    private String pagetitle;

    /**
     * 版心id
     */
    private Long pagesizeid;

    /**
     * 版心名称
     */
    private String pagesizename;

    /**
     * 宽(mm)
     */
    private Integer ipagewidth;

    /**
     * 高(mm)
     */
    private Integer ipageheight;

    /**
     * 色彩id
     */
    @LogData(alias = "色彩id", mappedBy = "tbadcolor", mappedByColumn = "sname")
    private Long adcolorid;

    /**
     * 色彩名称
     */
    @LogData(alias = "色彩名称")
    private String adcolorname;

    /**
     * 广告版标记
     */
    @LogData(alias = "广告版标记")
    private Boolean adflag;

    /**
     * 截版日期
     */
    @LogData(alias = "截版日期")
    private Date stoptime;

    /**
     * 截版标记
     */
    @LogData(alias = "截版标记")
    private Boolean stopflag;

    /**
     * 备注
     */
    private String sremark;
}
