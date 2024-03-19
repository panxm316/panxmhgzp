package com.hgzp.advertising.controller.flow;

import cn.hutool.core.util.ObjectUtil;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * FlowController
 * 创建人：songly
 * 类描述：广告订单表 前端控制器
 * 创建日期：2024/03/08
 *
 * @folder flow/FlowController
 */
@Validated
@RestController
@RequestMapping("/flowset")
public class FlowController extends BaseController {
    @Autowired
    private TbflowServiceI flowServiceI;

    /**
     * 根据流程key获取流程设置
     * 方法功能:
     *
     * @param flowKey
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbflow>
     * @author songly
     * @date 2024/2/16 11:11
     */
    @GetMapping(value = "/getFlowTypeByKey")
    public Json<Tbflow> getFlowTypeByKey(@NotNull(message = "flowKey不可为空") String flowKey) {
        Tbflow flowInfo = flowServiceI.getFlowTypeByKey(flowKey);
        return ObjectUtil.isNotNull(flowInfo) ? Json.success(flowInfo) : Json.fail();
    }

    /**
     * 获取流程设置列表
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Tbflow>>
     * @author songly
     * @date 2024/3/8 10:19
     */
    @GetMapping(value = "/getFlowTypeList")
    public Json<List<Tbflow>> getFlowTypeList() {
        List<Tbflow> flowInfo = flowServiceI.getFlowTypeList();
        return ObjectUtil.isNotNull(flowInfo) ? Json.success(flowInfo) : Json.fail();
    }
}
