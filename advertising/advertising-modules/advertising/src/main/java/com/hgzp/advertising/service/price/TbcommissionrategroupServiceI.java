package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.page.BaseQueryInfo;

/**
 * <p>
 * 计提比例总表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
public interface TbcommissionrategroupServiceI extends IService<Tbcommissionrategroup> {
    /**
     * 方法功能: 根据查询条件获取计提比例总表分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbcommissionrategroup>
     * @author suny
     * @date 2023/11/24 10:09
     */
    Page<Tbcommissionrategroup> getCommissionRateGroupPageList(Page<Tbcommissionrategroup> page, BaseQueryInfo query) throws Exception;

    /**
     * 方法功能: 新增保存计提比例总表信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/24 10:10
     */
    void saveCommissionRateGroup(Tbcommissionrategroup entity) throws Exception;

    /**
     * 方法功能: 更新保存计提比例总表信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/24 10:10
     */
    void updateCommissionRateGroup(Tbcommissionrategroup entity) throws Exception;

    /**
     * 方法功能: 删除计提比例总表信息
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/24 10:10
     */
    void deleteCommissionRateGroup(String ids);
}
