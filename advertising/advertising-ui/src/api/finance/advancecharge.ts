/*
 * @Author: suny
 * @Date: 2023-12-25 10:35:27
 * @LastEditTime: 2024-01-18 15:27:09
 * @LastEditors: suny suny@hgzp.com.cn
 * @Description: 预收款核销相关API
 */

import { TBankCustomerChargeQuery } from '@/types/views/business/bankaccountallocate'
import { TInvoiceBackHistoryVO } from '@/types/views/finance/invoicebackhistory'
import { TOrderApportiondetailVO } from '@/types/views/finance/orderapportiondetail'
import request from '@/utils/request'
import qs from 'qs'
/**
 * 查询收费表中 有余额的 客户预收费项目
 * @param param
 * @returns
 */
export const getCustomerChargeBankDTOListApi = (param: TBankCustomerChargeQuery) => {
  return request({
    url: '/finance/customercharge/getCustomerChargeBankDTOList',
    method: 'get',
    params: param
  })
}

/**
 * 根据客户id查询所有有欠款的订单的广告明细
 * @param param
 * @returns
 */
export const getOrderAndItemByCustomeridApi = (param: TBankCustomerChargeQuery) => {
  return request({
    url: '/ad/tworderitem/getOrderAndItemByCustomerid',
    method: 'get',
    params: param
  })
}
/**
 * 查看分摊详情列表
 * @param param
 * @returns
 */
export const getOrderApportiondetailPageListApi = (param: TOrderApportiondetailVO) => {
  return request({
    url: '/finance/orderapportiondetail/getOrderApportiondetailPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存预收款分摊明细
 * @param data
 * @returns
 */
export const saveOrderApportiondetailApi = (data: {
  customerchargeId: string
  orderitemIds: string
  dateString: string
}) => {
  return request({
    url: '/finance/orderapportiondetail/saveOrderApportiondetail',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 撤销指定的分摊明细
 * @param data
 * @returns
 */
export const revertWriteOffApi = (data: { orderApportiondetailIds: string; sdesc: string }) => {
  return request({
    url: '/finance/orderapportiondetail/revertWriteOff',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 查询回退历史列表
 * @param param
 * @returns
 */
export const getInvoiceBackHistoryPageListApi = (param: TInvoiceBackHistoryVO) => {
  return request({
    url: '/finance/orderapportiondetail/getInvoiceBackHistoryPageList',
    method: 'get',
    params: param
  })
}
/**
 * 根据订单明细id查询佣金列表
 * @param param
 * @returns
 */
export const getCommissionListByItemIdApi = (param: { orderitemid: string }) => {
  return request({
    url: '/commission/commissionmanage/getCommissionListByItemId',
    method: 'get',
    params: param
  })
}
