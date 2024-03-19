package com.hgzp.mapper.customer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Twcustomer;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
public interface TwcustomerMapper extends BaseMapper<Twcustomer> {
    /**
     * 根据人员归属查询客户列表
     * 方法功能: 根据人员归属查询客户列表
     *
     * @param startTime
     * @param endTime
     * @param employid
     * @param pageNum
     * @param pageSize
     * @return java.util.List<com.hgzp.core.model.Twcustomer>
     * @author suny
     * @date 2024/1/26 16:37
     */
    List<Twcustomer> selectListByBelongid(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                          @Param("employid") Long employid, @Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 根据人员归属查询客户列表总数
     * 方法功能: 根据人员归属查询客户列表总数
     *
     * @param employid
     * @return java.lang.Long
     * @author suny
     * @date 2024/1/24 10:41
     */
    Long getListCountByBelongid(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("employid") Long employid);

    /**
     * 查询客户
     * 方法功能:  查询客户
     *
     * @return Twcustomer
     * @author lhl
     * @date 2024/3/13
     */
    List<Twcustomer> getCustomerList(@Param("ids") List<Long> ids, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime);

}
