package com.hgzp.advertising.service.customer;

import com.hgzp.core.model.Tbcustomerstatus;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 客户状态表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
public interface TbcustomerstatusServiceI extends IMyService<Tbcustomerstatus> {
    /**
     * 获取客户状态列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbcustomerstatus>
     * @author songly
     * @date 2023/11/24 12:57
     */
    List<Tbcustomerstatus> getCustomerStatusList();

    /**
     * 获取客户状态下拉列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/11/24 12:58
     */
    List<DataCombo> getCustomerStatusCombo();

    /**
     * 判重复
     * 方法功能:
     *
     * @param tbcustomerstatus
     * @return boolean
     * @author songly
     * @date 2023/11/24 12:58
     */
    boolean isDuplicateSname(Tbcustomerstatus tbcustomerstatus);
}
