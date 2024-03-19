package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.pagemodel.finance.dto.CommissionDTO;
import com.hgzp.core.model.Twcommission;

import java.util.List;

/**
 * <p>
 * 佣金计提明细表 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-01-06
 */
public interface CommissionServiceI extends IService<Twcommission> {

    /**
     * 手动新增保存或修改佣金计提信息
     * 方法功能: 手动新增保存或修改佣金计提信息
     *
     * @param commissionDTO
     * @return void
     * @author suny
     * @date 2024/3/18 10:39
     */
    void saveOrUpdata(CommissionDTO commissionDTO) throws Exception;

    /**
     * 获取佣金列表
     * 方法功能: 根据条件获取佣金分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>
     * @author suny
     * @date 2024/1/16 10:02
     */
    IPage<OrderItemCommissionDTO> getCommissionList(Page<Twcommission> page, OrderItemCommissionVO query) throws Exception;

    /**
     * 保存佣金计提状态和说明信息
     * 方法功能: 保存佣金计提状态和说明信息
     * （20240307 suny 改成批量更新）
     *
     * @param entitylist
     * @return void
     * @author suny
     * @date 2024/1/16 14:01
     */
    void saveCommission(List<OrderItemCommissionDTO> entitylist) throws Exception;

    /**
     * 根据id删除佣金计提信息
     * 方法功能:  根据id删除佣金计提信息
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2024/1/16 14:11
     */
    void deleteCommission(String ids) throws Exception;

    /**
     * 新增或更新佣金计提信息
     * 方法功能: 新增或更新佣金计提信息
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2024/1/18 15:23
     */
    void saveOrUpdateCommission(OrderItemCommissionDTO entity) throws Exception;

    /**
     * 更新佣金计提信息客户信息
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/18 9:21
     */
    void updateCommissionCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据订单刊期id查询佣金计提列表
     * 方法功能: 根据订单刊期id查询佣金计提列表
     *
     * @param orderitemid
     * @return java.util.List<com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO>
     * @author suny
     * @date 2024/1/18 15:24
     */
    List<OrderItemCommissionDTO> getCommissionListByItemId(Long orderitemid) throws Exception;

    /**
     * 冲抵或删除佣金计提信息
     * 方法功能: 冲抵或删除佣金计提信息
     *
     * @param orderitemid
     * @return void
     * @author suny
     * @date 2024/1/18 16:13
     */
    void offsetCommission(Long orderitemid, String sdesc) throws Exception;

    /**
     * 根据查询条件获取佣金汇总
     * 方法功能: 根据查询条件获取佣金汇总
     *
     * @param vo
     * @return com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO
     * @author suny
     * @date 2024/2/1 9:12
     */
    OrderItemCommissionDTO getMyCommissionCount(OrderItemCommissionVO vo) throws Exception;

    /**
     * 查询手动添加的佣金计提列表
     * 方法功能:   查询手动添加的佣金计提列表
     *
     * @param page
     * @param query
     * @param type
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twcommission>
     * @author suny
     * @date 2024/3/18 10:42
     */
    IPage<Twcommission> getCommissionListByAddType(Page<Twcommission> page, OrderItemCommissionVO query, String type) throws Exception;
}
