package com.hgzp.advertising.controller.ad;

import com.hgzp.advertising.service.ad.TbadfromServiceI;
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
 TbadfromController
 创建人：songly
 类描述：广告来源 服务类
 创建日期：2024/3/7 13:43
 @folder ad/TbadfromController
 */
@RestController
@RequestMapping(value = "/ad/adfrom")
@Validated
public class TbadfromController extends BaseController {
    @Autowired
    private TbadfromServiceI tbAdFromService;
    /**
     * 获取来源树
     * 方法功能:   获取来源树
     * @author songly
     * @date 2024/3/7 13:46
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdFromTree")
    public Json<List<TreeModel>> getTbAdFromTree(TreeQueryVo queryInfo){
        List<TreeModel> tbAdFromListTree = tbAdFromService.getTbAdFromTree(queryInfo);
        return Json.success(tbAdFromListTree);
    }
}