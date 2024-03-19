/*
 * @Author: suny
 * @Date: 2024-01-15 10:01:07
 * @LastEditTime: 2024-03-18 09:47:36
 * @LastEditors: suny
 * @Description: 订单佣金计算相关API
 */
import {
  ICommissionDTO,
  TOrderItemCommissionDTO,
  TOrderItemCommissionVO
} from '@/types/views/commission/commissioncalcu'
import request from '@/utils/request'

/**
 * 查询订单明细相关综合对象列表(佣金计提)
 * @param data
 * @returns
 */
export const getOrderAndItemDTOListForCommissionApi = (data: TOrderItemCommissionVO) => {
  return request({
    url: '/commission/commissioncalcu/getOrderAndItemDTOListForCommission',
    method: 'get',
    params: data
  })
}
/**
 * 佣金计提计算结果保存或更新
 * @param data
 * @returns
 */
export const saveOrUpdateCommissionApi = (data: TOrderItemCommissionDTO) => {
  return request({
    url: '/commission/commissioncalcu/saveOrUpdateCommission',
    method: 'post',
    data: data
  })
}
/**
 * 手动添加佣金计提
 * @param data
 * @returns
 */
export const saveAddCommissionApi = (data: ICommissionDTO) => {
  return request({
    url: '/commission/commissioncalcu/saveAddCommission',
    method: 'post',
    data: data
  })
}
/**
 * 查询手动添加的佣金计提分页列表
 * @param data
 * @returns
 */
export const getAddCommissionListApi = (data: TOrderItemCommissionVO) => {
  return request({
    url: '/commission/commissioncalcu/getAddCommissionList',
    method: 'get',
    params: data
  })
}
