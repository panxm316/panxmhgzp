package com.hgzp.advertising.controller.media;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.media.vo.PagesortVO;
import com.hgzp.advertising.service.media.TbpagesortServiceI;
import com.hgzp.core.model.Tbpagesort;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PagesortController
 * 创建人：wangwk
 * 类描述：版面类别controller
 * 创建日期：2023/9/19 14:09
 *
 * @folder media/PagesortController
 */
@RestController
@RequestMapping("/media/pagesort")
@Validated
public class PagesortController extends BaseController {

    @Autowired
    TbpagesortServiceI tbpagesortService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor logInterceptor;


    /**
     * 分页查询版面类别列表
     * 方法功能: 分页查询版面类别列表
     *
     * @param request
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.media.vo.PagesortVO>>
     * @author wangwk
     * @date 2023/9/19 17:19
     */
    @GetMapping("/getPagesortList")
    public Json<List<PagesortVO>> getPagesortList(PageRequest request, PagesortVO query) {
        Page page = getPage(request);
        IPage<PagesortVO> pagesortPageList = tbpagesortService.getPagesortPageList(page, query);
        return Json.success(pagesortPageList);
    }

    /**
     * 保存版面类别信息
     * 方法功能: 保存版面类别信息
     *
     * @param tbpagesort
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/savePagesort")
    public Json savePagesort(@RequestBody Tbpagesort tbpagesort) {
        tbpagesortService.savePagesort(tbpagesort);
        return Json.success();
    }

    /**
     * 更新版面类别信息
     * 方法功能: 更新版面类别信息
     *
     * @param tbpagesort
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updatePagesort")
    public Json updatePagesort(@RequestBody Tbpagesort tbpagesort) {
        tbpagesortService.updatePagesort(tbpagesort);
        return Json.success();
    }


    /**
     * 根据id删除版面类别信息
     * 方法功能: 根据id删除版面类别信息
     *
     * @param id
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deteleById")
    public Json deteleById(Long id) {
        logInterceptor.recoredLog();
        tbpagesortService.removeById(id);
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author peij
     * @date 2023/10/27 11:08
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort() {
        Integer maxSort = tbpagesortService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 根据媒体类型和叠次获取版面类别树
     * 方法功能:根据媒体类型和叠次获取版面类别树
     *
     * @param mediaType
     * @param foldId
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author CGD
     * @date 2023/11/13 9:53
     */
    @GetMapping("/getPageSortTreeList")
    public Json<List<TreeModel>> getPageSortTreeList(String mediaType, Long foldId) {
        List<TreeModel> pageSortTreeByQuery = tbpagesortService.getPageSortTreeList(mediaType, foldId);
        return Json.success(pageSortTreeByQuery);
    }

    /**
     * 获取可用的版面类别列表,已排序
     *
     * @param mediaType 媒体类型
     * @param foldId    版次id
     * @return {@link List<Tbpagesort>}
     * @author wangxk
     * @since 2023-12-12
     */
    @GetMapping("/listUsablePageSort")
    public Json<List<Tbpagesort>> listUsablePageSort(String mediaType, Long foldId) {
        return Json.success(tbpagesortService.listUsablePageSort(mediaType, foldId));
    }
}
