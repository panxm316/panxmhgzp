/*
 * @Author: yanz
 * @Date: 2024-01-24 10:09:09
 * @LastEditors: yanz
 * @LastEditTime: 2024-03-06 14:44:43
 * @Description: 我的 - 我的数据相关api
 *
 */
import { TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { ITaskReportQuery } from '@/types/views/business/tasks'
import { TCustomerBelongVo, Twcustomeraccounts } from '@/types/views/customer/index'
import { TInvoiceQuery } from '@/types/views/finance/invoice'
import { TOrderItemCommissionVO } from '@/types/views/commission/commissioncalcu'
import request from '@/utils/request'

/**
 * 业绩统计
 */
export const getMyAchievementPageListApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyAchievementPageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取业绩汇总
 */
export const getMyAchievementCountApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyAchievementCount',
    method: 'get',
    params: data
  })
}
/**
 * 我的任务统计
 */
export const getMyTaskStatApi = (data: ITaskReportQuery) => {
  return request({
    url: '/personal/personalstat/getMyTaskStat',
    method: 'get',
    params: data
  })
}
/**
 * 我的客户统计
 */
export const getMyCustomerPageListApi = (data: TCustomerBelongVo) => {
  return request({
    url: '/personal/personalstat/getMyCustomerPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据客户id和时间范围获取订单明细列表
 */
export const getOrderItemListByCustomerIdApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getOrderItemListByCustomerId',
    method: 'get',
    params: data
  })
}
/**
 * 根据客户id获取客户账户列表
 */
export const getCustomerChargeByCustomeridApi = (data: Twcustomeraccounts) => {
  return request({
    url: '/personal/personalstat/getCustomerChargeByCustomerid',
    method: 'get',
    params: data
  })
}
/**
 * 根据客户id获取发票列表
 */
export const getInvoiceByCustomeridApi = (data: TInvoiceQuery) => {
  return request({
    url: '/personal/personalstat/getInvoiceByCustomerid',
    method: 'get',
    params: data
  })
}
/**
 * 根据客户id获取客户收费表中的客户收费记录汇总、剩余金额汇总
 * @param data
 */
export const getMyCustomerChargeCountApi = (data: Twcustomeraccounts) => {
  return request({
    url: '/personal/personalstat/getMyCustomerChargeCount',
    method: 'get',
    params: data
  })
}
/**
 * 我的佣金统计
 */
export const getMyCommissionPageListApi = (data: TOrderItemCommissionVO) => {
  return request({
    url: '/personal/personalstat/getMyCommissionPageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取佣金汇总
 */
export const getMyCommissionCountApi = (data: TOrderItemCommissionVO) => {
  return request({
    url: '/personal/personalstat/getMyCommissionCount',
    method: 'get',
    params: data
  })
}

/**
 * 我的订单(明细)汇总
 */
export const getMyOrderItemCountApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyOrderItemCount',
    method: 'get',
    params: data
  })
}

/**
 * 我的订单
 */
export const getMyOrderItemPageListApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyOrderItemPageList',
    method: 'get',
    params: data
  })
}

/**
 * 我的发票
 */
export const getMyInvoicePageListApi = (data: TInvoiceQuery) => {
  return request({
    url: '/personal/personalstat/getMyInvoicePageList',
    method: 'get',
    params: data
  })
}

/**
 * 我的实收 - 汇总
 */
export const getMyReceivedSummariesPageListApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyReceivedSummariesPageList',
    method: 'get',
    params: data
  })
}

/**
 * 我的实收 - 列表
 */
export const getMyReceivedPageListApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/personal/personalstat/getMyReceivedPageList',
    method: 'get',
    params: data
  })
}
