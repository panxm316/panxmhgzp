package com.hgzp.advertising.controller.media;

import com.hgzp.advertising.service.media.TbpagesizeServiceI;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * PageSizeController
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/11/25 10:21
 *
 * @folder media/PageSizeController
 */
@Validated
@RestController
@RequestMapping(value = "/media/pagesize")
public class PageSizeController extends BaseController {
    @Autowired
    private TbpagesizeServiceI pagesizeServiceI;

    /**
     * 获取有效的版心数据列表
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbpagesize>>
     * @author songly
     * @date 2023/11/25 10:23
     */
    @GetMapping(value = "/getPageSizeList")
    public Json<List<Tbpagesize>> getPageSizeList() {
        List<Tbpagesize> pages = pagesizeServiceI.getPageSizeList();
        return Json.success(pages);
    }
}