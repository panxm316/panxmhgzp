package com.hgzp.mapper.ad;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.pagemodel.ad.AdOrderItemCostVO;
import com.hgzp.pagemodel.ad.AdSummaryVO;
import com.hgzp.pagemodel.business.OrderAndItemDTO;
import com.hgzp.pagemodel.business.TaskCountVo;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.schedule.PageOrderItemVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单刊期表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @author lhl
 * @since 2023-11-15
 */
public interface TworderitemMapper extends BaseMapper<Tworderitem> {
    /**
     * 获取刊发基于媒体、叠次、叠次版本期间范围内的广告刊发量
     * 方法功能:  获取刊发基于媒体、叠次、叠次版本期间范围内的广告刊发量
     *
     * @return long
     * @author lhl
     * @date 2024/1/20
     */
    long queryOredrPublishCount(@Param("mediaId") Long mediaId, @Param("foldId") String foldId, @Param("areaverId") String areaverId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("publishstatus") String publishstatus);

    /**
     * 获取刊发基于媒体、叠次、叠次版本期间范围内的广告刊发记录列表
     * 方法功能:  获取刊发基于媒体、叠次、叠次版本期间范围内的广告刊发记录列表
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<OrderPublishQueryResultVo> queryOredrPublishList(@Param("mediaId") Long mediaId, @Param("foldId") String foldId, @Param("areaverId") String areaverId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("publishstatus") String publishstatus);

    /**
     * 获取刊发状态为1、2的广告刊发记录数量
     * 方法功能:  获取刊发状态为1、2的广告刊发记录数量
     *
     * @return long
     * @author lhl
     * @date 2024/1/20
     */
    long queryCheckOredrListCount(@Param("mediaId") Long mediaId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取刊发状态为1、2的广告刊发记录列表
     * 方法功能:  获取刊发状态为1、2的广告刊发记录列表
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<OrderPublishQueryResultVo> queryCheckOredrList(@Param("mediaId") Long mediaId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取部门任务汇总
     * 方法功能:  获取部门任务汇总
     *
     * @return List<TaskCountVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<TaskCountVo> queryDepartmentTaskCountList(@Param("mediaId") String mediaId, @Param("ids") List<Long> ids, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("publishstatus") String publishstatus);

    /**
     * 获取人员任务汇总
     * 方法功能:  获取人员任务汇总
     *
     * @return List<TaskCountVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<TaskCountVo> queryEmployeTaskCountList(@Param("mediaId") String mediaId, @Param("ids") List<Long> ids, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("publishstatus") String publishstatus);

    /**
     * 汇总期间订单金额(含刊发状态查询条件)
     * 方法功能:  汇总期间订单金额(含刊发状态查询条件)
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     */
    AdSummaryVO summaryReceivable(@Param("adprojectId") String adprojectId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType);

    /**
     * 汇总期间订单金额(不含刊发状态查询条件)
     * 方法功能:  汇总期间订单金额(不含刊发状态查询条件)
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     */
    AdSummaryVO summaryHistoryReceivable(@Param("adprojectId") String adprojectId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType);

    /**
     * 汇总期间订单费用
     * 方法功能:  汇总期间订单费用
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     */

    AdSummaryVO summaryOrderCost(@Param("adprojectId") String adprojectId);

    /**
     * 汇总期间订单金额
     * 方法功能:  汇总期间订单金额
     *
     * @return AdSummaryVO
     * @author lhl
     * @date 2024/1/20
     */
    AdSummaryVO summaryReceivableHistory(@Param("adprojectId") String adprojectId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime);

    /**
     * 汇总项目订单明细
     * 方法功能:  汇总项目订单明细
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */

    List<OrderPublishQueryResultVo> queryAdProjectOredrDetails(@Param("projectId") Long projectId, @Param("mediaId") Long mediaId);

    /**
     * 汇总项目期间完成情况
     * 方法功能:  汇总项目期间完成情况
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<OrderPublishQueryResultVo> queryAdProjectOredrDetailsWithTime(@Param("projectId") Long projectId, @Param("mediaId") String mediaId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 汇总项目历史完成情况
     * 方法功能:  汇总项目历史完成情况
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<OrderPublishQueryResultVo> queryHistoryAdProjectOredrDetailsWithTime(@Param("projectId") Long projectId, @Param("mediaId") String mediaId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取项目期间完成情况记录数量
     * 方法功能:  获取项目期间完成情况记录数量
     *
     * @return long
     * @author lhl
     * @date 2024/1/20
     */
    long queryAdProjectOredrDetailsWithTimeCount(@Param("projectId") Long projectId, @Param("mediaId") String mediaId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取项目历史完成情况记录数量
     * 方法功能:  获取项目历史完成情况记录数量
     *
     * @return long
     * @author lhl
     * @date 2024/1/20
     */
    long queryHistoryAdProjectOredrDetailsWithTimeCount(@Param("projectId") Long projectId, @Param("mediaId") String mediaId, @Param("lastStartTime") String lastStartTime, @Param("lastEndTime") String lastEndTime, @Param("thisStartTime") String thisStartTime, @Param("thisEndTime") String thisEndTime, @Param("summaryType") String summaryType, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取项目订单明细列表
     * 方法功能:  获取项目订单明细列表
     *
     * @return List<AdOrderItemCostVO>
     * @author lhl
     * @date 2024/1/20
     */
    List<AdOrderItemCostVO> queryOrderItemCostList(@Param("projectId") Long projectId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取项目订单明细记录数量
     * 方法功能:  获取项目订单明细记录数量
     *
     * @return long
     * @author lhl
     * @date 2024/1/20
     */
    List<Long> queryOrderItemCostCount(@Param("projectId") Long projectId);

    /**
     * 获取版面预定广告列表
     * 方法功能:  获取版面预定广告列表
     *
     * @return List<PageOrderItemVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<PageOrderItemVo> queryPageOrderItemList(@Param("pageId") Long pageId);

    /**
     * 获取媒体预定广告列表
     * 方法功能:  获取媒体版面预定广告列表
     *
     * @return List<PageOrderItemVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<PageOrderItemVo> queryNewMediaOrderItemList(@Param("mediaId") String mediaId, @Param("foldId") String foldId, @Param("areaverId") String areaverId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("formId") String formId);

    /**
     * 获取已安排广告列表
     * 方法功能:  获取已安排广告列表
     *
     * @return List<OrderPublishQueryResultVo>
     * @author lhl
     * @date 2024/1/20
     */
    List<OrderPublishQueryResultVo> queryOredrPredetermineList(@Param("mediaId") Long mediaId, @Param("foldId") String foldId, @Param("areaverId") String areaverId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询人员/部门业绩总和
     * 方法功能: 查询人员、部门业绩总和
     *
     * @param startTime
     * @param endTime
     * @param mediaid
     * @param deptidList
     * @param employid
     * @return java.math.BigDecimal
     * @author suny
     * @date 2024/1/23 16:41
     */
    Tworderitem getSumAchievement(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                                  @Param("deptidList") List<Long> deptidList, @Param("employid") Long employid, @Param("customerid") Long customerid);

    /**
     * 查询客户业绩总和
     * 方法功能: 查询客户业绩总和
     *
     * @param startTime
     * @param endTime
     * @param mediaid
     * @param customerid
     * @return com.hgzp.core.model.Tworderitem
     * @author suny
     * @date 2024/2/22 9:07
     */
    Tworderitem getSumCustomerAchievement(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                                          @Param("customerid") Long customerid);

    /**
     * 查询指定客户的订单明细
     * 方法功能:  查询指定客户的订单明细
     *
     * @param startTime
     * @param endTime
     * @param mediaid
     * @param customerid
     * @return java.util.List<com.hgzp.pagemodel.business.OrderAndItemDTO>
     * @author suny
     * @date 2024/1/24 15:20
     */
    List<OrderAndItemDTO> selectOrderAndItemList(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                                                 @Param("customerid") Long customerid, @Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 查询指定客户的订单明细总数
     * 方法功能: 查询指定客户的订单明细总数
     *
     * @param startTime
     * @param endTime
     * @param mediaid
     * @param customerid
     * @return java.lang.Long
     * @author suny
     * @date 2024/1/24 15:45
     */
    Long getOrderAndItemCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                              @Param("customerid") Long customerid);

    /**
     * 我的订单(明细)汇总
     * 方法功能:我的订单(明细)汇总
     *
     * @param startTime
     * @param endTime
     * @param deptidList
     * @param employid
     * @param queryKey
     * @param barrearsflag
     * @param publishStatus
     * @return com.hgzp.pagemodel.business.OrderAndItemDTO
     * @author yanz
     * @date 2024/3/6 15:05
     */
    OrderAndItemDTO getSumMyOrderItem(@Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime,
                                      @Param("deptidList") List<Long> deptidList,
                                      @Param("employid") Long employid,
                                      @Param("queryKey") String queryKey,
                                      @Param("barrearsflag") Boolean barrearsflag,
                                      @Param("publishStatus") Integer publishStatus);
}
