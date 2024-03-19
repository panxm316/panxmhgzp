/*
 * @Author: wanghl
 * @Date: 2023-11-08 13:34:56
 * @LastEditTime: 2023-12-21 14:44:06
 * @LastEditors: suny
 * @Description:预开发票申请相关API
 */

import {
  IPreInvoiceApplicationApprove,
  IPreInvoiceApplicationDTO,
  TPreInvoiceApplicationQuery
} from '@/types/views/business/preinvoiceapplication'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 预开发票申请分页查询
 * @param param
 * @returns
 */
export const getPreInvoiceApplicationPageListApi = (param: TPreInvoiceApplicationQuery) => {
  return request({
    url: '/business/twpreinvoiceapplication/getInvoiceApplicationPageList',
    method: 'get',
    params: param
  })
}
/**
 * 预开发票申请新增
 */
export const savePreinvoapplyAsPendingApi = (data: IPreInvoiceApplicationDTO) => {
  return request({
    url: '/business/twpreinvoiceapplication/savePreinvoapplyAsPending',
    method: 'post',
    data: data
  })
}

/**
 * 根据id获取预开票申请详情
 * @param id
 * @returns
 */
export const getPreinvoiceapplicationByIdApi = (id: string) => {
  return request({
    url: '/business/twpreinvoiceapplication/getPreinvoiceapplicationById',
    method: 'get',
    params: { id: id }
  })
}

/**
 * 修改预开发票申请
 * @param data
 * @returns
 */
export const updatePreinvoiceapplicationApi = (data: IPreInvoiceApplicationDTO) => {
  return request({
    url: '/business/twpreinvoiceapplication/updatePreinvoiceapplication',
    method: 'post',
    data: data
  })
}

/**
 * 删除预开发票申请
 * @param id
 * @returns
 */
export const deletePreinvoiceapplicationApi = (ids: string) => {
  return request({
    url: '/business/twpreinvoiceapplication/deletePreinvoiceapplication',
    method: 'post',
    data: qs.stringify({ ids: ids })
  })
}

export const approvePreinvoiceapplicationApi = (data: IPreInvoiceApplicationApprove) => {
  return request({
    url: '/business/twpreinvoiceapplication/approvePreinvoiceapplication',
    method: 'post',
    data: qs.stringify({ ids: data.ids, flowId: data.flowId })
  })
}
/**
 * （0-专用发票、2-普通发票、51-电子发票、81-数电专票、82-数电普票）
 */
export const getPreinvoiceStyleComboApi = () => {
  return request({
    url: '/enums/emnusset/getPreinvoiceStyleCombo',
    method: 'get'
  })
}
/**
 * 预开发票退回
 * @param data
 * @returns
 */
export const rejectInvoiceApi = (data: {
  preinvoiceapplicationId: string
  rejectReason: string
}) => {
  return request({
    url: '/business/twpreinvoiceapplication/rejectInvoice',
    method: 'post',
    data: qs.stringify(data)
  })
}
