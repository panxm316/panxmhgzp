/*
 * @Author: suny
 * @Date: 2023-12-22 14:10:44
 * @LastEditTime: 2023-12-23 16:37:49
 * @LastEditors: suny
 * @Description: 发票核销相关API
 */
import { EStatus } from '@/types/common/enumindex'
import { TBankCustomerChargeQuery } from '@/types/views/business/bankaccountallocate'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取银行流水分配预开发票核销列表
 * @param param
 * @returns
 */
export const getInvoiceChargePageListApi = (param: TBankCustomerChargeQuery) => {
  return request({
    url: '/finance/customercharge/getInvoiceChargePageList',
    method: 'get',
    params: param
  })
}
/**
 * 预开发票分配审核
 * @param data
 * @returns
 */
export const submitConfirmationApi = (data: { id: string; iStatus: EStatus; comments: string }) => {
  return request({
    url: '/finance/customercharge/submitConfirmation',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 据预开申请id，获取预开申请详情列表
 * @param data
 * @returns
 */
export const getPreInvoiceApplyDtoByIdApi = (data: {
  preinvoiceapplicationId: string
  orderId: string
}) => {
  return request({
    url: '/business/twpreinvoiceapplication/getPreInvoiceApplyDtoById',
    method: 'get',
    params: data
  })
}
/**
 * 保存预开发票核销结果
 * @param data
 * @returns
 */
export const writeOffPreinvoiceapplicationApi = (data: {
  customerChargeId: string
  orderitemIds: string
  dateString: string
}) => {
  return request({
    url: '/finance/customercharge/writeOffPreinvoiceapplication',
    method: 'post',
    data: qs.stringify(data)
  })
}
