package com.hgzp.advertising.controller.price;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.price.dto.DiscountReduceDTO;
import com.hgzp.advertising.pagemodel.price.vo.DiscountReduceVO;
import com.hgzp.advertising.service.price.TbdiscountreduceServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbdiscountreduce;
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
 * DiscountReduceController
 * 创建人：suny
 * 类描述：折扣下点 前端控制器
 * 创建日期：2023/11/30 15:00
 *
 * @folder price/DiscountReduceController
 */
@RestController
@RequestMapping("/price/discountreduce")
public class DiscountReduceController extends BaseController {
    @Autowired
    TbdiscountreduceServiceI tbdiscountreduceServiceI;

    /**
     * 根据查询条件获取折扣下点分页列表
     * 方法功能:  根据查询条件获取折扣下点分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbdiscountreduce>>
     * @author suny
     * @date 2023/11/30 15:11
     */
    @GetMapping("/getDiscountReducePageList")
    public Json<List<Tbdiscountreduce>> getDiscountReducePageList(PageRequest pageRequest, DiscountReduceVO query) throws Exception {
        Page<Tbdiscountreduce> page = getPage(pageRequest);
        Page<Tbdiscountreduce> discountReducePageList = tbdiscountreduceServiceI.getDiscountReducePageList(page, query);
        return Json.success(discountReducePageList);
    }

    /**
     * 新增保存折扣下点
     * 方法功能:  新增保存折扣下点
     *
     * @param entiry
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbdiscountreduce>
     * @author suny
     * @date 2023/11/30 15:11
     */
    @PostMapping("/saveDiscountReduce")
    public Json<Tbdiscountreduce> saveDiscountReduce(@Validated(value = {ValidateParam.add.class}) @RequestBody DiscountReduceDTO entiry) throws Exception {
        Tbdiscountreduce tbdiscountreduce = new Tbdiscountreduce();
        BeanUtils.copyProperties(entiry, tbdiscountreduce);
        tbdiscountreduceServiceI.saveDiscountReduce(tbdiscountreduce);
        return Json.success(tbdiscountreduce);
    }

    /**
     * 修改保存折扣下点
     * 方法功能: 修改保存折扣下点
     *
     * @param entiry
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/30 15:11
     */
    @PostMapping("/updateDiscountReduce")
    public Json updateDiscountReduce(@Validated(value = {ValidateParam.edit.class}) @RequestBody DiscountReduceDTO entiry) throws Exception {
        Tbdiscountreduce tbdiscountreduce = new Tbdiscountreduce();
        BeanUtils.copyProperties(entiry, tbdiscountreduce);
        tbdiscountreduceServiceI.updateDiscountReduce(tbdiscountreduce);
        return Json.success();
    }

    /**
     * 删除折扣下点数据
     * 方法功能: 删除折扣下点数据
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/30 15:11
     */
    @PostMapping("/deleteDiscountReduce")
    public Json deleteDiscountReduce(@NotBlank(message = "计提比例明细id不能为空") String ids) {
        tbdiscountreduceServiceI.deleteDiscountReduce(ids);
        return Json.success();
    }
}

