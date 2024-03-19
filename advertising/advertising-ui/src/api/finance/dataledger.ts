/*
 * @Author: suny
 * @Date: 2024-01-05 09:24:31
 * @LastEditTime: 2024-01-08 10:20:16
 * @LastEditors: yanz
 * @Description: 数据台账相关接口API
 */

import { TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import request from '@/utils/request'

/**
 * 订单台账分页查询
 * @param param
 * @returns
 */
export const getOrdersInPeriodApi = (param: TOrderAndItemVO) => {
  return request({
    url: '/ad/tworder/getOrdersInPeriod',
    method: 'get',
    params: param
  })
}
/**
 * 查询订单合同详情
 * @param param
 * @returns
 */
export const getOrderContractDetailApi = (data: { orderId: string }) => {
  return request({
    url: '/ad/tworder/getOrderContractDetail',
    method: 'get',
    params: data
  })
}
/**
 * 发票台账分页查询
 * @param param
 * @returns
 */
export const getInvoicesInPeriodApi = (param: TOrderAndItemVO) => {
  return request({
    url: '/finance/invoice/getInvoicesInPeriod',
    method: 'get',
    params: param
  })
}
/**
 * 查询订单合同详情
 * @param param
 * @returns
 */
export const getInvoiceDetailsByInvoiceApi = (data: { invoiceid: string }) => {
  return request({
    url: '/finance/invoice/getInvoiceDetailsByInvoice',
    method: 'get',
    params: data
  })
}
