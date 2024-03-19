package com.hgzp.advertising.service.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.schedule.vo.PageColorVo;
import com.hgzp.core.model.Tbpagecolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 版面色彩结构 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TbpagecolorServiceI extends IMyService<Tbpagecolor> {
    /**
     * 方法功能: 分页检索版面色彩数据
     *
     * @param page
     * @param baseQueryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpagecolor>
     * @author songly
     * @date 2023/11/15 9:42
     */
    IPage<PageColorVo> getPageColorList(Page<Tbpagecolor> page, BaseQueryInfo baseQueryInfo);

    /**
     * 获取版面色彩列表数据
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.advertising.pagemodel.schedule.Vo.PageColorVo>
     * @author songly
     * @date 2023/11/16 13:28
     */
    List<PageColorVo> getPageColorAll();

    /**
     * 方法功能: 根据Id删除版面色彩
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2023/11/15 9:42
     */
    void deletePageColorById(String ids);

    /**
     * 获取版面色彩下拉框数据
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author songly
     * @date 2023/11/16 13:28
     */
    List<DataCombo> getPageColorCombo();
}
