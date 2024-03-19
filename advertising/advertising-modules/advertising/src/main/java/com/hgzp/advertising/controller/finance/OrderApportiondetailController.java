package com.hgzp.advertising.controller.finance;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.finance.vo.OrderApportiondetailVO;
import com.hgzp.advertising.service.finance.InvoiceBackHistoryServiceI;
import com.hgzp.advertising.service.finance.OrderApportiondetailServiceI;
import com.hgzp.core.model.Twinvoicebackhistory;
import com.hgzp.core.model.Tworderapportiondetail;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderApportiondetailController
 * 创建人：suny
 * 类描述：广告分摊明细表 前端控制器
 * 创建日期：2023/12/18 12:48
 *
 * @folder finance/OrderApportiondetailController
 */
@Validated
@RestController
@RequestMapping("/finance/orderapportiondetail")
public class OrderApportiondetailController extends BaseController {

    @Autowired
    private OrderApportiondetailServiceI orderApportiondetailService;
    @Autowired
    private InvoiceBackHistoryServiceI invoiceBackHistoryServiceI;

    /**
     * 查看分摊详情列表
     * 方法功能:根据收费表id，查询广告分摊表详情，使用 OrderApportiondetailDTO，包括以下字段：
     * 合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）
     * 注：有发票号时将按发票号匹配
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO>>
     * @author yanz
     * @date 2023/12/26 8:37
     */
    @GetMapping("/getOrderApportiondetailPageList")
    public Json<List<OrderApportiondetailDTO>> getOrderApportiondetailPageList(PageRequest pageRequest, OrderApportiondetailVO query) {
        Page<Tworderapportiondetail> page = getPage(pageRequest);
        IPage<OrderApportiondetailDTO> orderApportiondetailPageList = orderApportiondetailService.getOrderApportiondetailPageList(page, query);
        return Json.success(orderApportiondetailPageList);
    }

    /**
     * 保存分摊
     * 方法功能:保存流程：
     * 1、按照时间正序对指定的ids中的广告详情分别进行分配
     * 2、每条分配添加一条tworderapportiondetail广告分摊明细记录，同时将分摊情况会写到orderitem表对应的记录，同时记录好分配出去的额度和剩余额度
     * 3、如果所有orderitem全部分配结束，仍有剩余，更新Twcustomercharge中的已用和剩余金额，不改状态标记
     * 4、如果Twcustomercharge表中的额度全部分配完，则更新Twcustomercharge中的已用和剩余金额，同时将标记更新为已核销
     *
     * @param customerchargeId
     * @param orderitemIds     英文逗号分隔
     * @param dateString       传入分摊日期（字符串），yyyy-MM-dd 格式
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/25 16:55
     */
    @PostMapping("/saveOrderApportiondetail")
    public Json saveOrderApportiondetail(Long customerchargeId, String orderitemIds, String dateString) throws Exception {
        List<Long> idList = CollUtil.newArrayList(orderitemIds.split(",")).stream().map(Long::valueOf).distinct().collect(Collectors.toList());
        orderApportiondetailService.saveOrderApportiondetail(customerchargeId, idList, dateString);
        return Json.success();
    }

    /**
     * 退回分摊
     * 方法功能:退回分摊，将分摊的记录恢复为未分摊状态
     *
     * @param orderApportiondetailIds 英文逗号分隔
     * @param sdesc                   回退原因
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/26 13:21
     */
    @PostMapping("/revertWriteOff")
    public Json revertWriteOff(String orderApportiondetailIds, String sdesc) throws Exception {
        List<Long> ids = CollUtil.newArrayList(orderApportiondetailIds.split(",")).stream().map(Long::valueOf).distinct().collect(Collectors.toList());
        orderApportiondetailService.revertWriteOff(ids, sdesc);
        return Json.success();
    }

    /**
     * 查询回退历史列表
     * 方法功能:参数：日期范围、合同号、发票号（这两个号都用queryKey传递）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.InvoiceBackHistoryDTO>>
     * @author yanz
     * @date 2023/12/27 14:26
     */
    @GetMapping("/getInvoiceBackHistoryPageList")
    public Json<List<InvoiceBackHistoryDTO>> getInvoiceBackHistoryPageList(PageRequest pageRequest, BaseQueryInfo query) {
        Page<Twinvoicebackhistory> page = getPage(pageRequest);
        IPage<InvoiceBackHistoryDTO> pageList = invoiceBackHistoryServiceI.getInvoiceBackHistoryPageList(page, query);
        return Json.success(pageList);
    }


}
