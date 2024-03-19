package com.hgzp.advertising.controller.finance;

import com.hgzp.advertising.pagemodel.finance.dto.CommissionDTO;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hgzp.core.constant.ValidateParam.add;

/**
 * <p>
 * 佣金计提明细表 前端控制器
 * </p>
 *
 * @author wangxk
 * @since 2024-01-06
 */
@Validated
@RestController
@RequestMapping("/twcommission")
public class CommissionController extends BaseController {

}
