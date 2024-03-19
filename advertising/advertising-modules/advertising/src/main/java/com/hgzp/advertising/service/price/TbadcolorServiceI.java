package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbadcolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;


/**
 * <p>
 * 广告色彩 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbadcolorServiceI extends IMyService<Tbadcolor> {

    /**
     * deleteAdcolorById
     * 方法功能: 根据id删除广告色彩
     * @author peij
     * @date 2023/8/17 16:13
     * @param ids
     * @return int
     */
    void deleteAdcolorById(String ids);

   /**
    * getAdcolorList
    * 方法功能: 获取广告色彩列表
    * @author peij
    * @date 2023/8/24 14:31
    * @param page
    * @param baseQueryInfo
    * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbadcolor>
    */
    IPage<Tbadcolor> getAdcolorList(Page<Tbadcolor> page,BaseQueryInfo baseQueryInfo,String mediatypekey);

    /**
     * 根据媒体类型获取广告色彩树
     * 方法功能:根据媒体类型获取广告色彩树
     * @author CGD
     * @date 2023/11/13 9:54
     * @param mediaType
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    List<TreeModel> getAdColorTreeList(String mediaType);
    /**
     * 获取有效的广告色彩列表
     * 方法功能:
     * @author songly
     * @date 2023/11/16 14:17
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbadcolor>
     */
    List<Tbadcolor>getAdColorListValid(String mediaType);
}
