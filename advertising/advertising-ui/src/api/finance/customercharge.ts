/*
 * @Author: caogd
 * @Date: 2023-10-31 10:45:53
 * @LastEditTime: 2023-12-20 16:27:46
 * @LastEditors: suny
 * @Description: 客户预收款api
 */
import { ICustomerChargeDTO, TCustomerChargeQuery } from '@/types/views/finance/customercharge'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 客户预收款分页查询
 * @param param
 * @returns
 */
export const getCustomerChargePageListApi = (param: TCustomerChargeQuery) => {
  return request({
    url: '/finance/customercharge/getCustomerChargePageList',
    method: 'get',
    params: param
  })
}
/**
 * 客户预收款新增
 * @param data
 * @returns
 */
export const saveCustomerChargeApi = (data: ICustomerChargeDTO) => {
  return request({
    url: '/finance/customercharge/saveCustomerCharge',
    method: 'post',
    data: data
  })
}
/**
 * 客户预收款修改
 * @param data
 * @returns
 */
export const updateCustomerChargeApi = (data: ICustomerChargeDTO) => {
  return request({
    url: '/finance/customercharge/updateCustomerCharge',
    method: 'post',
    data: data
  })
}
/**
 * 根据id客户预收款详情
 * @param param
 * @returns
 */
export const getCustomerChargeDetailApi = (id: string) => {
  return request({
    url: '/finance/customercharge/getCustomerChargeDetail',
    method: 'get',
    params: { id: id }
  })
}
/**
 * 客户预收款删除
 * @param data
 * @returns
 */
export const deleteCustomerChargeApi = (id: string) => {
  return request({
    url: '/finance/customercharge/deleteCustomerCharge',
    method: 'post',
    data: qs.stringify({ id: id })
  })
}
