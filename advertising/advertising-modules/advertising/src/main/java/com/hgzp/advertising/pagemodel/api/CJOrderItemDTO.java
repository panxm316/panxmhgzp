package com.hgzp.advertising.pagemodel.api;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

/**
 * CJOrderItem
 * 创建人：suny
 * 类描述：广告订单对象DTO用于超捷
 * 创建日期：2024/2/29 13:38
 */
@Data
public class CJOrderItemDTO extends BaseDTO {
    /**
     * 是否截稿，默认是
     */
    private String smallad;
    /**
     * 安排状态
     */
    private String pagestatus;
    /**
     * 客户id
     */
    private String custid;
    /**
     * 客户名称
     */
    private String custname;
    /**
     * 广告编号
     */
    private String inforid;
    /**
     * 刊发时间
     */
    private String publdate;
    /**
     * 广告类型/类别id
     */
    private String kinddetid;
    /**
     * 广告类别
     */
    private String kinddetail;
    /**
     * 广告叠次id
     */
    private String foldid;
    /**
     * 广告叠次
     */
    private String fold;
    /**
     * 广告规格
     */
    private String adaxb;
    /**
     * 广告规格id
     */
    private String adaxbid;
    /**
     * 叠次版本id
     */
    private String foldedid;
    /**
     * 广告版本
     */
    private String foldedition;
    /**
     * 广告色彩
     */
    private String adcolor;
    /**
     * 广告标题
     */
    private String adtitle;
    /**
     * 合同号
     */
    private String compactid;
    /**
     * 刊期
     */
    private String publnum;
    /**
     * 明细版面
     */
    private String pagesnum;
    /**
     * 广告路径
     */
    private String adPath;
    /**
     * 广告内容
     */
    private String filecontent;
    /**
     * 广告图片路径
     */
    private String picturepath;
    /**
     * 广告类别parentid
     */
    private String parentid;
    /**
     * 广告类别localId
     */
    private String localId;
    /**
     * 广告类别
     */
    private String adkind;
    /**
     * 广告类别id
     */
    private String adkindid;
    /**
     * 广告行业parentid
     */
    private String paperid;
    /**
     * 报纸地域
     */
    private String paperver;
    /**
     * 报刊名称
     */
    private String adVer;
    /**
     * 录入时间
     */
    private String engagetime;
    /**
     * 广告备注
     */
    private String remark;
    /**
     * 是否指定版面，根据广告版面名称判断，如果有版面名称则为指定版面
     */
    private String EditorSet;
    /**
     * 广告高度
     */
    private String height;
    /**
     * 广告宽度
     */
    private String width;
    /**
     * 数量
     */
    private String pknum;
    /**
     * 价格计算标志/是否上传图片070114牟加
     */
    private String ppflg;
    /**
     * 计价方式(全部按格)
     */
    private String pricetype;

}

