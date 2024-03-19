package com.hgzp.advertising.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountVO;
import com.hgzp.advertising.service.finance.TwbankaccountsServiceI;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TwbankaccountsController
 * 创建人：suny
 * 类描述：银行流水单 前端控制器
 * 创建日期：2023/10/23 15:57
 *
 * @测试：
 * @folder finance/TwbankaccountsController
 */
@RestController
@RequestMapping("/finance/twbankaccounts")
public class TwbankaccountsController extends BaseController {
    @Autowired
    TwbankaccountsServiceI twbankaccountsServiceI;

    /**
     * 上传文件导入银行流水单
     * 方法功能: 上传文件导入银行流水单
     *
     * @param request
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/26 16:05
     */
    @PostMapping("/importBankAccount")
    public Json importBankAccount(HttpServletRequest request) throws Exception {
        FileInfo upload = WebUploadUtil.upload(request);
        twbankaccountsServiceI.importBankAccount(upload);
        return Json.success();
    }

    /**
     * 银行流水分页列表
     * 方法功能: 银行流水分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twbankaccounts>>
     * @author suny
     * @date 2023/10/26 16:33
     */
    @GetMapping("/getBankAccountPageList")
    public Json<List<Twbankaccounts>> getBankAccountPageList(PageRequest pageRequest, BankAccountVO query) throws Exception {
        Page<Twbankaccounts> page = getPage(pageRequest);
        IPage<Twbankaccounts> pageList = twbankaccountsServiceI.getBankAccountPageList(page, query);
        return Json.success(pageList);
    }
}
