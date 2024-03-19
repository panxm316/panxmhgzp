package com.hgzp.advertising.controller.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.media.dto.FoldDTO;
import com.hgzp.advertising.pagemodel.media.vo.FoldVO;
import com.hgzp.advertising.service.media.TbfoldServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbfold;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * FoldController
 * 创建人：suny
 * 类描述：媒体叠次controller
 * 创建日期：2023/8/23 13:09
 *
 * @folder media/FoldController
 */
@RestController
@RequestMapping("/media/fold")
@Validated
public class FoldController extends BaseController {
    @Autowired
    private TbfoldServiceI tbfoldService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取媒体叠次分页列表
     * 方法功能: 获取媒体叠次分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.media.vo.FoldVO>>
     * @author suny
     * @date 2023/9/25 13:00
     */
    @GetMapping("/getFoldPageList")
    public Json<List<FoldVO>> getFoldPageList(PageRequest pageRequest, FoldVO query) throws Exception {
        Page<Tbfold> page = getPage(pageRequest);
        Page<FoldVO> foldList = tbfoldService.getFoldPageList(page, query);
        return Json.success(foldList);
    }

    /**
     * 获取媒体叠次树形结构列表
     * 方法功能: 获取媒体叠次树形结构列表
     *
     * @param mediaType 媒体类型名称（空表示全部类型,可以多个，用“,”隔开）
     * @param getType   获取类型：Media、Fold或者空（空表等同于Fold）
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author suny
     * @date 2023/9/25 12:54
     */
    @GetMapping("/getMediaFoldTree")
    public Json<List<TreeModel>> getMediaFoldTree(String mediaType, String getType) throws Exception {
        List<TreeModel> mediaFoldTree = tbfoldService.getMediaFoldTree(mediaType, getType);
        return Json.success(mediaFoldTree);
    }

    /**
     * 保存叠次
     * 方法功能: 保存叠次
     *
     * @param fold
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.media.dto.FoldDTO>
     * @author suny
     * @date 2023/9/22 15:33
     */
    @PostMapping(value = "/saveFold")
    public Json<FoldDTO> saveFold(@Validated(value = {ValidateParam.add.class}) @RequestBody FoldDTO fold) throws Exception {
        if (fold.getIsort() == null) {
            Integer maxSort = tbfoldService.getMaxSort();
            fold.setIsort(maxSort);
        }
        tbfoldService.saveFold(fold);
        return Json.success(fold);
    }

    /**
     * 更新叠次信息
     * 方法功能:  更新叠次信息
     *
     * @param fold
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/8/23 13:39
     */
    @PostMapping(value = "/updateFold")
    public Json updateFold(@Validated(value = {ValidateParam.edit.class}) @RequestBody FoldDTO fold) throws Exception {
        tbfoldService.updateFold(fold);
        return Json.success();
    }

    /**
     * 根据id删除叠次信息
     * 方法功能: 根据id删除叠次信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/8/23 13:39
     */
    @PostMapping(value = "/deleteFoldById")
    public Json deleteFoldById(@NotNull(message = "ID不可为空") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        tbfoldService.removeByIds(idList);
        return Json.success();
    }

    /**
     * 获取叠次最大排序值
     * 方法功能: 获取叠次最大排序值
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author suny
     * @date 2023/9/25 13:00
     */
    @GetMapping("/getFoldMaxSort")
    public Json<Integer> getFoldMaxSort() {
        Integer maxSort = tbfoldService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 根据媒体id获取叠次下拉框列表
     * 方法功能: 根据媒体id获取叠次下拉框列表
     *
     * @param mediaid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author suny
     * @date 2023/9/18 15:25
     */
    @GetMapping("/getFoldCombo")
    public Json<List<DataCombo>> getFoldCombo(String mediaid) {
        List<DataCombo> foldCombo = tbfoldService.getFoldCombo(mediaid);
        return Json.success(foldCombo);
    }

    /**
     * 验证所选叠次下是否有关联数据
     * 方法功能: 验证所选叠次下是否有关联数据，有则不允许删除，返回存在的属性说明
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/11 9:56
     */
    @GetMapping(value = "/getBFoldChild")
    public Json getBFoldChild(@NotNull(message = "ID不可为空") String ids) {
        String chiledFlagMsg = tbfoldService.getFoldChild(ids);
        return Json.success(chiledFlagMsg);
    }
}