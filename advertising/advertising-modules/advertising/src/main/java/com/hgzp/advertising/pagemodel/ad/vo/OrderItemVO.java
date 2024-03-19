package com.hgzp.advertising.pagemodel.ad.vo;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitembelong;
import lombok.Data;

import java.util.List;

/**
 OrderItemVO
 创建人：hgsongly
 类描述：TODO
 创建日期：2023/12/5 13:36
 */
@Data
public class OrderItemVO extends Tworderitem {

    /**订单明细归属*/
    private List<Tworderitembelong> orderitembelong;
}