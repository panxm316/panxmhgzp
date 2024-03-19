/*
 * @Author: wanghl
 * @Date: 2023-11-06 09:06:53
 * @LastEditTime: 2023-11-24 16:09:47
 * @LastEditors: wanghl
 * @Description:客户信誉度
 */

import request from '@/utils/request'
import { TAdCustomerCredit } from '@/types/views/customer/customergroup'
import qs from 'qs'
/**
 *  获取客户信誉度列表
 */
export const getcustomerCreditListApi = () => {
  return request({
    url: '/customer/customercredit/getcustomerCreditList',
    method: 'get'
  })
}
/**
 * 获取客户信誉度下拉列表
 */
export const getcustomerCreditComboApi = () => {
  return request({
    url: 'customer/customercredit/getcustomerCreditCombo',
    method: 'get'
  })
}
/**
 * 保存客户信誉度
 * @param data
 * @returns
 */
export const savecustomerCreditApi = (data: TAdCustomerCredit) => {
  return request({
    url: '/customer/customercredit/savecustomerCredit',
    method: 'post',
    data: data
  })
}
/**
 * 客户信誉度修改
 * @param data
 * @returns
 */
export const updatecustomerCreditApi = (data: TAdCustomerCredit) => {
  return request({
    url: '/customer/customercredit/updatecustomerCredit',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取客户信誉度
 * @param data
 * @returns
 */
export const getcustomerCreditByIdApi = (data: string) => {
  return request({
    url: '/customer/customercredit/getcustomerCreditById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 删除客户信誉度
 */
export const deletecustomerCreditApi = (data: string) => {
  return request({
    url: 'customer/customercredit/deletecustomerCredit',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取客户信誉度最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/customer/customercredit/getMaxSort',
    method: 'get'
  })
}
