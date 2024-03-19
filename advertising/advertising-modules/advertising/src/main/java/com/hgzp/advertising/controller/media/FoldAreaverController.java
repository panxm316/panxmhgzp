package com.hgzp.advertising.controller.media;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.vo.MediaFoldTree;
import com.hgzp.advertising.pagemodel.media.vo.FoldAreaverVO;
import com.hgzp.advertising.service.media.TbfoldareaverServiceI;
import com.hgzp.core.model.Tbfoldareaver;
import com.hgzp.core.page.ElTreeNode;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * FoldAreaverController
 * 创建人：songly
 * 类描述：叠次版本controller
 * 创建日期：2023/9/19 08:51
 *
 * @folder media/FoldAreaverController
 */
@Validated
@RestController
@RequestMapping("/media/foldareaver")
public class FoldAreaverController extends BaseController {
    @Autowired
    private TbfoldareaverServiceI tbfoldareaverService;

    /**
     * 根据Id获取叠次版本信息
     * 方法功能: 根据Id获取叠次版本信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbfoldareaver>
     * @author songly
     * @date 2023/9/19 10:11
     */
    @GetMapping(value = "/getFoldAreaverById")
    public Json<Tbfoldareaver> getFoldAreaverById(@NotNull(message = "ID不可为空") String id) {
        Tbfoldareaver foldareaver = tbfoldareaverService.getById(id);
        return ObjectUtil.isNotNull(foldareaver) ? Json.success(foldareaver) : Json.fail();
    }

    /**
     * 获取叠次版本分页列表
     * 方法功能:  获取叠次版本分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbfoldareaver>>
     * @author songly
     * @date 2023/9/19 09:38
     */
    @GetMapping("/getFoldAreaverList")
    public Json<List<FoldAreaverVO>> getFoldAreaverList(PageRequest pageRequest, FoldAreaverVO query) {
        Page<Tbfoldareaver> page = getPage(pageRequest);
        IPage<FoldAreaverVO> foldList = tbfoldareaverService.getFoldAreaverPageList(page, query);
        return Json.success(foldList);
    }

    /**
     * 保存叠次版本信息
     * 方法功能:  保存叠次版本信息
     *
     * @param foldareaver
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/9/19 10:01
     */
    @PostMapping(value = "/saveFoldAreaver")
    public Json saveFoldAreaver(@RequestBody Tbfoldareaver foldareaver) {
        tbfoldareaverService.saveFoldAreaver(foldareaver);
        return Json.success();
    }

    /**
     * 更新叠次版本信息
     * 方法功能:  更新叠次版本信息
     *
     * @param foldareaver
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/9/19 10:09
     */
    @PostMapping(value = "/updateFoldAreaver")
    public Json updateFoldAreaver(@RequestBody Tbfoldareaver foldareaver) {
        tbfoldareaverService.updateFoldAreaver(foldareaver);
        return Json.success();
    }

    /**
     * 根据id删除叠次版本信息
     * 方法功能: 根据id删除叠次版本信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/9/19 10:12
     */
    @PostMapping(value = "/deleteFoldAreaverById")
    public Json deleteFoldById(@NotNull(message = "ID不可为空") String ids) {
        tbfoldareaverService.deleteFoldAreaverByIds(ids);
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author peij
     * @date 2023/10/27 11:07
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort() {
        Integer maxSort = tbfoldareaverService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 获取叠次版本树
     * 方法功能: 获取叠次版本树,可根据叠次id查询
     *
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     */
    @GetMapping("/getFoldAreaverTreeList")
    public Json<List<TreeModel>> getFoldAreaverTreeList(Long foldId) {
        List<TreeModel> foldAreaverTree = tbfoldareaverService.getFoldAreaverTreeList(foldId);
        return Json.success(foldAreaverTree);
    }

    /**
     * 获取媒体类型-媒体-叠次树,前端使用 Element Plus 树组件
     *
     * @return {@link List<ElTreeNode>} Element Plus 树节点集合
     * @author wangxk
     * @see <a href="https://element-plus.org/zh-CN/component/tree-select.html">参考链接</a>
     * @since 2023-12-06
     */
    @GetMapping("/getMediaFoldElTree")
    public Json<List<ElTreeNode>> getMediaFoldElTree() {
        List<ElTreeNode> list = tbfoldareaverService.getMediaFoldElTree();
        return Json.success(list);
    }

    /**
     * 获取媒体类型-媒体-叠次树, 移动端使用
     *
     * @return {@link List<TreeModel>} 树节点集合
     * @author wangxk
     * @since 2024-03-18
     */
    @GetMapping("/getMediaFoldTree")
    public Json<List<MediaFoldTree>> getMediaFoldTree() {
        List<MediaFoldTree> list = tbfoldareaverService.getMediaFoldTree();
        return Json.success(list);
    }

    /**
     * 获取有效的媒体、版面、叠次
     * 方法功能: 用于HgSingleZtree组件
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author songly
     * @date 2023/12/26 9:39
     */
    @GetMapping("/getMediaFoldAreaverTree")
    public Json<List<TreeModel>> getMediaFoldAreaverTree() {
        List<TreeModel> foldAreaverTree = tbfoldareaverService.getMediaFoldAreaverTree();
        return Json.success(foldAreaverTree);
    }
}