package com.hgzp.advertising.service.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.schedule.dto.FoldPageplanDTO;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPagePlaneTreeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPageplanReq;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.service.common.IMyService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 版面计划 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TbfoldpageplanServiceI extends IMyService<Tbfoldpageplan> {
    /**
     * 查询媒体、日期范围的版面计划
     * 方法功能:
     *
     * @param page
     * @param queryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbfoldpageplan>
     * @author songly
     * @date 2023/11/16 10:39
     */
    IPage<Tbfoldpageplan> getMeidaPagePlaneList(Page<Tbfoldpageplan> page, FoldPageplanReq queryInfo);

    /**
     * 版面计划分页查询
     * 方法功能:版面计划分页查询
     *
     * @param page
     * @param queryInfo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbfoldpageplan>
     * @author songly
     * @date 2023/11/15 15:01
     */
    IPage<Tbfoldpageplan> getFoldPagePlanePageList(Page<Tbfoldpageplan> page, FoldPageplanReq queryInfo);

    /**
     * 获取版面计划列表
     * 方法功能:
     *
     * @param tbfoldpageplan
     * @return java.util.List<com.hgzp.core.model.Tbfoldpageplan>
     * @author songly
     * @date 2023/12/7 9:30
     */
    List<Tbfoldpageplan> getFoldPagePlaneList(FoldPageplanReq tbfoldpageplan);

    /**
     * 获取刊发日期的版面计划树
     * 方法功能:
     *
     * @param foldPagePlaneTreeReq
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author songly
     * @date 2023/12/12 13:30
     */
    List<TreeModel> getFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq);

    /**
     * 批量添加版面计划
     * 方法功能:批量添加版面计划(删除原有版面计划)
     *
     * @param foldPageplanDTO
     * @return void
     * @author songly
     * @date 2023/11/16 13:12
     */
    Json saveBatchPagePlane(FoldPageplanDTO foldPageplanDTO) throws Exception;

    /**
     * 按版数批量添加版面计划
     * 方法功能: 按版数批量添加版面计划(不删除原有版面计划，只添加)
     *
     * @param tbfoldpageplan
     * @return void
     * @author songly
     * @date 2023/11/21 10:19
     */
    void savePagePlaneByPageNum(Tbfoldpageplan tbfoldpageplan) throws Exception;

    /**
     * 复制版面计划(按天或周)
     * 方法功能:
     *
     * @param foldPageplanDto
     * @return void
     * @author songly
     * @date 2023/11/15 15:13
     */
    Json duplicatePagePlane(FoldPageplanDTO foldPageplanDto) throws Exception;

    /**
     * 单个添加版面计划
     * 方法功能:
     *
     * @param tbfoldpageplan
     * @return void
     * @author songly
     * @date 2023/11/17 13:27
     */
    void savePagePlane(Tbfoldpageplan tbfoldpageplan) throws Exception;

    /**
     * 修改版面计划
     * 方法功能:
     *
     * @param tbfoldpageplan
     * @return void
     * @author songly
     * @date 2023/11/23 15:41
     */
    void updatePagePlane(Tbfoldpageplan tbfoldpageplan) throws Exception;

    /**
     * 修改版面计划版心
     * 方法功能:
     *
     * @param tbfoldpageplan
     * @return void
     * @author songly
     * @date 2023/11/25 10:47
     */
    void updatePagePlanePageSize(Tbfoldpageplan tbfoldpageplan) throws Exception;

    /**
     * 按Id删除版面计划
     * 方法功能: 按Id删除版面计划
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2023/11/15 15:08
     */
    void deletePagePlaneById(String ids) throws Exception;

    /**
     * 删除符合条件的版面计划
     * 方法功能: 删除符合条件的版面计划
     *
     * @param desReq
     * @return void
     * @author songly
     * @date 2023/11/15 17:20
     */
    void deletePagePlane(FoldPageplanReq desReq) throws Exception;

    /**
     * 获取排期选项，以供前端页面选择
     *
     * @return {@link Set} ["南方日报-A2-东莞观察","南方日报-A2-深圳观察","南方日报-A2-佛山观察","南方日报-A-全省全国"]
     * @author wangxk
     * @since 2023-12-04
     */
    Set<String> getPlanCombo();

    /**
     * 获取媒体叠次计划树（包含非报刊媒体叠次版本）
     * 方法功能:
     *
     * @param foldPagePlaneTreeReq
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author songly
     * @date 2024/1/9 16:18
     */
    List<TreeModel> getMediaFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq);

    /**
     * 获取版数
     * 方法功能:
     *
     * @param foldPageplanReq
     * @return java.util.List<java.lang.Integer>
     * @author hgsongly
     * @date 2024/1/17 13:33
     */
    List<DataCombo> getFoldPagePlanePageNum(FoldPageplanReq foldPageplanReq);

    /**
     * 获取指定叠次的版数内容, 数据来自版面计划表
     * 方法功能:  获取指定叠次的版数内容, 数据来自版面计划表
     *
     * @param FoldId
     * @param foldver
     * @param XDate
     * @return java.util.List<com.hgzp.core.model.Tbfoldpageplan>
     * @author suny
     * @date 2024/2/27 13:25
     */
    List<Tbfoldpageplan> getFoldPagePlaneListByFoldIdForCJ(String FoldId, String foldver, String XDate);
}
