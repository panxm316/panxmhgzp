package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.core.model.Tbdiscountgroup;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 折扣总表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */
public interface TbdiscountgroupServiceI extends IMyService<Tbdiscountgroup> {
    /**
     * 获取折扣总表分页列表
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:07
     * @param page
     * @param queryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbdiscountgroup>
     */
    IPage<Tbdiscountgroup> getDiscountGroupPageList(IPage<Tbdiscountgroup> page, DiscountItemVo queryInfo);
    /**
     * 新增折扣总表信息
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:08
     * @param tbdiscountgroup
     * @return boolean
     */
    void saveDiscountGroup(Tbdiscountgroup tbdiscountgroup) throws  Exception;
    /**
     * 更新折扣总表信息
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:09
     * @param tbdiscountgroup
     * @return boolean
     */
    void updateDiscountGroup(Tbdiscountgroup tbdiscountgroup)throws  Exception;
    /**
     * 删除折扣总表信息
     * 方法功能:
     * @author songly
     * @date 2023/11/22 13:09
     * @param ids
     * @return boolean
     */
    void deleteDiscountGroup(String ids) throws  Exception;
    /**
     * 获取总表折扣下拉列表
     * 方法功能: 获取总表折扣下拉列表
     * @author songly
     * @date 2023/11/23 10:27
     * @param sYear
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<Tbdiscountgroup> getDiscountGroupList(String sYear) ;
    /**
     * 获取媒体、行业等折扣
     * 方法功能: 获取媒体、行业等折扣
     * @param discountItemVo
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    String getNdiscountByYear(DiscountItemVo discountItemVo) throws Exception;
}
