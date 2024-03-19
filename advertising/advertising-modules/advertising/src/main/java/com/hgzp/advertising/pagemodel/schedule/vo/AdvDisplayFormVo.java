package com.hgzp.advertising.pagemodel.schedule.vo;
import lombok.Data;

import java.util.List;

/**
 * AdvDisplayFormVo
 * 创建人：lhl
 * 类描述：TODO
 * 创建日期：2023/12/7 13:16
 */
@Data
public class AdvDisplayFormVo {
    /**
     * 广告形式ID
     */
    private Long id;
    /**
     * 广告形式名称
     */
    private String name;
    /**
     * 广告日订单
     */
    List<AdvOfDayVo>  advOfDayVoList;



}


