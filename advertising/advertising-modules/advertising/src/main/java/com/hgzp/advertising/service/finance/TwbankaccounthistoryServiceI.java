package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountHistoryVO;
import com.hgzp.core.model.Twbankaccounthistory;

/**
 * <p>
 * 银行流水导入历史表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-26
 */
public interface TwbankaccounthistoryServiceI extends IService<Twbankaccounthistory> {

    /**
     * 获取银行流水导入分页列表
     * 方法功能: 获取银行流水导入分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twbankaccounthistory>
     * @author suny
     * @date 2023/10/27 15:37
     */
    IPage<Twbankaccounthistory> getBankAccountHistoryPageList(Page<Twbankaccounthistory> page, BankAccountHistoryVO query) throws Exception;
}
