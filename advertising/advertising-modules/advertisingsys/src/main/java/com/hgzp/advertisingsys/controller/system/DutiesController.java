package com.hgzp.advertisingsys.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.system.vo.DutiesVO;
import com.hgzp.advertisingsys.service.system.TbdutiesServiceI;
import com.hgzp.core.model.Tbduties;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * DutiesController
 * 创建人：yanz
 * 类描述： 人员职务 Controller
 * 创建日期：2023/8/16 17:12
 *
 * @folder system/DutiesController
 */
@Validated
@RestController
@RequestMapping("/system/duties")
public class DutiesController extends BaseController {
    @Autowired
    private TbdutiesServiceI serviceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 据id获取人员职务
     * 方法功能:据id获取人员职务
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbduties>
     * @author yanz
     * @date 2023/8/17 15:58
     */
    @GetMapping(value = "/getDutyById")
    public Json<Tbduties> getDutyById(@NotNull(message = "ID不可为空") String id) {
        Tbduties duty = serviceI.getById(id);
        return ObjectUtil.isNotNull(duty) ? Json.success(duty) : Json.fail();
    }

    /**
     * 获取人员职务
     * 方法功能:分页：获取人员职务
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbduties>>
     * @author yanz
     * @date 2023/8/31 15:34
     */
    @GetMapping(value = "/getDutyPageList")
    public Json<List<Tbduties>> getDutyPageList(PageRequest pageRequest, DutiesVO vo) {
        Page page = getPage(pageRequest);
        IPage<Tbduties> pages = serviceI.getDutyPageList(page, vo);
        return Json.success(pages);
    }


    /**
     * 修改人员职务
     * 方法功能:修改人员职务
     *
     * @param duty
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/18 8:57
     */
    @PostMapping(value = "/updateDuty")
    public Json updateDuty(@RequestBody Tbduties duty) {
        if (serviceI.isDuplicateSname(duty.getId(), duty.getSname())) {
            return Json.fail("存在同名类型");
        }
        innerInterceptor.recoredLog();
        return serviceI.updateById(duty) ? Json.success() : Json.fail();
    }

    /**
     * 根据id删除人员职务
     * 方法功能:根据id删除人员职务
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/25 10:18
     */
    @PostMapping(value = "/deleteDutyById")
    public Json deleteDutyById(@NotNull(message = "ID不可为空") String ids) {
        innerInterceptor.recoredLog();
        if (serviceI.removeByIds(Arrays.asList(ids.split(",")))) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 添加人员职务
     * 方法功能:添加人员职务
     *
     * @param duty
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/8/17 16:27
     */
    @PostMapping(value = "/saveDuty")
    public Json saveDuty(@RequestBody Tbduties duty) {
        if (serviceI.isDuplicateSname(duty.getId(), duty.getSname())) {
            return Json.fail("存在同名类型");
        }
        innerInterceptor.recoredLog();
        return serviceI.save(duty) ? Json.success() : Json.fail();
    }

    /**
     * 获取职务最大序号
     * 方法功能:获取职务最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author yanz
     * @date 2023/8/29 15:02
     */
    @GetMapping("/getDutyMaxSort")
    public Json<Integer> getDutyMaxSort() {
        Integer maxSort = serviceI.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 获取人员职务下拉列表
     * 方法功能:获取人员职务下拉列表（PS: Tbduties 没有 default 属性，按 iSort 从小到大）
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author yanz
     * @date 2023/9/1 11:18
     */
    @GetMapping(value = "/getDutiesCombo")
    public Json<List<DataCombo>> getDutiesCombo() throws Exception {
        List<DataCombo> list = serviceI.getDutiesCombo();
        return Json.success(list);
    }
}
