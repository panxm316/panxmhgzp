package com.hgzp.advertising.controller.system;

import com.hgzp.advertising.service.system.TbadindustryServiceI;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 AdIndustryController
 创建人：songly
 类描述：行业
 创建日期：2023/10/28 9:13
 @folder system/AdIndustryController
 */
@Validated
@RestController
@RequestMapping("/system/adindustry")
public class AdIndustryController extends BaseController {
    @Autowired
    TbadindustryServiceI adindustryServiceI;

    /**
     * 获取有效行业下拉数据
     * 方法功能:获取有效行业下拉数据
     * @author hgsongly
     * @date 2023/10/28 9:18
     * @param
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping("/getAdIndustryTree")
    public Json<List<TreeModel>> getAdIndustryCombo()  {
        List<TreeModel> dataCombo=adindustryServiceI.getTbAdIndustryTree();
        return Json.success(dataCombo);
    }
}