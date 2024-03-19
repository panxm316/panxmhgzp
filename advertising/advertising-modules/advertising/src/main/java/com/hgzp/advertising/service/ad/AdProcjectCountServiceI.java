package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectContractVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectCountOrderDetailsVO;
import com.hgzp.advertising.pagemodel.ad.vo.TworderitemVO;
import com.hgzp.core.model.Twadproject;
import com.hgzp.pagemodel.ad.AdOrderItemCostVO;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.pagemodel.business.TaskCountVo;

import java.util.List;

/**
 * <p>
 * 项目汇总统计 服务类
 * </p>
 *
 * @author lhl
 * @since 2024-01-05
 */
public interface AdProcjectCountServiceI {
    /**
     * 广告项目汇总
     * 方法功能: 广告项目汇总
     *
     * @param stratTime,endTime，adProjectId，pageNum，pageSize，publishstatus
     * @return TaskCountVo
     * @author lhl
     * @date 2024/01/05
     */
    IPage<AdProjectCountVO> getAdProjectCountList(String stratTime, String endTime, String adProjectId,int pageNum, int pageSize, String publishstatus,String queryKey,String projectEnd);

    /**
     * 获取广告项目列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Twadproject>
     * @author lhl
     * @date 2024/01/06
     */
    List<Twadproject> getAdProjectList();

    /**
     * 获取广告项目订单明细
     * 方法功能:
     *
     * @param adProjectId
     * @return AdProjectCountOrderDetailsVO
     * @author lhl
     * @date 2024/01/08
     */
    AdProjectCountOrderDetailsVO getAdProjectOrderDetails(String adProjectId,String mediaId,int pageNum, int pageSize,String detailtype);

    /**
     * 获取广告项目成本明细
     * 方法功能:
     *
     * @param
     * @return AdOrderItemCostVO
     * @author lhl
     * @date 2024/01/10
     */
    IPage<AdOrderItemCostVO> getAdProjectCostDetails(String adProjectId, int pageNum, int pageSize);

    /**
     * 获取广告项目相关合同
     * 方法功能: 获取广告项目相关合同
     *
     * @param
     * @return AdProjectContractVO
     * @author lhl
     * @date 2024/03/06
     */
    IPage<AdProjectContractVO> getAdProjectContract(String adProjectId, int projectType,int pageNum, int pageSize);

    /**
     * 获取订单明细
     * 方法功能: 获取订单明细
     *
     * @param
     * @return AdProjectContractVO
     * @author lhl
     * @date 2024/03/06
     */
    IPage<TworderitemVO> getAdOrderItem(String adOrderId, int pageNum, int pageSize);

}
