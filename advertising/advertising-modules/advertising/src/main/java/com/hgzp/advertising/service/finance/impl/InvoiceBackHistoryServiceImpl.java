package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.finance.InvoiceBackHistoryServiceI;
import com.hgzp.core.model.Twinvoicebackhistory;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.finance.TwinvoicebackhistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票(收款)回退历史表 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
@Service
public class InvoiceBackHistoryServiceImpl extends ServiceImpl<TwinvoicebackhistoryMapper, Twinvoicebackhistory> implements InvoiceBackHistoryServiceI {
    @Autowired
    private TworderitemServiceI orderitemServiceI;

    /**
     * 查询回退历史列表
     * 方法功能:参数：日期范围、合同号、发票号（这两个号都用queryKey传递）
     *
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO>
     * @author yanz
     * @date 2023/12/27 13:16
     */
    @Override
    public IPage<InvoiceBackHistoryDTO> getInvoiceBackHistoryPageList(Page<Twinvoicebackhistory> page, BaseQueryInfo query) {
        LambdaQueryWrapper<Twinvoicebackhistory> lqw = Wrappers.lambdaQuery();
        // 关键字查询 - 合同号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.and(i -> i.like(Twinvoicebackhistory::getScontractnum, query.getQueryKey()).or().like(Twinvoicebackhistory::getInvoice, query.getQueryKey()));
        }
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twinvoicebackhistory::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twinvoicebackhistory::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        Page<Twinvoicebackhistory> paged = this.page(page, lqw);
        Page<InvoiceBackHistoryDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }

        List<Long> orderitemIds = paged.getRecords().stream()
                .map(Twinvoicebackhistory::getOrderitemid)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderitemIds)) {
            return dtoPage;
        }
        Map<Long, Tworderitem> tworderitemMap = orderitemServiceI.listByIds(orderitemIds)
                .stream()
                .collect(Collectors.toMap(Tworderitem::getId, Function.identity()));
        List<InvoiceBackHistoryDTO> results = paged.getRecords().stream()
                .map(history -> {
                    Tworderitem tworderitem = tworderitemMap.get(history.getOrderitemid());
                    return InvoiceBackHistoryDTO.parseToDTO(history, tworderitem);
                })
                .sorted((o1, o2) -> o2.getDcreatetime().compareTo(o1.getDcreatetime()))
                .collect(Collectors.toList());
        dtoPage.setRecords(results);
        dtoPage.setTotal(paged.getTotal());
        return dtoPage;
    }
}
