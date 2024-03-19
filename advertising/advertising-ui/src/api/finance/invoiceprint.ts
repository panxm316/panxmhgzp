/*
 * @Author: suny
 * @Date: 2023-12-19 10:07:07
 * @LastEditTime: 2023-12-21 11:09:46
 * @LastEditors: suny
 * @Description: 发票打印相关类型
 */

import { TPreInvoiceQuery } from '@/types/views/business/preinvoiceapplication'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取预开发票打印查询分页列表
 * @param data
 * @returns
 */
export const getPreinvoicePageListApi = (data: TPreInvoiceQuery) => {
  return request({
    url: '/business/twpreinvoiceapplication/getPreinvoiceapplicationPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据预开发票id保存发票表接口，返回保存后的发票表id
 * @param data
 * @returns
 */
export const saveInvoiceByPreInvoApplyIdApi = (data: { preinvoiceapplicationId: string }) => {
  return request({
    url: '/finance/invoice/saveInvoiceByPreInvoApplyId',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 根据发票id获取打印信息
 * @returns
 */
export const getInvoiceByIdApi = (data: { id: string }) => {
  return request({
    url: '/finance/invoice/getInvoiceById',
    method: 'get',
    params: data
  })
}
/**
 * 打印发票返回的发票号保存到发票表
 * @param data
 * @returns
 */
export const saveInvoiceCodeApi = (data: {
  invoiceId: string
  invoice: string
  invoiceCode: string
}) => {
  return request({
    url: '/finance/invoice/saveInvoiceCode',
    method: 'post',
    data: qs.stringify(data)
  })
}
