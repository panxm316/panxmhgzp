package com.hgzp.advertising.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Tworderitemfiles;

import java.util.List;

/**
 * <p>
 * 订单明细文件表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TworderitemfilesServiceI extends IService<Tworderitemfiles> {
    /**
     * 获取订单明细文件
     * 方法功能: 获取订单明细文件
     *
     * @param strdate      日期
     * @param scontractnum 合同号
     * @return java.util.List<com.hgzp.core.model.Tworderitemfiles>
     * @author suny
     * @date 2024/2/29 16:09
     */
    List<Tworderitemfiles> getFilesByOrderItemIdForCJ(String strdate, String scontractnum);
}
