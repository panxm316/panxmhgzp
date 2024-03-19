package com.hgzp.advertising.controller.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.CommissionRateGroupDTO;
import com.hgzp.advertising.service.price.TbcommissionrategroupServiceI;
import com.hgzp.advertising.service.price.TbcommissionrateitemServiceI;
import com.hgzp.advertising.service.price.TbdiscountreduceServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * CommissionRateGroupController
 * 创建人：suny
 * 类描述：计提比例总表 前端控制器
 * 创建日期：2023/11/24 10:17
 *
 * @folder price/CommissionRateGroupController
 */
@RestController
@RequestMapping("/price/commissionrategroup")
public class CommissionRateGroupController extends BaseController {
    @Autowired
    TbcommissionrategroupServiceI tbcommissionrategroupServiceI;
    @Autowired
    TbcommissionrateitemServiceI tbcommissionrateitemServiceI;
    @Autowired
    TbdiscountreduceServiceI tbdiscountreduceServiceI;

    /**
     * 根据查询条件获取计提比例总表分页列表
     * 方法功能: 根据查询条件获取计提比例总表分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbcommissionrategroup>>
     * @author suny
     * @date 2023/11/24 10:27
     */
    @GetMapping("/getCommissionRateGroupPageList")
    public Json<List<Tbcommissionrategroup>> getCommissionRateGroupPageList(PageRequest pageRequest,
                                                                            BaseQueryInfo query) throws Exception {
        Page<Tbcommissionrategroup> page = getPage(pageRequest);
        Page<Tbcommissionrategroup> commissionRateGroupPageList =
                tbcommissionrategroupServiceI.getCommissionRateGroupPageList(page, query);
        return Json.success(commissionRateGroupPageList);
    }

    /**
     * 新增保存、复制计提比例总表信息
     * 方法功能: 新增保存计提比例总表信息，或者复制保存计提比例总表信息，包括计提比例明细表和折扣下点表
     *
     * @param entiry
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbcommissionrategroup>
     * @author suny
     * @date 2023/11/24 10:27
     */
    @PostMapping("/saveCommissionRateGroup")
    public Json<Tbcommissionrategroup> saveCommissionRateGroup(@Validated(value = {ValidateParam.add.class}) @RequestBody CommissionRateGroupDTO entiry) throws Exception {
        Tbcommissionrategroup tbcommissionrategroup = new Tbcommissionrategroup();
        BeanUtils.copyProperties(entiry, tbcommissionrategroup);
        tbcommissionrategroupServiceI.saveCommissionRateGroup(tbcommissionrategroup);
        if (entiry.getCopyFlag()) {
            tbcommissionrateitemServiceI.SaveCopyCommissionRateItem(entiry.getId(), tbcommissionrategroup);
            tbdiscountreduceServiceI.SaveCopyDiscountReduce(entiry.getId(), tbcommissionrategroup);
        }
        return Json.success(tbcommissionrategroup);
    }

    /**
     * 修改保存计提比例总表信息
     * 方法功能: 修改保存计提比例总表信息
     *
     * @param entiry
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/24 10:28
     */
    @PostMapping("/updateCommissionRateGroup")
    public Json updateCommissionRateGroup(@Validated(value = {ValidateParam.edit.class}) @RequestBody CommissionRateGroupDTO entiry) throws Exception {
        Tbcommissionrategroup tbcommissionrategroup = new Tbcommissionrategroup();
        BeanUtils.copyProperties(entiry, tbcommissionrategroup);
        tbcommissionrategroupServiceI.updateCommissionRateGroup(tbcommissionrategroup);
        return Json.success();
    }

    /**
     * 删除计提比例总表信息
     * 方法功能: 删除计提比例总表信息
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/24 10:28
     */
    @PostMapping("/deleteCommissionRateGroup")
    public Json deleteCommissionRateGroup(@NotBlank(message = "计提比例id不能为空") String ids) {
        tbcommissionrategroupServiceI.deleteCommissionRateGroup(ids);
        return Json.success();
    }
}
