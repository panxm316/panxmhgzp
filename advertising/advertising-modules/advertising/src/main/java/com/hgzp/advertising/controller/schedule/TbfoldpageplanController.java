package com.hgzp.advertising.controller.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.schedule.dto.FoldPageplanDTO;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPagePlaneTreeReq;
import com.hgzp.advertising.pagemodel.schedule.vo.FoldPageplanReq;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.page.ElTreeNode;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * TbfoldpageplanController
 * 创建人：songly
 * 类描述：版面色彩服务接口
 * 创建日期：2023/11/16 13:20
 *
 * @folder schedule/TbfoldpageplanController
 */
@Validated
@RestController
@RequestMapping("/schedule/foldpageplan")
public class TbfoldpageplanController extends BaseController {

    @Autowired
    TbfoldpageplanServiceI foldPageplanService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 按媒体获取版面计划
     * 方法功能: 按媒体获取版面计划
     *
     * @param pageRequeste
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbfoldpageplan>>
     * @author songly
     * @date 2023/11/16 13:03
     */
    @GetMapping("/getMeiaPageplanList")
    public Json<List<Tbfoldpageplan>> getMediaPageplanList(PageRequest pageRequeste, FoldPageplanReq queryInfo) {
        Page page = getPage(pageRequeste);
        IPage<Tbfoldpageplan> FoldPageplanList = foldPageplanService.getMeidaPagePlaneList(page, queryInfo);
        return Json.success(FoldPageplanList);
    }

    /**
     * 获取版面计划分页列表
     * 方法功能: 获取版面计划分页列表
     *
     * @param pageRequeste
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbfoldpageplan>>
     * @author songly
     * @date 2023/11/16 13:05
     */
    @GetMapping("/getFoldPageplanList")
    public Json<List<Tbfoldpageplan>> getFoldPageplanPageList(PageRequest pageRequeste, FoldPageplanReq queryInfo) {
        Page page = getPage(pageRequeste);
        IPage<Tbfoldpageplan> FoldPageplanList = foldPageplanService.getFoldPagePlanePageList(page, queryInfo);
        return Json.success(FoldPageplanList);
    }

    /**
     * 按媒体、叠、类别、色彩、日期等获取版面计划列表
     * 方法功能: 按媒体、叠、类别、色彩、日期等获取版面计划列表
     *
     * @param foldPageplanReq
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbfoldpageplan>>
     * @author songly
     * @date 2023/12/7 9:45
     */
    @GetMapping("/getPageplanList")
    public Json<List<Tbfoldpageplan>> getFoldPageplanList(FoldPageplanReq foldPageplanReq) {

        List<Tbfoldpageplan> FoldPageplanList = foldPageplanService.getFoldPagePlaneList(foldPageplanReq);
        return Json.success(FoldPageplanList);
    }

    /**
     * 根据Id获取版面计划
     * 方法功能: 根据Id获取版面计划
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbfoldpageplan>
     * @author songly
     * @date 2023/11/16 13:05
     */
    @GetMapping(value = "/getFoldPageplanById")
    public Json<Tbfoldpageplan> getFoldPageplanById(@NotNull(message = "ID不可为空") String id) {
        Tbfoldpageplan tbFoldPageplan = foldPageplanService.getById(id);
        return ObjectUtil.isNotNull(tbFoldPageplan) ? Json.success(tbFoldPageplan) : Json.fail();
    }

