package com.hgzp.advertising.pagemodel.schedule.dto;

import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.util.List;


/**
 OrderItemBatchArrangeDTO
 创建人：songly
 类描述：订单明细批量安排
 创建日期：2023/12/18 14:39
 */
@Data
public class OrderItemBatchArrangeDTO {
    /**
     * 订单ids
     */
    private List<OrderArrangeIdDTO> orderitemids;
    /**
     * 叠次id
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
     * 版面id
     */
    private Long foldpageplanid;
    /**
     * 版面名称
     */
    private String foldpageplanname;
    /**
     * 指定编辑人员id
     */
    private Long editorid;
    /**
     * 指定编辑人员名称
     */
    private String editorname;
    /**
     * 备注
     */
    private String sremark;

}