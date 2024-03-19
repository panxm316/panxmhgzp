package com.hgzp.advertising.controller.finance;


import com.hgzp.advertising.service.finance.TbadprintitemServiceI;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * AdPrintItemController
 * 创建人：caogd
 * 类描述： 开票项目 Controller
 * 创建日期：2023/8/18 15:55
 *
 * @folder finance/AdPrintItemController
 */
@Validated
@RestController
@RequestMapping(value = "/finance/adprintitem")
public class AdPrintItemController extends BaseController {
    @Autowired
    private TbadprintitemServiceI tbadprintitemService;

    /**
     * 获取开票项目下拉
     * 方法功能:获取开票项目下拉
     *
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbadprintitem>>
     */
    @GetMapping("/getAdPrintItemCombo")
    public Json<List<Tbadprintitem>> getAdPrintItemCombo() {
        List<Tbadprintitem> list = tbadprintitemService
                .lambdaQuery()
                .eq(Tbadprintitem::getBuse, true)
                .orderByAsc(Tbadprintitem::getIsort).list();
        return Json.success(list);
    }
}
