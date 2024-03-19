package com.hgzp.advertising.controller.flow;

import com.hgzp.advertising.pagemodel.flow.TwapplicationrelationsVO;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.advertising.service.flow.TwapplicationrelationsServiceI;
import com.hgzp.core.model.Twapplicationrelations;
import org.springframework.web.bind.annotation.RestController;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import static com.hgzp.core.constant.ValidateParam.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * TwapplicationrelationsController
 * 创建人：wwk
 * 类描述：审批流程关联表 控制器
 * @author muyn
 * 创建日期：2023/12/05
 * @folder flow/TwapplicationrelationsController
 */
@Validated
@RestController
@RequestMapping("/flow/applicationrelations")
public class TwapplicationrelationsController  extends BaseController {

    @Autowired
    private TwapplicationrelationsServiceI twapplicationrelationsService;

    /**
     * 新增审批流程关联信息
     *
     * @author muyn
     * @since 2023-12-05
     * @param twapplicationrelationsVO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/saveapplicationRelations")
    public Json saveApplicationRelations(@RequestBody @Validated(add.class) TwapplicationrelationsVO twapplicationrelationsVO) {
        twapplicationrelationsService.saveApplicationRelations(twapplicationrelationsVO);
        return Json.success();
    }

    /**
     * 删除审批流程关联信息
     *
     * @author muyn
     * @since 2023-12-05
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/deleteapplicationRelations")
    public Json deleteApplicationRelations(@RequestBody @Validated(delete.class) TwapplicationrelationsDTO twapplicationrelationsDTO) {
        twapplicationrelationsService.deleteApplicationRelations(twapplicationrelationsDTO);
        return Json.success();
    }

    /**
     * 修改审批流程关联信息
     *
     * @author muyn
     * @since 2023-12-05
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @PostMapping("/updateapplicationRelations")
    public Json updateApplicationRelations(@RequestBody @Validated(edit.class) TwapplicationrelationsDTO twapplicationrelationsDTO) {
        twapplicationrelationsService.updateApplicationRelations(twapplicationrelationsDTO);
        return Json.success();
    }

    /**
     * 根据某些值查询审批流程关联信息
     *
     * @author muyn
     * @since 2023-12-05
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/getapplicationRelations")
    public Json getApplicationRelations(@RequestBody @Validated(detail.class) TwapplicationrelationsDTO twapplicationrelationsDTO) {
        return Json.success(twapplicationrelationsService.getApplicationRelations(twapplicationrelationsDTO));
    }

    /**
     * 根据的某些值查询审批流程关联信息列表
     *
     * @author muyn
     * @since 2023-12-05
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/getapplicationRelationslist")
    public Json getApplicationRelationslist(@RequestBody TwapplicationrelationsDTO twapplicationrelationsDTO) {
        return Json.success(twapplicationrelationsService.getApplicationRelationsList(twapplicationrelationsDTO));
    }

    /**
     * 根据某些值查询审批流程关联信息分页数据
     *
     * @author muyn
     * @since 2023-12-05
     * @param pageRequest 分页请求参数
     * @param twapplicationrelationsDTO 审批流程关联表数据传输对象
     * @return {@link Json}
     */
    @GetMapping("/getapplicationRelationsPageList")
    public Json getApplicationrelationsPageList(PageRequest pageRequest, @RequestBody TwapplicationrelationsDTO twapplicationrelationsDTO) {
        Page<Twapplicationrelations> page = getPage(pageRequest);
        return Json.success(twapplicationrelationsService.getApplicationrelationsPageList(page, twapplicationrelationsDTO));
    }

}
