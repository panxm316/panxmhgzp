package com.hgzp.advertising.pagemodel.schedule.vo;

/**
 * @author new wei
 * @date 2023/12/13 14:37
 */

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tworderitemarrange;
import com.hgzp.core.page.DataCombo;
import lombok.Data;

import java.util.List;

/**
 * OrderitemarrangeVO
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/12/13 14:37
 */
@Data
public class OrderitemarrangeVO extends Tworderitemarrange {
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 客户名称
     */
    private String customername;
    /**
     * 广告类型id
     */
    private Long adtypeid;
    /**
     * 广告类型名称
     */
    private String adtypename;
    /**
     * 预刊发叠次id
     */
    private Long prefoldid;

    /**
     * 预刊发叠次名称
     */
    private String prefoldname;

    /**
     * 预刊发叠次版本id
     */
    private Long prefoldareaverid;

    /**
     * 预刊发叠次版本名称
     */
    private String prefoldareavername;
    /**
     * 预刊发版面id
     */
    private Long prefoldpageplanid;

    /**
     * 预刊发版面名称
     */
    private Integer prefoldpageplanname;
    /**
     * 备注
     */
    private String presremark;
    /**
     * 业务id
     */
    private Long employid;

    /**
     * 业务员名称
     */
    private String employname;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 发布状态名称
     */
    private String spublishstatusName;
    /**
     * 版数
     */
    private List<DataCombo> pageNumList;
}