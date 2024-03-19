package com.hgzp.advertising.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.finance.dto.BankAccountHistoryDTO;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountHistoryVO;
import com.hgzp.advertising.service.finance.TwbankaccounthistoryServiceI;
import com.hgzp.core.model.Twbankaccounthistory;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.file.UfileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * TwbankaccounthistoryController
 * 创建人：suny
 * 类描述：银行流水导入历史表 前端控制器
 * 创建日期：2023/10/23 15:57
 *
 * @测试：
 * @folder finance/TwbankaccounthistoryController
 */
@RestController
@RequestMapping("/finance/twbankaccounthistory")
public class TwbankaccounthistoryController extends BaseController {
    @Value("${ufile.uExtURL}")
    private String uExtURL;
    @Autowired
    TwbankaccounthistoryServiceI twbankaccounthistoryServiceI;

    /**
     * 获取银行流水导入分页列表
     * 方法功能: 获取银行流水导入分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.BankAccountHistoryDTO>>
     * @author suny
     * @date 2023/10/31 17:13
     */
    @GetMapping("/getBankAccountHistoryPageList")
    public Json<List<BankAccountHistoryDTO>> getBankAccountHistoryPageList(PageRequest pageRequest, BankAccountHistoryVO query) throws Exception {
        Page<Twbankaccounthistory> page = getPage(pageRequest);
        IPage<Twbankaccounthistory> pageList = twbankaccounthistoryServiceI.getBankAccountHistoryPageList(page, query);

        Page<BankAccountHistoryDTO> reslutPage = new Page<>();
        List<BankAccountHistoryDTO> result = new ArrayList<>();
        for (Twbankaccounthistory record : pageList.getRecords()) {
            BankAccountHistoryDTO bankAccountHistoryDTO = new BankAccountHistoryDTO(record);
            String url = UfileUtil.getStaticUrl(record.getSfileid(), record.getSfileformat());
            bankAccountHistoryDTO.setDurl(url);
            result.add(bankAccountHistoryDTO);
        }
        reslutPage.setRecords(result);
        reslutPage.setTotal(pageList.getTotal());
        return Json.success(reslutPage);
    }
}
