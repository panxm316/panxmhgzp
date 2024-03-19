package com.hgzp.advertisingsys.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

/**
 * <p>
 * 付款方式 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbpaymethodServiceI extends IMyService<Tbpaymethod> {

    /**
     * getPaymethodPageList
     * 方法功能:付款方式分页查询
     *
     * @param page
     * @param querykey
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpaymethod>
     * @author CGD
     * @date 2023/8/18 13:52
     */
    IPage<Tbpaymethod> getPaymethodPageList(Page<Tbpaymethod> page, String querykey);

    /**
     * deletePaymethod
     * 方法功能: 付款方式删除，支持","间隔多选
     *
     * @param ids
     * @return void
     * @author CGD
     * @date 2023/8/18 13:52
     */
    void deletePaymethod(String ids);

    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     *
     * @return Json
     * @author songly
     * @date 2023/9/15 10:34
     */
    Json doDefaultLogic(Tbpaymethod tbpaymethod);

    /**
     * 判重名
     * 方法功能:
     *
     * @param tbpaymethod
     * @return boolean
     * @author songly
     * @date 2024/2/27 9:48
     */
    boolean isDuplicateSname(Tbpaymethod tbpaymethod);
}
