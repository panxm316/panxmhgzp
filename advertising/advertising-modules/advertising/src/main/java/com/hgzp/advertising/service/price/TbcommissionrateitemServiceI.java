package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.price.vo.CommissionRateItemVO;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.model.Tbcommissionrateitem;

/**
 * <p>
 * 计提比例明细表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
public interface TbcommissionrateitemServiceI extends IService<Tbcommissionrateitem> {
    /**
     * 方法功能: 根据条件查询计提比例明细表分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbcommissionrateitem>
     * @author suny
     * @date 2023/11/25 9:53
     */
    Page<Tbcommissionrateitem> getCommissionRateItemPageList(Page<Tbcommissionrateitem> page, CommissionRateItemVO query) throws Exception;

    /**
     * 方法功能: 新增保存计提比例明细表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/25 9:54
     */
    void saveCommissionRateItem(Tbcommissionrateitem entity) throws Exception;

    /**
     * 方法功能: 修改保存计提比例明细表
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/25 9:54
     */
    void updateCommissionRateItem(Tbcommissionrateitem entity) throws Exception;

    /**
     * 方法功能: 删除计提比例明细表数据
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/25 9:54
     */
    void deleteCommissionRateItem(String ids);

    /**
     * 方法功能: 根据价格组id复制计提比例明细表
     *
     * @param oldpricegroupid
     * @param tbcommissionrategroup
     * @return void
     * @author suny
     * @date 2023/12/1 9:15
     */
    void SaveCopyCommissionRateItem(Long oldpricegroupid, Tbcommissionrategroup tbcommissionrategroup);
}
