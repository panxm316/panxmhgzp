package com.hgzp.advertisingsys.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.finance.vo.AdPrintItemVO;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 开票项目 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbadprintitemServiceI extends IMyService<Tbadprintitem> {

    /**
     * getAdPrintItemPageList
     * 方法功能:分页：获取开票项目
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbadprintitem>
     * @author yanz
     * @date 2023/8/31 15:06
     */
    IPage<Tbadprintitem> getAdPrintItemPageList(Page<Tbadprintitem> page, AdPrintItemVO vo);

    /**
     * deleteAdPrintItemByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:31
     */
    boolean deleteAdPrintItemByIds(List<String> idList);

    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     *
     * @return Json
     * @author songly
     * @date 2023/9/15 10:34
     */
    Json doDefaultLogic(Tbadprintitem tbadprintitem);

    /**
     * 判断重名
     * 方法功能:
     *
     * @param tbadprintitem
     * @return boolean
     * @author songly
     * @date 2024/2/27 10:09
     */
    boolean isDuplicateSname(Tbadprintitem tbadprintitem);
}
