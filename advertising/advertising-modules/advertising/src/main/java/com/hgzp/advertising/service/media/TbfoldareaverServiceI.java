package com.hgzp.advertising.service.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.MediaFoldTree;
import com.hgzp.advertising.pagemodel.media.vo.FoldAreaverVO;
import com.hgzp.core.model.Tbfoldareaver;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.ElTreeNode;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 叠次版本信息 服务类
 * </p>
 *
 * @author songly
 * @date 2023/9/18 15:39
 */
public interface TbfoldareaverServiceI extends IMyService<Tbfoldareaver> {
    /**
     * getFoldAreaverPageList
     * 方法功能: 获取叠次版本分页列表
     *
     * @param page
     * @param query
     * @return
     */
    IPage<FoldAreaverVO> getFoldAreaverPageList(Page<Tbfoldareaver> page, FoldAreaverVO query);

    /**
     * saveFoldAreaver
     * 方法功能: 保存叠次版本信息
     *
     * @param
     * @return
     * @author songly
     * @date 2023/9/18 16:19
     */
    void saveFoldAreaver(Tbfoldareaver tbfoldareaver);

    /**
     * updateFoldAreaver
     * 方法功能:  更新叠次版本信息
     *
     * @param tbfoldareaver
     * @return void
     * @author songly
     * @date 2023/9/18 16:20
     */
    void updateFoldAreaver(Tbfoldareaver tbfoldareaver);

    /**
     * isDuplicateSname
     * 方法功能: 检查名称是否重复
     *
     * @param tbfoldareaver
     * @return boolean
     * @author songly
     * @date 2023/9/18 16:21
     */
    boolean isExistFoldAreaver(Tbfoldareaver tbfoldareaver);

    /**
     * deleteAdTypeByIds
     * 方法功能: 删除叠次版本
     *
     * @param ids
     * @return boolean
     * @author songly
     * @date 2023/9/18 16:21
     */
    void deleteFoldAreaverByIds(String ids);

    /**
     * 获取叠次版本树
     * 方法功能: 获取叠次版本树,可根据叠次id查询
     *
     * @param foldId 叠次id
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author CGD
     * @date 2023/11/11 14:40
     */
    List<TreeModel> getFoldAreaverTreeList(Long foldId);

    /**
     * 获取媒体类型-媒体-叠次树,前端使用 Element Plus 树组件
     *
     * @return {@link List<ElTreeNode>} Element Plus 树节点集合
     * @author wangxk
     * @see <a href="https://element-plus.org/zh-CN/component/tree-select.html">参考链接</a>
     * @since 2023-12-06
     */
    List<ElTreeNode> getMediaFoldElTree();

    /**
     * 获取媒体类型-媒体-叠次树, 移动端使用
     *
     * @return {@link List<TreeModel>} 树节点集合
     * @author wangxk
     * @since 2024-03-18
     */
    List<MediaFoldTree> getMediaFoldTree();

    /**
     * 获取有效的媒体、叠次、叠次版本树
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author songly
     * @date 2023/12/26 9:20
     */
    List<TreeModel> getMediaFoldAreaverTree();

    /**
     * 获取指定叠次的叠次地域版次内容
     * 方法功能: 获取指定叠次的叠次地域版次内容
     *
     * @param foldId
     * @return java.util.List<com.hgzp.core.model.Tbfoldareaver>
     * @author suny
     * @date 2024/2/27 13:23
     */
    List<Tbfoldareaver> getFoldAreaverListByFoldIdForCJ(String foldId);

    /**
     * getFoldAreaverCombo
     * 方法功能: 根据叠次id获取叠次版本下拉列表框
     *
     * @param foldId
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author lhl
     * @date 2024/03/08
     */
    List<DataCombo> getFoldAreaverCombo(String foldId);

}
