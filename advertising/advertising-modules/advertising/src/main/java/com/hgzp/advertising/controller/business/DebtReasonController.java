package com.hgzp.advertising.controller.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.business.dto.DebtReasonDTO;
import com.hgzp.advertising.pagemodel.business.vo.DebtReasonVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.TwdebtreasonServiceI;
import com.hgzp.core.model.Twdebtreason;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DebtReasonController
 * 创建人：suny
 * 类描述：欠款统计、原因表 前端控制器
 * 创建日期：2023-11-17 09:52:42
 *
 * @folder business/DebtReasonController
 */
@RestController
@RequestMapping("/business/debtreason")
public class DebtReasonController extends BaseController {
    @Autowired
    TworderitemServiceI tworderitemServiceI;
    @Autowired
    TwdebtreasonServiceI twdebtreasonServiceI;

    /**
     * 查询统计欠款
     * 方法功能: 查询统计欠款
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO>>
     * @author suny
     * @date 2023/11/22 8:49
     */
    @GetMapping("/getOrderDebtPageList")
    public Json<List<OrderDebtDTO>> getOrderDebtPageList(PageRequest pageRequest, DebtReasonVO query) throws Exception {
        Page<Tworderitem> page = getPage(pageRequest);
        IPage<OrderDebtDTO> pageList = tworderitemServiceI.getOrderDebtPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 推送欠款信息到欠款统计表中
     * 方法功能: 推送欠款信息到欠款统计表中，同时更新订单刊期表中的欠款状态为已推送欠款统计表
     * (此方法已不用，用户要求不再主动推送，改为业务员主动直接查询，填写原因后自动推送)
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/22 8:50
     */
    @PostMapping(value = "/pushOrderDebt")
    public Json pushOrderDebt(@NotNull(message = "ID不可为空") String ids) throws Exception {
        tworderitemServiceI.pushOrderDebt(ids);
        return Json.success();
    }

    /**
     * 根据查询条件获取欠款原因分页列表
     * 方法功能: 根据查询条件获取欠款原因分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twdebtreason>>
     * @author suny
     * @date 2023/11/22 9:52
     */
    @GetMapping("/getDebtReasonPageList")
    public Json<List<Twdebtreason>> getDebtReasonPageList(PageRequest pageRequest, DebtReasonVO query) throws Exception {
        Page<Twdebtreason> page = getPage(pageRequest);
        IPage<Twdebtreason> pageList = twdebtreasonServiceI.getDebtReasonPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 提交欠款原因表信息
     * 方法功能: 提交欠款原因信息
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/22 9:58
     */
    @PostMapping(value = "/updateDebtReason")
    public Json updateDebtReason(@RequestBody DebtReasonDTO entity) throws Exception {
        twdebtreasonServiceI.updateDebtReason(entity);
        return Json.success();
    }

}
