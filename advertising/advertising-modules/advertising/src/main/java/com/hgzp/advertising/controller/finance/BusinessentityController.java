package com.hgzp.advertising.controller.finance;

import com.hgzp.advertising.service.finance.TbbusinessentityServiceI;
import com.hgzp.core.model.Tbbusinessentity;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * BusinessentityController
 * 创建人：CGD
 * 类描述：经营主体查询Controller
 * 创建日期：2023/10/30 11:21
 *
 * @folder finance/BusinessentityController
 */
@Validated
@RestController
@RequestMapping("/finance/businessentity")
public class BusinessentityController extends BaseController {
    @Autowired
    private TbbusinessentityServiceI tbbusinessentityService;

    /**
     * 经营主体下拉
     * 方法功能:  经营主体下拉值较多一并返回
     * @author CGD
     * @date 2023/10/30 12:47
     * @param
     * @return java.util.List<com.hgzp.core.model.Tbbusinessentity>
     */
    @RequestMapping("/getBusinessentityCombo")
    public Json<List<Tbbusinessentity>> getBusinessentityCombo (){
        List<Tbbusinessentity> list = tbbusinessentityService.lambdaQuery()
                .eq(Tbbusinessentity::getBuse, true)
                .orderByAsc(Tbbusinessentity::getIsort).list();

        return Json.success(list);
    }
}

