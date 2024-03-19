package com.hgzp.advertising.service.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.vo.AdDisplayFormVO;
import com.hgzp.core.model.Tbaddisplayform;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 广告形式 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-06
 */
public interface TbaddisplayformServiceI extends IMyService<Tbaddisplayform> {
    /**
     * 方法功能: 获取广告形式分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbaddisplayform>
     * @author suny
     * @date 2023/11/6 10:22
     */
    Page<Tbaddisplayform> getAdDisplayFormPageList(Page<Tbaddisplayform> page, AdDisplayFormVO query) throws Exception;

    /**
     * 方法功能: 新增保存广告形式
     *
     * @param tbaddisplayform
     * @return void
     * @author suny
     * @date 2023/11/6 10:23
     */
    void saveAdDisplayForm(Tbaddisplayform tbaddisplayform);

    /**
     * 方法功能:  修改保存广告形式
     *
     * @param tbaddisplayform
     * @return void
     * @author suny
     * @date 2023/11/6 10:23
     */
    void updateAdDisplayForm(Tbaddisplayform tbaddisplayform);

    /**
     * getMaxSort
     * 方法功能: 获取广告形式最大isort序号
     *
     * @param
     * @return java.lang.Integer
     * @author suny
     * @date 2023/11/06 13:45
     */
    Integer getMaxSort();
    /**
     * 根据媒体类型获取广告形式树
     * 方法功能: 根据媒体类型获取广告形式树
     * @author CGD
     * @date 2023/11/13 9:01
     * @param mediaType
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    List<TreeModel> getAdDisplayFormTreeList(String mediaType);

    /**
     * 根据媒体类型获取可用的广告形式列表,已排序
     *
     * @param mediaType 媒体类型
     * @return {@link List<Tbaddisplayform>}
     * @author wangxk
     * @since 2023-12-12
     */
    List<Tbaddisplayform> listUsableAdDisplayForm(String mediaType);
}
