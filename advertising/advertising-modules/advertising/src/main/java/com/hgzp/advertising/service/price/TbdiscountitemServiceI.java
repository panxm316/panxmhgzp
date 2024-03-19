package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.core.model.Tbdiscountitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 折扣明细表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */
public interface TbdiscountitemServiceI extends IMyService<Tbdiscountitem> {
    /**
     * 获取折扣总表分页列表
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:07
     * @param page
     * @param queryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbdiscountitem>
     */
    IPage<Tbdiscountitem> getDiscountItemPageList(IPage<Tbdiscountitem> page, DiscountItemVo queryInfo);
    /**
     * 新增折扣总表信息
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:08
     * @param tbdiscountitem
     * @return boolean
     */
    void saveDiscountItem(Tbdiscountitem tbdiscountitem) throws  Exception;
    /**
     * 更新折扣总表信息
     * 方法功能:
     * @author hgsongly
     * @date 2023/11/22 13:09
     * @param tbdiscountitem
     * @return boolean
     */
    void updateDiscountItem(Tbdiscountitem tbdiscountitem)throws  Exception;
    /**
     * 删除折扣总表信息
     * 方法功能:
     * @author hgsongly
     * @date 2023/11/22 13:09
     * @param ids
     * @return boolean
     */
    void deleteDiscountItem(String ids) throws  Exception;
    /**
     * 获取媒体、行业等折扣下拉列表
     * 方法功能: 获取媒体、行业等折扣下拉列表
     * @author songly
     * @date 2023/11/23 10:27
     * @param discountItemVo
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<Tbdiscountitem> getDiscountItemList(DiscountItemVo discountItemVo) ;

}
