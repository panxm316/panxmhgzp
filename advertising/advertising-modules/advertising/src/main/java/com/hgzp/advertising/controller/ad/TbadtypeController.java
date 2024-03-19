package com.hgzp.advertising.controller.ad;

/**
 * @author new wei
 * @date 2023/12/5 16:09
 */

import com.hgzp.advertising.service.ad.TbadtypeServiceI;
import com.hgzp.advertising.service.ad.impl.TbadtypeServiceImpl;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TbadtypeController
 * 创建人：songly
 * 类描述：广告类型 服务类
 * 创建日期：2023/12/5 16:09
 *
 * @folder ad/TbadtypeController
 */
@RestController
@RequestMapping("/ad/adtype")
@Validated
public class TbadtypeController extends BaseController {
    @Autowired
    TbadtypeServiceI tbadtypeServiceI;

    /**
     * 获取广告类型列表
     * 方法功能: 获取广告类型列表
     *
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbadtype>>
     */
    @GetMapping(value = "/getadtypelist")
    public Json<List<Tbadtype>> getAdTypeList() {
        try {
            List<Tbadtype> lsAdType = tbadtypeServiceI.getAdTypeList();
            return Json.success(lsAdType);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }
}