package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.PriceItemDTO;
import com.hgzp.advertising.pagemodel.price.vo.PriceItemVO;
import com.hgzp.core.model.Tbpriceitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 价格明细表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
public interface TbpriceitemServiceI extends IMyService<Tbpriceitem> {


    Page<PriceItemVO> getPriceItemPageList(Page<Tbpriceitem> page, PriceItemVO query);

    /**
     * 保存价格明细
     * 方法功能:保存价格明细
     *
     * @param priceItemDTO
     * @return void
     * @author CGD
     * @date 2023/11/13 11:22
     */
    void savePriceItem(PriceItemDTO priceItemDTO);

    /**
     * 修改价格明细
     * 方法功能: TODO 用过后就不可修改条件和价格
     *
     * @param priceItemDTO
     * @return void
     * @author CGD
     * @date 2023/11/13 11:23
     */
    void updatePriceItem(PriceItemDTO priceItemDTO);

    /**
     * 删除价格明细
     * 方法功能:    TODO 用过后就不可删除
     *
     * @param ids
     * @return void
     * @author CGD
     * @date 2023/11/16 13:45
     */
    void deletePriceItem(String ids);

    /**
     * 方法功能: 根据价格组id批量复制价格明细
     *
     * @param oldpricegroupid
     * @param newpricegroupid
     * @param queryInfo
     * @return void
     * @author suny
     * @date 2023/11/24 16:55
     */
    void SaveCopyPriceItem(Long oldpricegroupid, Long newpricegroupid, BaseQueryInfo queryInfo);

    /**
     * 方法功能:  保存批量修改价格明细
     *
     * @param ids
     * @param queryInfo
     * @return void
     * @author suny
     * @date 2023/11/21 9:03
     */
    void batchUpdatePriceItem(String ids, BaseQueryInfo queryInfo);
    List<Tbpriceitem> getPriceItemList(PriceItemVO query) ;
}
