package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.dto.ParameterDTO;
import com.hgzp.advertisingsys.service.system.TwparameterServiceI;
import com.hgzp.core.model.Twparameter;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author muyn
 * @since 2024-02-23
 */
@Validated
@RestController
@RequestMapping("/system/twparameter")
public class ParameterController extends BaseController {

    @Autowired
    private TwparameterServiceI parameterService;


    /***
     *
     * 方法功能:保存Twparameter
     * @author lijp
     * @date 2024/3/7 13:04
     * @param parameterDTO
     * @return com.hgzp.core.page.Json
     */

    @PostMapping("/saveTwparameter")
    public Json save(@RequestBody @Validated(add.class) ParameterDTO parameterDTO) {
        parameterService.saveParm(parameterDTO);
        return Json.success();
    }

    /***
     *
     * 方法功能:删除 Twparameter
     * @author lijp
     * @date 2024/3/7 13:03
     * @param parameterDTO
     * @return com.hgzp.core.page.Json
     */

    @PostMapping("/deleteTwparameter")
    public Json delete(@RequestBody @Validated(delete.class) ParameterDTO parameterDTO) {
        LambdaQueryWrapper<Twparameter> twp = Wrappers.lambdaQuery();
        twp.eq(Twparameter::getId, parameterDTO.getId());
        parameterService.removeById(parameterDTO.getId());
        return Json.success();
    }

    /***
     *
     * 方法功能: 更新Twparameter
     * @author lijp
     * @date 2024/3/7 13:01
     * @param parameterDTO
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateTwparameter")
    public Json update(@RequestBody @Validated(edit.class) ParameterDTO parameterDTO) {
        parameterService.updateParm(parameterDTO);
        return Json.success();
    }

    /***
     *
     * 方法功能:查询Twparameter
     * @author lijp
     * @date 2024/3/15 16:02
     * @param pageRequest
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Twparameter>>
     */

    @GetMapping("/getParameterPageList")
    public Json<List<Twparameter>> getParameterPageList(PageRequest pageRequest, BaseQueryInfo queryInfo) throws Exception {
        Page page = getPage(pageRequest);
        IPage<Twparameter> parameterPageList = parameterService.getParameterPageList(page, queryInfo);
        return Json.success(parameterPageList);
    }
}
