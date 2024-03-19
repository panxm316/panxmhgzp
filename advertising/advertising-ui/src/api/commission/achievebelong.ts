/*
 * @Author: suny
 * @Date: 2024-01-11 10:46:42
 * @LastEditTime: 2024-01-11 14:49:35
 * @LastEditors: suny
 * @Description: 业绩归属相关API
 */

import { TOrderAndItemVO, IOrderAndItemDTO } from '@/types/views/business/orderitemcost'
import request from '@/utils/request'

/**
 * 获取订单详情查询分页列表
 * @param data
 * @returns
 */
export const getPerformanceRatioApi = (data: TOrderAndItemVO) => {
  return request({
    url: '/ad/tworderitem/getPerformanceRatio',
    method: 'get',
    params: data
  })
}
/**
 * 根据订单明细id获取业绩归属数据
 * @param data
 * @returns
 */
export const getOrderBelongListByOrderitemIdApi = (data: { orderitemId: string }) => {
  return request({
    url: '/ad/tworderitembelong/getOrderBelongListByOrderitemId',
    method: 'get',
    params: data
  })
}
/**
 * 保存业绩归属数据
 * @param data
 * @returns
 */
export const saveCommisstionDataApi = (data: IOrderAndItemDTO) => {
  return request({
    url: '/ad/tworderitembelong/saveCommisstionData',
    method: 'post',
    data: data
  })
}
