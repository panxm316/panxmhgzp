package com.hgzp.advertising.controller.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectContractVO;
import com.hgzp.advertising.pagemodel.ad.vo.AdProjectCountOrderDetailsVO;
import com.hgzp.advertising.pagemodel.ad.vo.TworderitemVO;
import com.hgzp.advertising.service.ad.AdProcjectCountServiceI;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.ad.AdOrderItemCostVO;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.pagemodel.business.TaskCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * AdProjectCountController
 * 创建人：lhl
 * 类描述：广告项目汇总统计 前端控制器
 * 创建日期：2024/1/3 12:55
 *
 * @folder ad/AdProjectCountController
 */
@RestController
@RequestMapping("/ad/adprojectcount")

public class AdProjectCountController extends BaseController {
    @Autowired
    AdProcjectCountServiceI  adProcjectCountServiceI;
    /**
     * 广告项目汇总
     * 方法功能: 广告项目汇总
     *
     * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd
     * @return TaskCountVo
     * @author lhl
     * @date 2023/12/27
     */
    @GetMapping("/getAdProjectCountList")
    public Json<List<AdProjectCountVO>> getAdProjectCountList(String stratTime, String endTime, String adProjectId,int pageNum, int pageSize,String publistStatus,String queryKey,String projectEnd) {
        IPage<AdProjectCountVO> list = adProcjectCountServiceI.getAdProjectCountList(stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd);
        return Json.success(list);
    }

    /**
     * 获取广告项目列表
     * 方法功能: 获取广告项目列表
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twadproject>>
     * @author lhl
     * @date 2024/01/06
     */
    @GetMapping(value = "/getAdProjectList")
    public Json<List<Twadproject>> getAdProjectList() {
        List<Twadproject> adprojects = adProcjectCountServiceI.getAdProjectList();
        return Json.success(adprojects);
    }

    /**
     * 获取广告项目订单明细
     * 方法功能: 获取广告项目订单明细
     *
     * @param adProjectId,mediaId,pageNum,pageSize,detailtype
     * @return AdProjectCountOrderDetailsVO
     * @author lhl
     * @date 2024/01/08
     */
    @GetMapping(value = "/getAdProjectOrderDetails")
    public Json<AdProjectCountOrderDetailsVO> getAdProjectOrderDetails(String adProjectId,String mediaId,int pageNum, int pageSize,String detailtype) {
        return Json.success(adProcjectCountServiceI.getAdProjectOrderDetails(adProjectId,mediaId,pageNum,pageSize,detailtype));
    }

    /**
     * 获取广告项目成本明细
     * 方法功能: 获取广告项目成本明细
     *
     * @param adProjectId,mediaId,pageNum,pageSize,detailtype
     * @return AdProjectCountOrderDetailsVO
     * @author lhl
     * @date 2024/01/08
     */
    @GetMapping(value = "/getAdProjectCostDetails")
    public Json<List<AdOrderItemCostVO>> getAdProjectCostDetails(String adProjectId, int pageNum,int pageSize) {
        return Json.success(adProcjectCountServiceI.getAdProjectCostDetails(adProjectId,pageNum,pageSize));
    }

    /**
     * 获取广告项目相关合同
     * 方法功能: 获取广告项目相关合同
     *
     * @param
     * @return AdProjectContractVO
     * @author lhl
     * @date 2024/03/06
     */
    @GetMapping(value = "/getAdProjectContract")
    public Json<List<AdProjectContractVO>> getAdProjectContract(String adProjectId,int projectType, int pageNum, int pageSize) {
        return Json.success(adProcjectCountServiceI.getAdProjectContract(adProjectId,projectType,pageNum,pageSize));
    }

    /**
     * 获取订单明细
     * 方法功能: 获取订单明细
     *
     * @param
     * @return AdProjectContractVO
     * @author lhl
     * @date 2024/03/06
     */
    @GetMapping(value = "/getAdOrderItem")
    public Json<List<TworderitemVO>> getAdOrderItem(String adOrderId, int pageNum, int pageSize) {
        return Json.success(adProcjectCountServiceI.getAdOrderItem(adOrderId,pageNum,pageSize));
    }

}
