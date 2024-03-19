package com.hgzp.advertising.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.service.finance.TbpaymethodServiceI;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.DataCombo;
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
 * 创建人：wangwk
 * 类描述：付款方式
 * 创建日期：2023/8/16 15:33
 * @folder finance/PaymethodController
 */
@RequestMapping(value = "/finance/paymethod")
@RestController
public class PaymethodController extends BaseController {

    @Autowired
    TbpaymethodServiceI tbpaymethodService;


    /**
     * 付款方式下拉框
     * 方法功能:  付款方式下拉框
     * @author wangwk
     * @date 2023/10/31 14:43
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @RequestMapping(value = "/getPaymethodCombo")
    public Json<List<DataCombo>> getPaymethodCombo(){
        List<DataCombo> paymethodCombo = tbpaymethodService.getPaymethodCombo();
        return Json.success(paymethodCombo);
    }


}
