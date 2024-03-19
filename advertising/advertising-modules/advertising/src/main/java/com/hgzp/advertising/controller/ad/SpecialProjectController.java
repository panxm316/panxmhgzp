package com.hgzp.advertising.controller.ad;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.ad.SpecialProjectServiceI;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Twspecialproject;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 特刊项目 前端控制器
 * </p>
 *
 * @author muyn
 * @since 2024-03-13
 */
@Validated
@RestController
@RequestMapping("/ad/specialproject")
public class SpecialProjectController extends BaseController {

    @Autowired
    private SpecialProjectServiceI specialprojectService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;


    /**
     * 特刊项目保存
     * 方法功能: 特刊项目保存
     *
     * @param specialproject
     * @return com.hgzp.core.page.Json
     * @author lhl
     * @date 2024/3/13
     */
    @PostMapping(value = "/saveSpecialProject")
    public Json saveSpecialProject(@RequestBody Twspecialproject specialproject, HttpServletRequest request) throws Exception {
        if(specialprojectService.isExistSpecialProject(specialproject)) {
            return Json.fail("项目名称已存在");
        }
        specialprojectService.saveAdProject(specialproject, request);
        return Json.success();
    }

    /**
     * 分页查询特刊项目
     * 方法功能:  分页查询特刊项目
     *
     * @param pageRequeste
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twspecialproject>>
     * @author lhl
     * @date 2024/3/13
     */
    @GetMapping(value = "/getSpecialProjectPageList")
    public Json<List<Twspecialproject>> getSpecialProjectPageList(PageRequest pageRequeste, BaseQueryInfo query) {
        Page page = getPage(pageRequeste);
        IPage<Twspecialproject> adprojects = specialprojectService.getSpecialProjectPageList(page, query);
        return Json.success(adprojects);
    }

    /**
     * 修改特刊项目
     * 方法功能:  修改特刊项目
     *
     * @param specialproject
     * @return com.hgzp.core.page.Json
     * @author lhl
     * @date 2024/3/13
     */
    @PostMapping(value = "/updateSpecialProject")
    public Json updateSpecialProject(@RequestBody Twspecialproject specialproject) {
        if(specialprojectService.isExistSpecialProject(specialproject)) {
            return Json.fail("项目名称已存在");
        }
        innerInterceptor.recoredLog();
        specialprojectService.updateById(specialproject);
        return Json.success();
    }

    /**
     * 根据id删除特刊项目信息
     * 方法功能:  根据id删除特刊项目信息，支持","多选分割
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author lhl
     * @date 2024/3/13
     */
    @PostMapping(value = "/deleteSpecialProject")
    public Json deleteSpecialProject(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        String sRet= specialprojectService.deleteSpecialProject(ids);
        if(StrUtil.isNotBlank(sRet)) {
            return Json.fail(sRet);
        }else {
            return Json.success();
        }
    }

    /**
     * 获取特刊项目序号最大值
     * 方法功能: 获取特刊项目序号最大值
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author lhl
     * @date 2024/3/14
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = specialprojectService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 根据id查询特刊项目信息
     * 方法功能: 根据id查询特刊项目信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twspecialproject>
     * @author lhl
     * @date 2024/3/14
     */
    @GetMapping(value = "/getBySpecialProjectId")
    public Json<Twspecialproject> getBySpecialProjectId(@NotNull(message = "请传入需要查询的id") Long id) {
        Twspecialproject byId = specialprojectService.getById(id);
        return Json.success(byId);
    }

    /**
     * 特刊项目汇总
     * 方法功能: 特刊项目汇总
     *
     * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd
     * @return AdProjectCountVO
     * @author lhl
     * @date 2024/3/15
     */
    @GetMapping("/getSpecialProjectCountList")
    public Json<List<AdProjectCountVO>> getSpecialProjectCountList(String stratTime, String endTime, String adProjectId, int pageNum, int pageSize, String publistStatus, String queryKey, String projectEnd) {
        IPage<AdProjectCountVO> list = specialprojectService.getSpecialProjectCountList(stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd);
        return Json.success(list);
    }

}
