package com.hgzp.advertisingsys.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.PricePackageTypeVO;
import com.hgzp.core.model.Tbpricepackagetype;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 打包类型表	 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbpricepackagetypeServiceI extends IMyService<Tbpricepackagetype> {

    /**
     * getPricePackageTypePageList
     * 方法功能:分页：获取打包类型
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpricepackagetype>
     * @author yanz
     * @date 2023/8/31 15:11
     */
    IPage<Tbpricepackagetype> getPricePackageTypePageList(Page<Tbpricepackagetype> page, PricePackageTypeVO vo);

    /**
     * deletePricePackageTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:21
     */
    boolean deletePricePackageTypeByIds(List<String> idList);

    /**
     * isDuplicateSname
     * 方法功能:重名判断
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2024/2/27 14:19
     */
    boolean isDuplicateSname(Long id, String sname);
}
