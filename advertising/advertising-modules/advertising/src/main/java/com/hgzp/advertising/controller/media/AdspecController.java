package com.hgzp.advertising.controller.media;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.media.dto.AdspecDateModelDTO;
import com.hgzp.advertising.pagemodel.media.vo.AdspecModelVO;
import com.hgzp.advertising.service.media.TbadspecServiceI;
import com.hgzp.core.model.Tbadspec;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * AdspecController
 * 创建人：CGD
 * 类描述：广告规格controller
 * 创建日期：2023/8/23 13:09
 *
 * @folder media/AdspecController
 */
@RestController
@RequestMapping("/media/adspec")
@Validated
public class AdspecController extends BaseController {
    @Autowired
    private TbadspecServiceI tbadspecService;


    /**
     * 广告规格分页查询
     * 方法功能: 广告规格分页查询
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.media.vo.AdspecModelVO>>
     * @author CGD
     * @date 2023/9/19 15:48
     */
    @GetMapping("/getAdspecPageList")
    public Json<List<AdspecModelVO>> getAdspecPageList(PageRequest pageRequest, AdspecModelVO query) {
        Page<Tbadspec> page = getPage(pageRequest);
        Page<AdspecModelVO> adspecList = tbadspecService.getAdspecPageList(page, query);
        return Json.success(adspecList);
    }


    /**
     * 广告规格新增
     * 方法功能:  广告规格新增
     *
     * @param adspec
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/9/19 15:49
     */
    @PostMapping(value = "/saveAdspec")
    public Json saveAdspec(@RequestBody Tbadspec adspec) {
        tbadspecService.saveAdspec(adspec);
        return Json.success();
    }

    /**
     * 广告规格修改
     * 方法功能:  广告规格修改
     *
     * @param adspec
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/9/19 15:49
     */
    @PostMapping(value = "/updateAdspec")
    public Json updateAdspec(@RequestBody Tbadspec adspec) {
        tbadspecService.updateAdspec(adspec);
        return Json.success();
    }

    /**
     * 批量删除广告规格
     * 方法功能: 批量删除广告规格
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/9/19 15:51
     */
    @PostMapping(value = "/deleteAdspecById")
    public Json deleteAdspecById(@NotNull(message = "ID不可为空") String ids) {
        tbadspecService.deleteAdspecById(ids);
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能:   获取最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author CGD
     * @date 2023/9/19 15:51
     */
    @GetMapping("/getMaxSort")
    public Json<Integer> getMaxSort() {
        Integer maxSort = tbadspecService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 更新规格时间
     * 方法功能:  更新规格时间
     *
     * @param adspecDate
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/9/20 15:42
     */
    @PostMapping(value = "/updateAdspecDate")
    public Json updateAdspecDate(@RequestBody AdspecDateModelDTO adspecDate) {
        tbadspecService.updateAdspecDate(adspecDate);
        return Json.success();
    }

    /**
     * 根据媒体id获取广告规格树
     * 方法功能:  根据媒体id获取广告规格树
     *
     * @param mediaId
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author CGD
     * @date 2023/11/13 9:25
     */
    @GetMapping("/getAdspecTreeList")
    public Json<List<TreeModel>> getAdspecTreeList(String mediaId) {
        List<TreeModel> adspecTreeList = tbadspecService.getAdspecTreeList(mediaId);
        return Json.success(adspecTreeList);
    }

    /**
     * 根据媒体id获取可用的广告规格列表,已排序
     *
     * @param mediaId 媒体id
     * @return {@link List<Tbadspec>}
     * @author wangxk
     * @since 2023-12-12
     */
    @GetMapping("/listUsableAdSpec")
    public Json<List<Tbadspec>> listUsableAdSpec(Long mediaId) {
        return Json.success(tbadspecService.listUsableAdSpec(mediaId));
    }
}