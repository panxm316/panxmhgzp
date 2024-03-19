package com.hgzp.advertisingsys.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.finance.TbpaymethodServiceI;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * PaymethodController
 * 类全名：com.hgzp.advertising.controller.finance.PaymethodController
 * 创建人：CGD
 * 类描述：付款方式
 * 创建日期：2023/8/16 15:33
 * @folder finance/PaymethodController
 */
@Validated
@RequestMapping(value = "/finance/paymethod")
@RestController
public class PaymethodController extends BaseController {

    @Autowired
    private TbpaymethodServiceI tbpaymethodService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 付款方式分页查询
     * 方法功能:  付款方式分页查询
     *
     * @param pageRequeste
     * @param queryKey
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpaymethod>>
     * @author CGD
     * @date 2023/8/18 13:53
     */
    @GetMapping(value = "/getPaymethodPageList")
    public Json<List<Tbpaymethod>> getPaymethodPageList(PageRequest pageRequeste, String queryKey) {
        Page page = getPage(pageRequeste);
        IPage<Tbpaymethod> tbpaymethods = tbpaymethodService.getPaymethodPageList(page, queryKey);
        return Json.success(tbpaymethods);
    }

    /**
     * 付款方式新增保存
     * 方法功能: 付款方式新增保存
     *
     * @param paymethod
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 13:51
     */
    @PostMapping(value = "/savePaymethod")
    public Json savePaymethod(@RequestBody Tbpaymethod paymethod) {
        if(tbpaymethodService.isDuplicateSname(paymethod)){
            return Json.fail("付款方式名称不能重复!");
        }
        Json jsonRet=tbpaymethodService.doDefaultLogic(paymethod);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        tbpaymethodService.save(paymethod);
        return Json.success();
    }

    /**
     * 付款方式修改
     * 方法功能: 付款方式修改
     *
     * @param paymethod
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 13:51
     */
    @PostMapping(value = "/updatePaymethod")
    public Json updatePaymethod(@RequestBody Tbpaymethod paymethod) {
        if(tbpaymethodService.isDuplicateSname(paymethod)){
            return Json.fail("付款方式名称不能重复!");
        }
        Json jsonRet=tbpaymethodService.doDefaultLogic(paymethod);
        if(!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        innerInterceptor.recoredLog();
        tbpaymethodService.updateById(paymethod);
        return Json.success();
    }

    /**
     * 付款方式按id查询信息
     * 方法功能: 付款方式按id查询信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbpaymethod>
     * @author CGD
     * @date 2023/8/18 13:51
     */
    @GetMapping(value = "/getPaymethodById")
    public Json<Tbpaymethod> getPaymethodById(@NotNull(message = "请传入需要查询的id") Long id) {
        Tbpaymethod byId = tbpaymethodService.getById(id);
        return Json.success(byId);
    }

    /**
     * 付款方式删除
     * 方法功能: 付款方式删除，支持","间隔多选
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 13:51
     */
    @PostMapping(value = "/deletePaymethod")
    public Json deletePaymethod(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        tbpaymethodService.removeByIds(Arrays.asList(ids.split(",")));
        return Json.success();
    }

    /**
     * 获取付款方式序号最大值
     * 方法功能: 获取付款方式序号最大值
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 14:36
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = tbpaymethodService.getMaxSort();
        return Json.success(maxSort);

    }
}
