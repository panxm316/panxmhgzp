/*
 * @Author: yanz
 * @Date: 2024-01-17 09:06:08
 * @LastEditors: suny suny@hgzp.com.cn
 * @LastEditTime: 2024-03-07 09:05:20
 * @Description: 订单佣金确认相关API
 */

import {
  TOrderItemCommissionVO,
  TOrderItemCommissionDTO
} from '@/types/views/commission/commissioncalcu'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 查询佣金计提分页列表(佣金确认)
 * @param data
 * @returns
 */
export const getCommissionListApi = (data: TOrderItemCommissionVO) => {
  return request({
    url: '/commission/commissionmanage/getCommissionList',
    method: 'get',
    params: data
  })
}
/**
 * 保存佣金计提状态和说明信息
 * @param data
 * @returns
 */
export const saveCommissionApi = (data?: TOrderItemCommissionDTO[]) => {
  return request({
    url: '/commission/commissionmanage/saveCommission',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除佣金计提信息
 * @param data
 * @returns
 */
export const deleteCommissionByIdApi = (data: string | undefined) => {
  return request({
    url: '/commission/commissionmanage/deleteCommissionById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
