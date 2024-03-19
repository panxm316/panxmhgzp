package com.hgzp.advertising.service.customer;

import com.hgzp.core.model.Tbcustomercredit;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 客户信誉度表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
public interface TbcustomercreditServiceI extends IMyService<Tbcustomercredit> {
    /**
     * 获取客户信誉度列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbcustomercredit>
     * @author hgsongly
     * @date 2023/11/24 14:14
     */
    List<Tbcustomercredit> getCustomerCreditList();

    /**
     * 获取客户信誉度下拉列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/11/24 12:58
     */
    List<DataCombo> getCustomerCreditCombo();

    /**
     * 判重复
     * 方法功能:
     *
     * @param tbcustomercredit
     * @return boolean
     * @author songly
     * @date 2023/11/24 12:58
     */
    boolean isDuplicateSname(Tbcustomercredit tbcustomercredit);
}
