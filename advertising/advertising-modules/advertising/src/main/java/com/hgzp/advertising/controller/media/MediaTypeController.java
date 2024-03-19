package com.hgzp.advertising.controller.media;

import com.hgzp.advertising.service.media.TbmediatypeServiceI;
import com.hgzp.core.model.Tbmediatype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MediaTypeController
 * 创建人：suny
 * 类描述： 媒体类型 Controller
 * 创建日期：2023/11/06 10:36
 *
 * @folder media/MediaTypeController
 */
@Validated
@RestController
@RequestMapping("/media/mediatype")
public class MediaTypeController extends BaseController {
    @Autowired
    private TbmediatypeServiceI serviceI;


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
     * 获取可用的媒体列表,已排序
     *
     * @return {@link List<Tbmediatype>} 可用的媒体列表
     * @author wangxk
     * @since 2023-12-11
     */
    @GetMapping(value = "/listUsableMediaType")
    public Json<List<Tbmediatype>> listUsableMediaType() {
        return Json.success(serviceI.listUsableMediaType());
    }
}
