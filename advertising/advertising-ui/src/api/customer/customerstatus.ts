/*
 * @Author: wanghl
 * @Date: 2023-11-06 09:06:53
 * @LastEditTime: 2023-11-24 16:59:02
 * @LastEditors: wanghl
 * @Description:客户状态
 */

import request from '@/utils/request'
import { TAdCustomerStatus } from '@/types/views/customer/customergroup'
import qs from 'qs'
/**
 *  获取客户状态列表
 */
export const getcustomerStatusListApi = () => {
  return request({
    url: '/customer/customerstatus/getcustomerStatusList',
    method: 'get'
  })
}
/**
 * 获取客户状态下拉列表
 */
export const getcustomerStatusComboApi = () => {
  return request({
    url: 'customer/customerstatus/getcustomerStatusCombo',
    method: 'get'
  })
}
/**
 * 保存客户状态
 * @param data
 * @returns
 */
export const savecustomerStatusApi = (data: TAdCustomerStatus) => {
  return request({
    url: '/customer/customerstatus/savecustomerStatus',
    method: 'post',
    data: data
  })
}
/**
 * 客户状态修改
 * @param data
 * @returns
 */
export const updatecustomerStatusApi = (data: TAdCustomerStatus) => {
  return request({
    url: '/customer/customerstatus/updatecustomerStatus',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取客户状态
 * @param data
 * @returns
 */
export const getcustomerStatusByIdApi = (data: string) => {
  return request({
    url: '/customer/customerstatus/getcustomerStatusById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 删除客户状态
 */
export const deletecustomerStatusApi = (data: string) => {
  return request({
    url: '/customer/customerstatus/deletecustomerStatus',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取客户状态最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/customer/customerstatus/getMaxSort',
    method: 'get'
  })
}
