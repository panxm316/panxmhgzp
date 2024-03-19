package com.hgzp.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Twcommission;
import com.hgzp.pagemodel.business.OrderItemAndCommissionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 佣金计提明细表 Mapper 接口
 * </p>
 *
 * @author muyn
 * @since 2024-01-06
 */
public interface TwcommissionMapper extends BaseMapper<Twcommission> {
    /**
     * 根据查询条件获取佣金汇总
     * 方法功能:  根据查询条件获取佣金汇总
     *
     * @param startTime
     * @param endTime
     * @param deptidList
     * @param employid
     * @return com.hgzp.pagemodel.business.OrderItemAndCommissionDTO
     * @author suny
     * @date 2024/2/1 9:02
     */
    OrderItemAndCommissionDTO getSumCommission(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                               @Param("deptidList") List<Long> deptidList, @Param("employid") Long employid);
}
