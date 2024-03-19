package com.hgzp.advertising.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderReq;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsReq;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsVO;
import com.hgzp.advertising.service.system.OpinionsServiceI;
import com.hgzp.core.model.Twopinions;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.add;

/**
 * OpinionsController
 * 创建人：wwk
 * 类描述：广告订单表 前端控制器
 * 创建日期：2023/11/15
 *
 * @folder system/OpinionsController
 */
@Validated
@RestController
@RequestMapping("/system/opinions")
public class OpinionsController extends BaseController {

    @Autowired
    private OpinionsServiceI opinionsService;

    /**
     * 获取常用审批意见分页列表
     * 方法功能:
     *
     * @param pageRequest
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.system.vo.OpinionsVO>>
     * @author songly
     * @date 2024/3/8 9:36
     */
    @GetMapping("/getOpinionsPageList")
    public Json<List<OpinionsVO>> getOpinionsPageList(PageRequest pageRequest, OpinionsReq queryInfo) {
        try {
            Page<Twopinions> page = getPage(pageRequest);
            IPage<OpinionsVO> pages = opinionsService.getOpinionsPageList(page, queryInfo);
            return Json.success(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取常用审批意见列表
     * 方法功能:
     *
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.system.vo.OpinionsVO>>
     * @author songly
     * @date 2024/3/8 9:39
     */
    @GetMapping("/getOpinionsList")
    public Json<List<OpinionsVO>> getOpinionsList(OpinionsReq queryInfo) {
        try {
            List<OpinionsVO> lsdata = opinionsService.getOpinionsList(queryInfo);
            return Json.success(lsdata);
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 根据Id获取常用审批意见
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.system.vo.OpinionsVO>
     * @author songly
     * @date 2024/3/8 9:44
     */
    @GetMapping(value = "/getopinionsById")
    public Json<OpinionsVO> getOpinionsById(@NotNull(message = "请传入需要查询的id") String id) {
        OpinionsVO byId = opinionsService.getById(Long.valueOf(id));
        return Json.success(byId);
    }

    /**
     * 新增Twopinions
     *
     * @param twopinions 常用审批意见数据传输对象
     * @return {@link Json}
     * @author muyn
     * @since 2024-03-07
     */
    @PostMapping("/saveOpinions")
    public Json save(@RequestBody @Validated(add.class) Twopinions twopinions) {
        try {
            opinionsService.saveOpinions(twopinions);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 更新常用审批意见
     * 方法功能:
     *
     * @param twopinions
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/8 10:00
     */
    @PostMapping("/updateOpinions")
    public Json updateOpinions(@RequestBody @Validated(add.class) Twopinions twopinions) {
        try {
            opinionsService.updateOpinions(twopinions);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除常用审批意见
     * 方法功能:
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/8 10:00
     */
    @PostMapping("/deleteOpinions")
    public Json deleteOpinions(@NotNull(message = "请传入需要删除的id") String ids) {
        try {
            opinionsService.deleteOpinions(ids);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取最大序号
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/8 9:59
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = opinionsService.getMaxSort();
        return Json.success(maxSort);
    }
}
