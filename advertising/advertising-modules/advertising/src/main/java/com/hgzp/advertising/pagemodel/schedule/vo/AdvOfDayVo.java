package com.hgzp.advertising.pagemodel.schedule.vo;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;
import lombok.Data;

import java.util.List;

/**
 * AdvOfDayVo
 * 创建人：lhl
 * 类描述：TODO
 * 创建日期：2023/12/7 13:25
 */
@Data
public class AdvOfDayVo {
    /**
     * 天
     */
    private int day;
    /**
     * 广告订单
     */
    List<PageOrderItemVo>  pageOrderItemVoList;

}


