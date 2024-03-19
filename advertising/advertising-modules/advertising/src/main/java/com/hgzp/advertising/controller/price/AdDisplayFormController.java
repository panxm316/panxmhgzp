package com.hgzp.advertising.controller.price;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.dto.AdDisplayFormDTO;
import com.hgzp.advertising.pagemodel.price.vo.AdDisplayFormVO;
import com.hgzp.advertising.service.price.TbaddisplayformServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Tbaddisplayform;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * FoldController
 * 创建人：suny
 * 类描述：广告形式controller
 * 创建日期：2023/11/06 10:09
 *
 * @folder price/AdDisplayFormController
 */
@Validated
@RestController
@RequestMapping("/price/addisplayform")
public class AdDisplayFormController extends BaseController {
    @Autowired
    TbaddisplayformServiceI tbaddisplayformServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 获取广告形式分页列表
     * 方法功能:根据查询条件获取广告形式分页列表
     *
     * @param pageRequest
     * @param vo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbaddisplayform>>
     * @author suny
     * @date 2023/11/6 10:24
     */
    @GetMapping(value = "/getAdDisplayFormPageList")
    public Json<List<Tbaddisplayform>> getAdDisplayFormPageList(PageRequest pageRequest, AdDisplayFormVO vo) throws Exception {
        Page page = getPage(pageRequest);
        IPage<Tbaddisplayform> pages = tbaddisplayformServiceI.getAdDisplayFormPageList(page, vo);
        return Json.success(pages);
    }

    /**
     * 新增保存广告形式
     * 方法功能: 新增保存广告形式
     *
     * @param displayFormDTO
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/6 10:25
     */
    @PostMapping(value = "/saveAdDisplayForm")
    public Json saveAdDisplayForm(@Validated(value = {ValidateParam.add.class}) @RequestBody AdDisplayFormDTO displayFormDTO) {
        Tbaddisplayform tbaddisplayform = new Tbaddisplayform();
        BeanUtils.copyProperties(displayFormDTO, tbaddisplayform);
        tbaddisplayformServiceI.saveAdDisplayForm(tbaddisplayform);
        return Json.success();
    }

    /**
     * 修改保存广告形式
     * 方法功能: 修改保存广告形式
     *
     * @param displayFormDTO
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/6 10:25
     */
    @PostMapping(value = "/updateAdDisplayForm")
    public Json updateAdDisplayForm(@Validated(value = {ValidateParam.edit.class}) @RequestBody AdDisplayFormDTO displayFormDTO) {
        Tbaddisplayform newAdDisplayForm = new Tbaddisplayform();
        BeanUtils.copyProperties(displayFormDTO, newAdDisplayForm);
        tbaddisplayformServiceI.updateAdDisplayForm(newAdDisplayForm);
        return Json.success();
    }

    /**
     * 删除广告形式
     * 方法功能: 删除广告形式
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/11/6 10:25
     */
    @PostMapping(value = "/deleteAdDisplayFormById")
    public Json deleteAdDisplayFormById(@NotNull(message = "ID不可为空") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        if (tbaddisplayformServiceI.removeByIds(idList)) {
            return Json.success();
        } else {
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 获取广告形式最大序号
     * 方法功能:  获取广告形式最大序号
     *
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     * @author suny
     * @date 2023/11/6 12:40
     */
    @GetMapping("/getAdDisplayFormMaxSort")
    public Json<Integer> getAdDisplayFormMaxSort() {
        Integer maxSort = tbaddisplayformServiceI.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 根据媒体类型获取广告形式树
     * 方法功能:  根据媒体类型获取广告形式树
     *
     * @param mediaType
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author CGD
     * @date 2023/11/13 9:01
     */
    @GetMapping("/getAdDisplayFormTreeList")
    public Json<List<TreeModel>> getAdDisplayFormTreeList(String mediaType) {
        return Json.success(tbaddisplayformServiceI.getAdDisplayFormTreeList(mediaType));
    }

    /**
     * 根据媒体类型获取可用的广告形式列表,已排序
     *
     * @param mediaType 媒体类型
     * @return {@link List<Tbaddisplayform>}
     * @author wangxk
     * @since 2023-12-12
     */
    @GetMapping("/listUsableAdDisplayForm")
    public Json<List<Tbaddisplayform>> listUsableAdDisplayForm(String mediaType) {
        return Json.success(tbaddisplayformServiceI.listUsableAdDisplayForm(mediaType));
    }
}
