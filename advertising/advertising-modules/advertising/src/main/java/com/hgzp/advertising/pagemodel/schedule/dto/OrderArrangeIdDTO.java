package com.hgzp.advertising.pagemodel.schedule.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hgzp.core.annotation.LogData;
import lombok.Data;

/**
 OrderArrangeIdDTO
 创建人：hgsongly
 类描述：TODO
 创建日期：2023/12/19 9:52
 */
@Data
public class OrderArrangeIdDTO {
    /**
     * Id
     */
    private Long id;
    /**
     * 订单表id
     */
    private Long orderid;

    /**
     * 订单明细id
     */
    private Long orderitemid;
    /**
     * 并发标记
     */
    private Long version;
    /**
     * x坐标
     */
    private Integer nleftx;
    /**
     * y坐标
     */
    private Integer ntopy;
}