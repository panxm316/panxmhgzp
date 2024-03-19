package com.hgzp.mapper.ad;

import com.hgzp.core.model.Tbadfrom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.ad.AdCustomerResourceVO;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告来源信息表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbadfromMapper extends BaseMapper<Tbadfrom> {
    /**
     * 汇总期间区域新增客户数量
     * 方法功能:  汇总期间区域新增客户数量
     *
     * @return AdCustomerResourceVO
     * @author lhl
     * @date 2024/3/12
     */
    Long summaryCustomer(@Param("ids") List<Long> ids, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime);
}
