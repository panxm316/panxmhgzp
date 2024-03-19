/*
 * @Author: wanghl
 * @Date: 2023-12-01 12:51:56
 * @LastEditTime: 2024-01-29 09:51:36
 * @LastEditors: wanghl
 * @Description:广告预定
 */

import request from '@/utils/request'
import { QTworder, Tworder, TworderCustomer, Tworderitem } from '@/types/views/ad/adtworder'
import type { TCustomerApprove } from '@/types/views/customer'
import qs from 'qs'
import { PreOrder } from '@/types/views/ad/pre-order'

/**
 * 获取广告订单分页列表
 */
export const getorderPagelistApi = (data: QTworder) => {
  return request({
    url: '/ad/tworder/getorderPagelist',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取广告信息
 */
export const getorderByIdApi = (id: string) => {
  return request({
    url: '/ad/tworder/getorderById',
    method: 'get',
    params: { id: id }
  })
}
/**
 * 获取指定业务员+客户的所有合同信息
 */
export const getContractNumByEmployIdAndCustomerIdApi = (data: TworderCustomer) => {
  return request({
    url: '/ad/tworder/getContractNumByEmployIdAndCustomerId',
    method: 'get',
    params: data
  })
}
/**
 * 保存广告信息
 * @param data
 * @returns
 */
export const saveorderApi = (data: Tworder) => {
  return request({
    url: '/ad/tworder/saveorder',
    method: 'post',
    data: data
  })
}
/**
 * 修改广告项目
 * @param data
 * @returns
 */
export const updateorderApi = (data: Tworder) => {
  return request({
    url: '/ad/tworder/updateorder',
    method: 'post',
    data: data
  })
}
/**
 * 删除广告项目
 */
export const deleteorderApi = (data: string) => {
  return request({
    url: '/ad/tworder/deleteorder',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * （0-预约、1-加刊、2-改刊、3-停刊、4-折扣）
 */
export const getApproveTypeComboApi = () => {
  return request({
    url: '/enums/emnusset/getApproveTypeCombo',
    method: 'get'
  })
}
/**
 * 折扣审批申请
 * @param data
 * @returns
 */
export const discountApproveOrderApi = (data: TCustomerApprove) => {
  return request({
    url: '/ad/tworder/discountApproveOrder',
    method: 'post',
    data: qs.stringify({ id: data.id, flowId: data.flowId })
  })
}
/**
 * 订单审批申请
 * @param data
 * @returns
 */
export const approveOrderApi = (data: TCustomerApprove) => {
  return request({
    url: '/ad/tworder/approveOrder',
    method: 'post',
    data: qs.stringify({ id: data.id, flowId: data.flowId })
  })
}
/**
 *获取广告订单明细归属------------------------------------------------------
 */
export const getorderItemBelonglistApi = (id: string) => {
  return request({
    url: '/ad/tworderitembelong/getorderItemBelonglist',
    method: 'get',
    params: { sOrderItemIds: id }
  })
}
/**
 * 获取订单合同号
 */
export const getOrderNoApi = () => {
  return request({
    url: '/ad/tworder/getOrderNo',
    method: 'get'
  })
}
/**
 * 保存广告预约
 */
export const savePreOrderApi = ({
  order,
  orderItems,
  adResourceApplicationId,
  approvedResourceApplicationId
}: {
  order: PreOrder
  orderItems: Tworderitem[]
  adResourceApplicationId: string
  approvedResourceApplicationId: string
}) => {
  return request({
    url: '/ad/tworder/savePreOrder',
    method: 'post',
    data: { order, orderItems, adResourceApplicationId, approvedResourceApplicationId }
  })
}

/**
 * 广告预约提交审核
 */
export const submitPreOrderApi = (orderId: string) => {
  return request({
    url: '/ad/tworder/submitPreOrder',
    method: 'post',
    data: qs.stringify({ orderId })
  })
}
/**
 * 审核广告预约
 */
export const approvePreOrderApi = (orderId: string, status: string, comment: string) => {
  return request({
    url: '/ad/tworder/approvePreOrder',
    method: 'post',
    data: qs.stringify({ orderId, status, comment })
  })
}
/**
 * 根据合同号获取订单信息
 */
export const getorderByContractNum = (data: string) => {
  return request({
    url: '/ad/tworder/getorderByContractNum',
    method: 'get',
    params: { contractNum: data }
  })
}
