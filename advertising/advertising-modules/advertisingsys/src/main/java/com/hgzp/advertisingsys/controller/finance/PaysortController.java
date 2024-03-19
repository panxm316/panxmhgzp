package com.hgzp.advertisingsys.controller.finance;

import com.hgzp.core.model.Tbpaysort;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.advertisingsys.service.finance.TbpaysortServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PaysortController
 * 创建人：CGD
 * 类描述：付款时间设置
 * 创建日期：2023/8/18 15:54
 * @folder finance/PaysortController
 */

@RequestMapping(value = "/finance/paysort")
@RestController
public class PaysortController extends BaseController {
    @Autowired
    TbpaysortServiceI tbpaysortService;

    /**
     * 查询付款时间设置
     * 方法功能: 查询付款时间设置，skey（刊前付款 bepub 刊后付款 afpub）
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpaysort>>
     * @author CGD
     * @date 2023/8/18 16:07
     */
    @GetMapping(value = "/getPaysortList")
    public Json<List<Tbpaysort>> getPaysortList() {
        List<Tbpaysort> list = tbpaysortService.lambdaQuery().orderByAsc(Tbpaysort::getIsort).list();
        return Json.success(list);
    }

    /**
     * 添加保存付款时间信息
     * 方法功能:  添加保存付款时间信息
     *
     * @param paysort
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 16:08
     */
    @PostMapping(value = "/savePaysort")
    public Json savePaymethod(@RequestBody Tbpaysort paysort) {
        tbpaysortService.save(paysort);
        return Json.success();
    }


}