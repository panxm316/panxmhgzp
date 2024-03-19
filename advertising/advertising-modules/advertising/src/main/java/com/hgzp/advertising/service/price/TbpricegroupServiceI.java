package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.vo.PriceGroupVO;
import com.hgzp.core.model.Tbpricegroup;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 价格表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
public interface TbpricegroupServiceI extends IMyService<Tbpricegroup> {
    /**
     * 方法功能:  根据查询条件获取价格组分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbpricegroup>
     * @author suny
     * @date 2023/11/11 10:05
     */
    Page<Tbpricegroup> getPriceGroupPageList(Page<Tbpricegroup> page, PriceGroupVO query) throws Exception;

    /**
     * 方法功能:  获取价格组树形结构数据
     *
     * @param query
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/11/10 13:35
     */
    List<TreeModel> getPriceGroupTree(TreeQuery query) throws Exception;

    /**
     * 方法功能: 新增保存价格组信息
     *
     * @param tbpricegroup
     * @return void
     * @author suny
     * @date 2023/11/10 13:35
     */
    void savePriceGroup(Tbpricegroup tbpricegroup) throws Exception;

    /**
     * 方法功能: 修改保存价格组信息
     *
     * @param tbpricegroup
     * @return void
     * @author suny
     * @date 2023/11/10 13:35
     */
    void updatePriceGroup(Tbpricegroup tbpricegroup) throws Exception;

    /**
     * 方法功能:  删除价格组信息
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/11/10 13:35
     */
    void deletePriceGroup(String ids);

    /**
     * 方法功能: 获取叠次最大isort序号
     *
     * @param
     * @return java.lang.Integer
     * @author suny
     * @date 2023/11/10 14:11
     */
    Integer getMaxSort();

    /**
     * 方法功能: 查询指定媒体类型下默认的价格组id
     *
     * @param mediatypekey
     * @return java.util.List<java.lang.Long>
     * @author suny
     * @date 2023/12/4 12:56
     */
    List<Long> getQueryGroupId(String mediatypekey);
    /**
     * 获取价格组列表
     * 方法功能:
     * @author hgsongly
     * @date 2023/12/5 17:07
     * @param query
     * @return java.util.List<com.hgzp.core.model.Tbpricegroup>
     */
    List<Tbpricegroup> getPriceGroupList(PriceGroupVO query) throws Exception;
}
