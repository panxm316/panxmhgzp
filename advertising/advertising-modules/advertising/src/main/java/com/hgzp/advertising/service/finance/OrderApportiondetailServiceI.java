package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.finance.vo.OrderApportiondetailVO;
import com.hgzp.core.model.Tworderapportiondetail;

import java.util.List;

/**
 * <p>
 * 广告分摊明细表 服务类
 * </p>
 *
 * @author suny
 * @since 2023-12-18
 */
public interface OrderApportiondetailServiceI extends IService<Tworderapportiondetail> {

    /**
     * 查看分摊详情列表
     * 方法功能:根据收费表id，查询广告分摊表详情，使用 OrderApportiondetailDTO，包括以下字段：
     * 合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）
     * 注：有发票号时将按发票号匹配
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO>
     * @author yanz
     * @date 2023/12/25 16:42
     */
    IPage<OrderApportiondetailDTO> getOrderApportiondetailPageList(Page<Tworderapportiondetail> page, OrderApportiondetailVO query);

    /**
     * 分摊详情（核销数据）POJO转DTO
     * 方法功能:有查询操作，涉及order、orderitem、invoice表
     *
     * @param tworderapportiondetailList
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO>
     * @author yanz
     * @date 2024/1/5 14:30
     */
    List<OrderApportiondetailDTO> convertApportiondetailToDTOs(List<Tworderapportiondetail> tworderapportiondetailList);

    /**
     * 保存分摊
     * 方法功能:保存流程：
     * 1、按照时间正序对指定的ids中的广告详情分别进行分配
     * 2、每条分配添加一条tworderapportiondetail广告分摊明细记录，同时将分摊情况会写到orderitem表对应的记录，同时记录好分配出去的额度和剩余额度
     * 3、如果所有orderitem全部分配结束，仍有剩余，更新Twcustomercharge中的已用和剩余金额，不改状态标记
     * 4、如果Twcustomercharge表中的额度全部分配完，则更新Twcustomercharge中的已用和剩余金额，同时将标记更新为已核销
     *
     * @param customerChargeId
     * @param orderitemIds
     * @param dateString
     * @return void
     * @author yanz
     * @date 2023/12/25 16:57
     */
    void saveOrderApportiondetail(Long customerChargeId, List<Long> orderitemIds, String dateString) throws Exception;

    /**
     * 退回分摊
     * 方法功能:流程：根据分摊表ids，找到需要回退的分摊表list——》每一条分摊需要根据orderitemid，
     * 找到对应的订单详情，将分摊结果回退，修改已收、欠款字段——》根据收费表id，找到收费表数据，将状态、已用金额、剩余金额分别回退
     *
     * @param apportiondetailIdList
     * @param sdesc                 回退原因
     * @return void
     * @author yanz
     * @date 2023/12/26 13:22
     */
    void revertWriteOff(List<Long> apportiondetailIdList, String sdesc) throws Exception;
}
