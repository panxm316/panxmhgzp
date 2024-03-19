package com.hgzp.advertising.service.ad;

import com.hgzp.core.model.Tbadfrom;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author new wei
 * @date 2024/3/7 13:36
 */

public interface TbadfromServiceI extends IMyService<Tbadfrom> {
    /**
     *
     * 方法功能:  广告来源树
     * @author songly
     * @date 2024/3/7 13:41
     * @param queryInfo
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    List<TreeModel> getTbAdFromTree(TreeQueryVo queryInfo);

    List<Long> getParentAdFromId(Long adFromId);
    List<Long> getChildAdFromId(Long adFromId);

}
