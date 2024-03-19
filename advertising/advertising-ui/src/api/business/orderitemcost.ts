/*
 * @Author: suny
 * @Date: 2023-12-14 10:15:26
 * @LastEditTime: 2024-03-18 18:22:23
 * @LastEditors: suny
 * @Description: 广告成本相关API
 */

import { IOrderItemCostDTO, TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取广告成本订单详情分页列表
 * @param param
 * @returns
 */
export const getOrderAndItemPageListApi = (param: TOrderAndItemVO) => {
  return request({
    url: '/finance/orderitemcost/getOrderAndItemPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存广告成本接口
 * @param data
 * @returns
 */
export const saveOrderItemCostApi = (data: IOrderItemCostDTO) => {
  return request({
    url: '/finance/orderitemcost/saveOrderItemCost',
    method: 'post',
    data: data
  })
}

/**
 * 更新广告成本接口
 * @param data
 * @returns
 */
export const updateOrderItemCostApi = (data: IOrderItemCostDTO) => {
  return request({
    url: '/finance/orderitemcost/updateOrderItemCost',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除广告成本接口
 * @param data
 * @returns
 */
export const deleteOrderItemCostByIdApi = (data: { ids: string }) => {
  return request({
    url: '/finance/orderitemcost/deleteOrderItemCostById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 广告成本提交审核
 * @param data
 * @returns
 */
export const updateOrderItemCostStatusApi = (data: { id: string; istatus: number }) => {
  return request({
    url: '/finance/orderitemcost/updateOrderItemCostStatus',
    method: 'post',
    data: qs.stringify(data)
  })
}
