package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO;
import com.hgzp.core.model.Twinvoicebackhistory;
import com.hgzp.core.page.BaseQueryInfo;

import java.util.List;

/**
 * <p>
 * 发票(收款)回退历史表 服务类
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
public interface InvoiceBackHistoryServiceI extends IService<Twinvoicebackhistory> {
    /**
     * 查询回退历史列表
     * 方法功能:参数：日期范围、合同号、发票号（这两个号都用queryKey传递）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO>
     * @author yanz
     * @date 2023/12/27 13:16
     */
    IPage<InvoiceBackHistoryDTO> getInvoiceBackHistoryPageList(Page<Twinvoicebackhistory> page, BaseQueryInfo query);
}
