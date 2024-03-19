package com.hgzp.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Twcustomercharge;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 客户收费表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TwcustomerchargeMapper extends BaseMapper<Twcustomercharge> {
    Twcustomercharge getSumCustomerCharge(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("customerid") Long customerid);
}
