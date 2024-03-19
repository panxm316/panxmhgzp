package com.hgzp.mapper.finance;

import com.hgzp.core.model.Tworderapportiondetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.pagemodel.ad.AdSummaryAllVO;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.statistic.AdChangeVo;
import com.hgzp.pagemodel.statistic.SynQueryItem;
import com.hgzp.pagemodel.statistic.SynQueryResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告分摊明细表 Mapper 接口
 * </p>
 *
 * @author muyn
 * @since 2023-12-18
 */
public interface TworderapportiondetailMapper extends BaseMapper<Tworderapportiondetail> {
    /**
     * 汇总分摊金额
     * 方法功能:  汇总分摊金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     * */
    AdSummaryVO summaryNamountapportion(@Param("departmentId")String departmentId,@Param("companyId")String companyId, @Param("startTime")String startTime, @Param("endTime")String endTime);
    /**
     * 汇总分摊金额部门数组
     * 方法功能:  汇总分摊金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     * */
    AdSummaryVO summaryNamountapportionArray(@Param("ids") List<String> ids, @Param("companyId")String companyId, @Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 汇总分摊金额年度业务
     * 方法功能:  汇总分摊金额年度业务
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/25
     * */
    AdSummaryVO summaryNamountapportionMedia(@Param("ids") List<String> ids,@Param("meidaids") List<String> mediaids,@Param("floadids") List<String> floadids,@Param("foldareaverids") List<String> foldareaverids, @Param("companyId")String companyId, @Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 汇总部门应收金额
     * 方法功能:  汇总部门应收金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/2/1
     * */
    AdSummaryVO summaryReceivable(@Param("depids") List<String> depids,@Param("companyId")String companyId,@Param("meidaids") List<String> mediaids,@Param("floadids") List<String> floadids,@Param("foldareaverids") List<String> foldareaverids, @Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 汇总行业应收金额
     * 方法功能:  汇总行业应收金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/2/18
     * */
    AdSummaryVO summaryIndustyReceivable(@Param("industyId") String industyId,@Param("mediaId") String mediaId,@Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 查询订单变动记录
     * 方法功能:  查询订单变动记录
     *
     * @return List<AdChangeVo>
     * @author lhl
     * @date 2024/2/20
     */

    List<AdChangeVo> queryChangeOredrItem(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 获取订单变动记录数
     * 方法功能:  获取订单变动记录数
     *
     * @return List<AdChangeVo>
     * @author lhl
     * @date 2024/2/20
     */
    Long getChangeOredrItemCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 综合查询
     * 方法功能:  综合查询
     *
     * @return List<SynQueryResultVO>
     * @author lhl
     * @date 2024/2/20
     */
    List<SynQueryResultVO> querySyn(@Param("fields") List<SynQueryItem> fields,@Param("range")String range,@Param("sortname")String sortname,@Param("sorttype")String sorttype,@Param("repeat")String repeat,@Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 综合查询记录数
     * 方法功能:  综合查询记录数
     *
     * @return Long
     * @author lhl
     * @date 2024/2/28
     */
    Long getQuerySynCount(@Param("fields") List<SynQueryItem> fields,@Param("range")String range,@Param("repeat")String repeat);

    /**
     * 合同订单明细应收金额、已收金额、欠款金额汇总
     * 方法功能:  合同订单明细应收金额、已收金额、欠款金额汇总
     *
     * @return Long
     * @author lhl
     * @date 2024/03/06
     */
    AdSummaryAllVO getAdSummaryAll(@Param("orderId")String orderId);

}
