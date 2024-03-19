/*
 * @Author: wanghl
 * @Date: 2023-11-06 09:06:53
 * @LastEditTime: 2023-12-09 16:13:24
 * @LastEditors: wanghl
 * @Description:客户备案
 */

import request from '@/utils/request'
import { TAdCustomer, TQueryCustomer, TCustomerApprove } from '@/types/views/customer'
import qs from 'qs'
/**
 * 分页查询备案客户列表
 */
export const getCustomerPageListloginApi = (data: TQueryCustomer) => {
  return request({
    url: '/customer/customerfiling/getCustomerPageList',
    method: 'get',
    params: data
  })
}
/**
 * 备案客户新增
 * @param data
 * @returns
 */
export const saveCustomeApi = (data: TAdCustomer) => {
  return request({
    url: '/customer/customerfiling/saveCustome',
    method: 'post',
    data: data
  })
}
/**
 * 备案客户修改
 * @param data
 * @returns
 */
export const updateCustomerApi = (data: TAdCustomer) => {
  return request({
    url: '/customer/customerfiling/updateCustomer',
    method: 'post',
    data: data
  })
}
/**
 * 备案客户修改
 * @param data
 * @returns
 */
export const approveCustomerApi = (data: TCustomerApprove) => {
  return request({
    url: '/customer/customerfiling/approveCustomer',
    method: 'post',
    data: qs.stringify({ id: data.id, flowId: data.flowId })
  })
}
