package com.hgzp.advertising.service.customer;

import com.hgzp.core.model.Tbcustomercategory;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 客户分类表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
public interface TbcustomercategoryServiceI extends IMyService<Tbcustomercategory> {
    /**
     * 获取客户分类列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbcustomercategory>
     * @author songly
     * @date 2023/11/24 12:57
     */
    List<Tbcustomercategory> getCustomerCategoryList();

    /**
     * 获取客户分类下拉列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/11/24 12:58
     */
    List<DataCombo> getCustomerCategoryCombo();

    /**
     * 判重复
     * 方法功能:
     *
     * @param tbcustomercategory
     * @return boolean
     * @author songly
     * @date 2023/11/24 12:58
     */
    boolean isDuplicateSname(Tbcustomercategory tbcustomercategory);
}
