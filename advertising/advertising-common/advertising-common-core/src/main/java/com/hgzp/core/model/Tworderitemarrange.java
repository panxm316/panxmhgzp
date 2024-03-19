package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 广告安排表
 * </p>
 *
 * @author muyn
 * @since 2023-12-06
 */
@Getter
@Setter
@TableName("tworderitemarrange")
@LogData(alias = "广告安排表")
public class Tworderitemarrange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者
     */
    @LogData(alias = "创建者")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 订单表id
     */
    @LogData(alias = "订单表id")
    private Long orderid;

    /**
     * 订单明细id
     */
    @LogData(alias = "订单明细id")
    private Long orderitemid;

    /**
     * 媒体类型id
     */
    @LogData(alias = "媒体类型key")
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    @LogData(alias = "媒体类型名称")
    private String mediatypename;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体id")
    private Long mediaid;

    /**
     * 媒体名称
     */
    @LogData(alias = "媒体名称")
    private String medianame;

    /**
     * 刊发日期
     */
    @LogData(alias = "刊发日期")
    private Date dpublishdate;

    /**
     * 刊发结束日期
     */
    @LogData(alias = "刊发结束日期")
    private Date dpublishenddate;

    /**
     * 叠次id
     */
    @LogData(alias = "叠次id")
    private Long foldid;

    /**
     * 叠次名称
     */
    @LogData(alias = "叠次名称")
    private String foldname;

    /**
     * 叠次版本id
     */
    @LogData(alias = "叠次版本id")
    private Long foldareaverid;

    /**
     * 叠次版本名称
     */
    @LogData(alias = "叠次版本名称")
    private String foldareavername;

    /**
     * 广告形式id
     */
    @LogData(alias = "广告形式id")
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    @LogData(alias = "广告形式名称")
    private String addisplayformname;

    /**
     * 版面类别id
     */
    @LogData(alias = "版面类别id")
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    @LogData(alias = "版面类别名称")
    private String pagesortname;

    /**
     * 色彩id
     */
    @LogData(alias = "色彩id")
    private Long adcolorid;

    /**
     * 色彩名称
     */
    @LogData(alias = "色彩名称")
    private String adcolorname;

    /**
     * 规格id
     */
    @LogData(alias = "规格id")
    private Long adspecid;

    /**
     * 规格名称
     */
    @LogData(alias = "规格名称")
    private String adspecname;

    /**
     * 宽(cm)
     */
    @LogData(alias = "宽")
    private BigDecimal nwidth;

    /**
     * 高(cm)
     */
    @LogData(alias = "高")
    private BigDecimal nheight;

    /**
     * 版面id
     */
    @LogData(alias = "版面id")
    private Long foldpageplanid;

    /**
     * 版面名称
     */
    @LogData(alias = "版面名称")
    private Integer foldpageplanname;

    /**
     * 刊期
     */
    @LogData(alias = "刊期")
    private Integer publishnum;

    /**
     * 广告标题
     */
    @LogData(alias = "广告标题")
    private String sadtitle;

    /**
     * 广告内容
     */
    @LogData(alias = "广告内容")
    private String sadcontent;

    /**
     * 安排备注
     */
    @LogData(alias = "安排备注")
    private String sremark;

    /**
     * 指定编辑人员id
     */
    @LogData(alias = "指定编辑人员id")
    private Long editorid;

    /**
     * 指定编辑人员名称
     */
    @LogData(alias = "指定编辑人员名称")
    private String editorname;

    /**
     * x坐标
     */
    @LogData(alias = "x坐标")
    private Integer nleftx;

    /**
     * y坐标
     */
    @LogData(alias = "y坐标")
    private Integer ntopy;

    /**
     * 已发布地址(主要用于新媒体)
     */
    @LogData(alias = "已发布地址(主要用于新媒体)")
    private String spublishedurl;

    /**
     * 最后修改者id
     */
    @LogData(alias = "最后修改者id")
    private Long lastoperatorid;

    /**
     * 最后修改才
     */
    @LogData(alias = "最后修改才")
    private String lastoperator;

    /**
     * 最后修改时间
     */
    @LogData(alias = "最后修改时间")
    private Date dlastmodifydate;

    /**
     * 并发标记
     */
    @Version
    private Long version;
}
