package com.hgzp.advertisingsys.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbadfrom;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * TbadfromServiceI
 * 广告来源信息 服务类
 * @author muyn
 * @since 2023-08-15
 */
public interface TbadfromServiceI extends IMyService<Tbadfrom> {
    /**
     * deleteAdFrom
     * 通过主键删除数据
     * @param tbAdFromIds 主键串
     * @return 是否成功
     */
    void deleteAdFrom(String tbAdFromIds);

    /**
     * getAdFromPageList
     * 方法功能:  广告来源
     * @author muyn
     * @date 2023/8/17 10:04
     * @param page
     * @param queryInfo
     * @return Json
     */
    IPage<Tbadfrom> getAdFromPageList(Page<Tbadfrom> page, TreeQueryVo queryInfo);

    /**
     * getTbAdFromTree
     * 方法功能： 广告来源树
     * @author: muyn
     * @param queryInfo
     * @return
     */
    List<TreeModel> getTbAdFromTree(TreeQueryVo queryInfo);

    /**
     * saveFromExtend
     * 方法功能： 保存来源扩展
     * @author: muyn
     * @param adFrom 来源对象
     * @return
     */
    void saveFromExtend(Tbadfrom adFrom);

    /**
     * saveAdFrom
     * 方法功能： 重写保存来源方法，增加了判断是否重名
     * @author: muyn
     * @param tbAdFrom 来源对象
     * @return void
     */
    void saveAdFrom(Tbadfrom tbAdFrom);

    /**
     * updateAdFrom
     * 方法功能： 重写更新来源方法，增加了判断是否重名
     * @author: muyn
     * @param tbAdFrom 来源对象
     * @return void
     */
    void updateAdFrom(Tbadfrom tbAdFrom);
    /**
     * resetAdFromExtend
     * 方法功能： 重置来源扩展表
     * @author: muyn
     * @return void
     */
    void resetAdFromExtend();
}
