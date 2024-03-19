package com.hgzp.advertising.service.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.media.dto.FoldDTO;
import com.hgzp.advertising.pagemodel.media.vo.FoldVO;
import com.hgzp.core.model.Tbfold;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 叠次信息 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbfoldServiceI extends IMyService<Tbfold> {
    /**
     * getFoldPageList
     * 方法功能: 获取媒体叠次分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.advertising.pagemodel.media.vo.FoldVO>
     * @author suny
     * @date 2023/9/25 12:58
     */
    Page<FoldVO> getFoldPageList(Page<Tbfold> page, FoldVO query) throws Exception;

    /**
     * getMediaFoldTree
     * 方法功能:  获取媒体叠次树形结构
     *
     * @param mediaType 媒体类型名称（空表示全部类型）
     * @param getType   获取类型：Media、Fold或者空（空表等同于Fold）
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/9/25 12:58
     */
    List<TreeModel> getMediaFoldTree(String mediaType, String getType) throws Exception;

    /**
     * saveFold
     * 方法功能: 新增保存叠次信息
     *
     * @param tbfold
     * @return void
     * @author suny
     * @date 2023/8/23 15:00
     */
    void saveFold(FoldDTO tbfold);

    /**
     * updateFold
     * 方法功能:  更新保存叠次信息
     *
     * @param tbfold
     * @return void
     * @author suny
     * @date 2023/8/23 15:00
     */
    void updateFold(FoldDTO tbfold);

    /**
     * getFoldCombo
     * 方法功能: 根据媒体id获取叠次下拉框列表
     *
     * @param mediaid
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author suny
     * @date 2023/9/18 15:24
     */
    List<DataCombo> getFoldCombo(String mediaid);

    /**
     * getMaxSort
     * 方法功能: 获取叠次最大isort序号
     *
     * @param
     * @return java.lang.Integer
     * @author suny
     * @date 2023/9/19 13:45
     */
    Integer getMaxSort();

    /**
     * getTbfoldListGroupByFoldId
     * 方法功能: 根据 叠次id 查询 分组后的叠次列表
     *
     * @param foldIdList
     * @return java.util.Map<java.lang.Long, java.util.List < com.hgzp.core.model.Tbfold>>
     * @author wangwk
     * @date 2023/9/19 17:06
     */
    Map<Long, List<Tbfold>> getTbfoldListGroupByFoldId(List<Long> foldIdList);

    /**
     * getFoldChild
     * 方法功能:  验证所选叠次下是否有关联数据，返回存在的属性说明
     *
     * @param ids
     * @return java.lang.String
     * @author suny
     * @date 2023/10/11 10:02
     */
    String getFoldChild(String ids);

    /**
     * 根据媒体类型查询叠次列表
     * 方法功能: 根据媒体类型查询叠次列表
     *
     * @param type
     * @return java.util.List<com.hgzp.core.model.Tbfold>
     * @author suny
     * @date 2024/2/23 10:16
     */
    List<Tbfold> getFoldListByMediaTypeForCJ(String type);
}
