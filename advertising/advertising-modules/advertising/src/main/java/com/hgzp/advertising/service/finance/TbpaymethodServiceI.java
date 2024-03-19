package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Tbbusinessentity;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * TbpaymethodServiceI
 * 创建人：wangwk
 * 类描述：付款方式
 * 创建日期：2023/10/31 14:17
 */
public interface TbpaymethodServiceI extends IService<Tbpaymethod> {

    /**
     *  付款方式下拉
     * 方法功能:  付款方式下拉
     * @author wangwk
     * @date 2023/10/31 14:42
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<DataCombo> getPaymethodCombo();
}
