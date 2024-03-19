package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.price.vo.DiscountReduceVO;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.model.Tbdiscountreduce;

/**
 * <p>
 * 折扣下点 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
public interface TbdiscountreduceServiceI extends IService<Tbdiscountreduce> {
    /**
     * 方法功能: 根据条件查询折扣下点分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbdiscountreduce>
     * @author suny
     * @date 2023/11/30 14:59
     */
    Page<Tbdiscountreduce> getDiscountReducePageList(Page<Tbdiscountreduce> page, DiscountReduceVO query) throws Exception;

    /**
     * 方法功能: 新增保存折扣下点
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/30 14:59
     */
    void saveDiscountReduce(Tbdiscountreduce entity) throws Exception;

    /**
     * 方法功能: 修改保存折扣下点
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/30 15:00
     */
    void updateDiscountReduce(Tbdiscountreduce entity) throws Exception;

    /**
     * 方法功能:  删除折扣下点数据
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/30 15:00
     */
    void deleteDiscountReduce(String ids);

    /**
     * 方法功能:  根据价格组id复制折扣下点
     *
     * @param oldpricegroupid
     * @param tbcommissionrategroup
     * @return void
     * @author suny
     * @date 2023/12/1 9:39
     */
    void SaveCopyDiscountReduce(Long oldpricegroupid, Tbcommissionrategroup tbcommissionrategroup);
}
