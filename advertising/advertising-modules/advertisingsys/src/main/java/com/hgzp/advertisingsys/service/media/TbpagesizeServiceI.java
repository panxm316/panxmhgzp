package com.hgzp.advertisingsys.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.media.vo.PageSizeVO;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 报纸版心 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbpagesizeServiceI extends IMyService<Tbpagesize> {

    /**
     * getPageSizePageList
     * 方法功能:分页：获取报纸版心
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpagesize>
     * @author yanz
     * @date 2023/8/31 15:04
     */
    IPage<Tbpagesize> getPageSizePageList(Page<Tbpagesize> page, PageSizeVO vo);

    /**
     * deletePageSizeByIds
     * 方法功能:据id批量删除
     *
     * @param ids
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:52
     */
    void deletePageSizeByIds(String ids) throws Exception;
    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     * @author songly
     * @date 2023/9/15 10:34
     * @return   Json
     */
    Json doDefaultLogic(Tbpagesize tpagesize);

    /**
     * isDuplicateSname
     * 方法功能:重名判断
     * @author yanz
     * @date 2024/2/27 14:27
     * @param id
     * @param sname
     * @return boolean
     */
    boolean isDuplicateSname(Long id, String sname);
}
