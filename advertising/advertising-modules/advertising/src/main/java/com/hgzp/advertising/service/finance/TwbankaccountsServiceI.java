package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountVO;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.utils.model.FileInfo;

/**
 * <p>
 * 银行流水单 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-26
 */
public interface TwbankaccountsServiceI extends IService<Twbankaccounts> {
    /**
     * importBankAccount
     * 方法功能: 导入银行流水单
     *
     * @param upfile
     * @return void
     * @author suny
     * @date 2023/10/26 16:08
     */
    void importBankAccount(FileInfo upfile) throws Exception;

    /**
     * getHolidayPageList
     * 方法功能: 获取银行流水单分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twbankaccounts>
     * @author suny
     * @date 2023/10/26 16:08
     */
    IPage<Twbankaccounts> getBankAccountPageList(Page<Twbankaccounts> page, BankAccountVO query) throws Exception;

    /**
     * 方法功能: 获取未分配完金额的银行流水
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twbankaccounts>
     * @author suny
     * @date 2023/12/5 10:19
     */
    IPage<Twbankaccounts> getBalanceBankAccountPageList(Page<Twbankaccounts> page, BankAccountVO query) throws Exception;
}
