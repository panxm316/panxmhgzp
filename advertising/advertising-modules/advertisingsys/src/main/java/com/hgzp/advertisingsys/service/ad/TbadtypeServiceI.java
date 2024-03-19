package com.hgzp.advertisingsys.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdTypeVO;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 广告类型 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbadtypeServiceI extends IMyService<Tbadtype> {

    /**
     * getAdTypePageList
     * 方法功能:分页：获取广告类型
     * @author yanz
     * @date 2023/8/31 15:12
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbadtype>
     */
    IPage<Tbadtype> getAdTypePageList(Page<Tbadtype> page, AdTypeVO vo);

    /**
     * isDuplicateSname
     * 方法功能:是否存在同名对象
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2023/8/25 9:49
     */
    boolean isDuplicateSname(Long id, String sname);

    /**
     * deleteAdTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:10
     */
    boolean deleteAdTypeByIds(List<String> idList);
    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     * @author songly
     * @date 2023/9/15 10:34
     * @return   Json
     */
    Json doDefaultLogic(Tbadtype tbadtype);
    /**
     * getAdTypeList
     * 方法功能: 获取广告类型列表
     * @author songly
     * @date 2023/10/20 15:07
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<DataCombo> getAdTypeCombo();
}
