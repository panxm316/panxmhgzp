package com.hgzp.advertisingsys.controller.media;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaPublicNumDto;
import com.hgzp.advertisingsys.service.media.TbmediapublicnumServiceI;
import com.hgzp.core.model.Tbmediapublicnum;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * MediaPublicNumController
 * 创建人：CGD
 * 类描述：媒体刊期设置
 * 创建日期：2023/8/17 11:54
 * @folder media/MediaPublicNumController
 */
@Validated
@RestController
@RequestMapping("/media/mediapublicnum")
public class MediaPublicNumController extends BaseController {

    @Autowired
    private TbmediapublicnumServiceI tbmediapublicnumService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    /**
     * 分页查询媒体刊期
     * 方法功能:  分页查询媒体刊期
     * @author CGD
     * @date 2023/9/7 13:30
     * @param pageRequeste
     * @param baseQueryInfo
     * @param mediapublicnum
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbmediapublicnum>>
     */
    @GetMapping("/getMediaPublicNumPageList")
    public Json<List<Tbmediapublicnum>> getMediaPublicNumPageList(PageRequest pageRequeste, BaseQueryInfo baseQueryInfo, Tbmediapublicnum mediapublicnum) {
        Page page = getPage(pageRequeste);
        Page<Tbmediapublicnum> mediaPublicNumPageList = tbmediapublicnumService.getMediaPublicNumPageList(page, baseQueryInfo, mediapublicnum);
        return Json.success(mediaPublicNumPageList);
    }

    /**
     * 单个新增媒体刊期
     * 方法功能:  单个新增媒体刊期
     * @author CGD
     * @date 2023/9/7 13:31
     * @param mediapublicnum
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveMediaPublicNum")
    public Json saveMediaPublicNum(@RequestBody Tbmediapublicnum mediapublicnum) {
        if (tbmediapublicnumService.examinePublishno(mediapublicnum)) {
            return Json.fail(mediapublicnum.getMedianame() + " 刊期已存在");
        }
        innerInterceptor.recoredLog();
        tbmediapublicnumService.save(mediapublicnum);
        return Json.success();
    }

    /**
     * 修改媒体刊期
     * 方法功能:  修改媒体刊期
     * @author CGD
     * @date 2023/9/7 13:31
     * @param mediapublicnum
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateMediaPublicNum")
    public Json updateMediaPublicNum(@RequestBody Tbmediapublicnum mediapublicnum) {
        if (tbmediapublicnumService.examinePublishno(mediapublicnum)) {
            return Json.fail(mediapublicnum.getMedianame() + mediapublicnum.getSpublishno() + "已存在");
        }
        innerInterceptor.recoredLog();
        tbmediapublicnumService.updateById(mediapublicnum);
        return Json.success();
    }

    /**
     * 删除媒体刊期多个支持
     * 方法功能: 删除媒体刊期多个支持","分割
     * @author CGD
     * @date 2023/9/7 13:31
     * @param ids
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteMediaPublicNum")
    public Json deleteMediaPublicNum(@NotBlank(message = "请传入需要删除的id") String ids){
//        innerInterceptor.recoredLog();
//        tbmediapublicnumService.removeByIds(CollUtil.newArrayList(ids.split(",")));

        try {
            tbmediapublicnumService.deleteMediaPublicNumByIds(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
    /**
     * 批量新增媒体刊期
     * 方法功能: 批量新增媒体刊期
     * @author CGD
     * @date 2023/9/8 15:26
     * @param mediaPublicNumDto
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveBatchMediaPublicNum")
    public Json saveBatchMediaPublicNum(@Validated @RequestBody MediaPublicNumDto mediaPublicNumDto){
        tbmediapublicnumService.saveBatchMediaPublicNum(mediaPublicNumDto);
        return Json.success();
    }

}
