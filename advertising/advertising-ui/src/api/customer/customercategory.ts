/*
 * @Author: wanghl
 * @Date: 2023-11-06 09:06:53
 * @LastEditTime: 2023-11-24 16:01:45
 * @LastEditors: wanghl
 * @Description:客户分类
 */

import request from '@/utils/request'
import { TAdCustomerCategory } from '@/types/views/customer/customergroup'
import qs from 'qs'
/**
 *  获取客户分类列表
 */
export const getcustomerCategoryListApi = () => {
  return request({
    url: '/customer/customercategory/getcustomerCategoryList',
    method: 'get'
  })
}
/**
 * 获取客户分类下拉列表
 */
export const getcustomerCategoryComboApi = () => {
  return request({
    url: 'customer/customercategory/getcustomerCategoryCombo',
    method: 'get'
  })
}
/**
 * 保存客户分类
 * @param data
 * @returns
 */
export const savecustomerCategoryApi = (data: TAdCustomerCategory) => {
  return request({
    url: '/customer/customercategory/savecustomerCategory',
    method: 'post',
    data: data
  })
}
/**
 * 客户分类修改
 * @param data
 * @returns
 */
export const updatecustomerCategoryApi = (data: TAdCustomerCategory) => {
  return request({
    url: '/customer/customercategory/updatecustomerCategory',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取客户分类
 * @param data
 * @returns
 */
export const getcustomerCategoryByIdApi = (data: string) => {
  return request({
    url: '/customer/customercategory/getcustomerCategoryById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 删除客户分类
 */
export const deletecustomerCategoryApi = (data: string) => {
  return request({
    url: 'customer/customercategory/deletecustomerCategory',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取客户分类最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/customer/customercategory/getMaxSort',
    method: 'get'
  })
}