    /**
     * 单个保存版面计划
     * 方法功能: 保存版面计划
     *
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/16 13:05
     */
    @PostMapping("/saveFoldPageplan")
    public Json saveFoldPageplan(@RequestBody Tbfoldpageplan tbFoldPageplan) {
        try {
            foldPageplanService.savePagePlane(tbFoldPageplan);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 批量添加版面计划
     * 方法功能: 批量添加版面计划
     *
     * @param foldPageplanDto
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/16 15:23
     */
    @PostMapping("/saveBatchPagePlane")
    public Json saveBatchPagePlane(@RequestBody FoldPageplanDTO foldPageplanDto) {
        try {
            return foldPageplanService.saveBatchPagePlane(foldPageplanDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 按版数增加版面计划
     * 方法功能:
     *
     * @param foldPageplan
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/21 10:40
     */
    @PostMapping("/savePagePlaneByPageNum")
    public Json savePagePlaneByPageNum(@RequestBody Tbfoldpageplan foldPageplan) {
        try {
            foldPageplanService.savePagePlaneByPageNum(foldPageplan);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改版面计划
     * 方法功能: 修改版面计划
     *
     * @param tbFoldPageplan
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/16 13:06
     */
    @PostMapping("/updateFoldPageplan")
    public Json updateFoldPageplan(@RequestBody Tbfoldpageplan tbFoldPageplan) {
        DateTime dateTime = DateUtil.offsetDay(tbFoldPageplan.getPublishdate(), -1);
        tbFoldPageplan.setStoptime(dateTime);

        innerInterceptor.recoredLog();
        try {
            foldPageplanService.updatePagePlane(tbFoldPageplan);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id删除版面计划
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/16 13:06
     */
    @PostMapping("/deleteFoldPageplan")
    public Json deleteFoldPageplan(@NotBlank(message = "ids不能为空") String ids) {
        innerInterceptor.recoredLog();
        try {
            foldPageplanService.deletePagePlaneById(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 批量删除媒体版面计划
     * 方法功能:
     *
     * @param sourceReq
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/16 13:08
     */
    @PostMapping("/deleteMediaPageplan")
    public Json deleteMediaPageplan(@RequestBody FoldPageplanReq sourceReq) {
        innerInterceptor.recoredLog();
        try {
            foldPageplanService.deletePagePlane(sourceReq);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 修改版面计划版心
     * 方法功能:
     *
     * @param tbFoldPageplan
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/11/25 10:55
     */
    @PostMapping("/updatePagePlanePageSize")
    public Json updatePagePlanePageSize(@RequestBody Tbfoldpageplan tbFoldPageplan) {
        try {
            foldPageplanService.updatePagePlanePageSize(tbFoldPageplan);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取排期选项，以供前端页面选择 todo 待删除
     *
     * @return {@link Json} ["南方日报-A2-东莞观察","南方日报-A2-深圳观察","南方日报-A2-佛山观察","南方日报-A-全省全国"]
     * @author wangxk
     * @since 2023-12-04
     */
    @GetMapping("/getPlanCombo")
    public Json getPlanCombo() {
        Set<String> options = foldPageplanService.getPlanCombo();
        return Json.success(options);
    }

    /**
     * 获取排期计划列表
     *
     * @param date         预刊日期 "2020-01-01"
     * @param mediaTypeKey 媒体类型key不可为空
     * @param mediaId      媒体id
     * @param foldId       叠次id
     * @return {@link List<Tbfoldpageplan>} 排期计划列表
     * @author wangxk
     * @since 2023-12-06
     */
    @GetMapping("/listPlanOnPreOrder")
    public Json<List<Tbfoldpageplan>> listPlanOnPreOrder(@NotBlank(message = "预刊日期不可为空") String date,
                                                         @NotBlank(message = "媒体类型key不可为空") String mediaTypeKey,
                                                         String mediaId, String foldId) {
        List<Tbfoldpageplan> list = foldPageplanService.list(Wrappers.<Tbfoldpageplan>lambdaQuery()
                .eq(Tbfoldpageplan::getMediatypekey, mediaTypeKey)
                .eq(StrUtil.isNotBlank(mediaId), Tbfoldpageplan::getMediaid, mediaId)
                .eq(StrUtil.isNotBlank(foldId), Tbfoldpageplan::getFoldid, foldId)
                .eq(Tbfoldpageplan::getPublishdate, date)
                .eq(Tbfoldpageplan::getStopflag, false)
                .orderByAsc(Tbfoldpageplan::getPagenum)); // 不需要处理截版日期
        return Json.success(list);
    }

    /**
     * 根据发布日期获取版面计划树
     * 方法功能:
     *
     * @param foldPagePlaneTreeReq
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author songly
     * @date 2023/12/12 14:18
     */
    @GetMapping(value = "/getFoldPagePlaneTree")
    public Json<List<TreeModel>> getFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq) {
        if (foldPagePlaneTreeReq.getMediatypekey().equals("paper") && ObjectUtil.isNull(foldPagePlaneTreeReq.getPublishdate())) {
            return Json.fail("预刊发日期不可为空");
        }
        List<TreeModel> foldPagePlaneTree = foldPageplanService.getFoldPagePlaneTree(foldPagePlaneTreeReq);
        return ObjectUtil.isNotNull(foldPagePlaneTree) ? Json.success(foldPagePlaneTree) : Json.fail();
    }

    /**
     * 获取有效媒体的版面计划树（含有非报刊媒体）
     * 方法功能:  获取有效媒体的版面计划树（含有非报刊媒体）
     *
     * @param foldPagePlaneTreeReq
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author songly
     * @date 2024/1/9 16:54
     */
    @GetMapping(value = "/getMediaFoldPagePlaneTree")
    public Json<List<TreeModel>> getMediaFoldPagePlaneTree(FoldPagePlaneTreeReq foldPagePlaneTreeReq) {
        if (foldPagePlaneTreeReq.getMediatypekey().equals("paper") && ObjectUtil.isNull(foldPagePlaneTreeReq.getPublishdate())) {
            return Json.fail("预刊发日期不可为空");
        }
        List<TreeModel> foldPagePlaneTree = foldPageplanService.getMediaFoldPagePlaneTree(foldPagePlaneTreeReq);
        return ObjectUtil.isNotNull(foldPagePlaneTree) ? Json.success(foldPagePlaneTree) : Json.fail();
    }
}
