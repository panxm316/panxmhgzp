package com.hgzp.advertisingsys.controller.media;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaTypeDTO;
import com.hgzp.advertisingsys.pagemodel.media.vo.MediaTypeVO;
import com.hgzp.advertisingsys.service.media.TbmediatypeServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * MediaTypeController
 * 创建人：yanz
 * 类描述： 媒体类型 Controller
 * 创建日期：2023/8/16 17:12
 *
 * @folder media/MediaTypeController
 */
@Validated
@RestController
@RequestMapping("/media/mediatype")
public class MediaTypeController extends BaseController {
    @Autowired
    private TbmediatypeServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取媒体类型
     * 方法功能:据id获取媒体类型
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbmediatype>
     * @author yanz
     * @date 2023/8/17 15:58
     */
    @GetMapping(value = "/getMediaTypeById")
    public Json<Tbmediatype> getMediaTypeById(@NotNull(message = "ID不可为空") String id) {
        Tbmediatype mediatype = serviceI.getById(id);
        return ObjectUtil.isNotNull(mediatype) ? Json.success(mediatype) : Json.fail();
    }

    /**
     * 获取媒体类型
     * 方法功能:分页：获取媒体类型
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbmediatype>>
     * @author yanz
     * @date 2023/8/31 15:05
     */
    @GetMapping(value = "/getMediaTypePageList")
    public Json<List<Tbmediatype>> getMediaTypePageList(PageRequest pageRequest, MediaTypeVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbmediatype> pages = serviceI.getMediaTypePageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 获取媒体类型下拉列表
     * 方法功能: 获取媒体类型下拉列表
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author suny
     * @date 2023/8/24 14:35
     */
    @GetMapping(value = "/getMediaTypeCombo")
    public Json<List<DataCombo>> getMediaTypeCombo() throws Exception {
        List<DataCombo> list = serviceI.getMediaTypeCombo();
        return Json.success(list);
    }

    /**
     * 修改媒体类型
     * 方法功能:修改媒体类型
     *
     * @param Mediatype
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 8:57
     */
    @PostMapping(value = "/updateMediaType")
    public Json updateMediaType(@Validated(value = {ValidateParam.edit.class}) @RequestBody MediaTypeDTO Mediatype) {
        Tbmediatype newMediatype = new Tbmediatype();
        BeanUtils.copyProperties(Mediatype, newMediatype);
        Json jsonRet = serviceI.doDefaultLogic(newMediatype);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        serviceI.updateMediaType(newMediatype);
        return Json.success();
    }

    /**
     * 据id删除媒体类型
     * 方法功能:据id删除媒体类型
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deleteMediaTypeById")
    public Json deleteMediaTypeById(@NotNull(message = "ID不可为空") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        if (serviceI.removeByIds(idList)) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加媒体类型
     * 方法功能:添加媒体类型
     *
     * @param mediatype
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/17 16:27
     */
    @PostMapping(value = "/saveMediaType")
    public Json saveMediaType(@Validated(value = {ValidateParam.add.class}) @RequestBody MediaTypeDTO mediatype) {
        Tbmediatype tbmediatype = new Tbmediatype();
        BeanUtils.copyProperties(mediatype, tbmediatype);
        Json jsonRet = serviceI.doDefaultLogic(tbmediatype);
        if (!jsonRet.isSuccess()) {
            return Json.fail(jsonRet.getMsg());
        }
        serviceI.saveMediaType(tbmediatype);
        return Json.success();
    }

    /**
     * 获取媒体类型最大序号
     * 方法功能:获取媒体类型最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/19 14:25
     */
    @GetMapping("/getMediaTypeMaxSort")
    public Json<Integer> getMediaTypeMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }
}
