package com.hgzp.mapper.ad;

import com.hgzp.core.model.Tworder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.api.CJAdPrinDTO;
import com.hgzp.pagemodel.business.OrderAndItemDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TworderMapper extends BaseMapper<Tworder> {
    /**
     * 获取打印排版表
     * 方法功能:获取打印排版表
     * @author suny
     * @date 2024/2/28 10:48
     * @param startTime
     * @param endTime
     * @param FoldId
     * @param FoldVer
     * @param FoldPage
     * @param Arrange
     * @param End
     * @return java.util.List<com.hgzp.pagemodel.api.CJAdPrinDTO>
     */
    List<CJAdPrinDTO> selectOrderAndItemListForCJ(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("FoldId") String FoldId,
                                             @Param("FoldVer") String FoldVer, @Param("FoldPage") String FoldPage, @Param("Arrange") Integer Arrange, @Param("End") Integer End);
}
