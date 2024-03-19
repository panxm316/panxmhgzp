/*
 * @Author: wanghl
 * @Date: 2023-10-25 11:17:07
 * @LastEditTime: 2024-01-19 09:53:59
 * @LastEditors: wanghl
 * @Description:客户管理
 */
import request from '@/utils/request'
import {
  TAdCustomer,
  TQueryCustomer,
  Twcustomerfiles,
  TcombineCustomer
} from '@/types/views/customer'
import qs from 'qs'
/**
 * 分页查询客户列表
 */
export const getCustomerPageListApi = (data: TQueryCustomer) => {
  return request({
    url: '/customer/customer/getCustomerPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取客户信息
 * @param data
 * @returns
 */
export const getCustomerByIdApi = (data: string) => {
  return request({
    url: '/customer/customer/getCustomerById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 根据名称相似的下拉客户信息
 * @param data
 * @returns
 */
export const getCustomerCombo = (data: string | undefined) => {
  return request({
    url: 'customer/customer/getCustomerCombo',
    method: 'get',
    params: { name: data }
  })
}
/**
 * 客户新增
 * @param data
 * @returns
 */
export const saveCustomeApi = (data: TAdCustomer) => {
  return request({
    url: '/customer/customer/saveCustome',
    method: 'post',
    data: data
  })
}
/**
 * 客户修改
 * @param data
 * @returns
 */
export const updateCustomerApi = (data: TAdCustomer) => {
  return request({
    url: '/customer/customer/updateCustomer',
    method: 'post',
    data: data
  })
}
/**
 * 删除客户信息
 */
export const deleteCustomerByIdApi = (data: string) => {
  return request({
    url: '/customer/customer/deleteCustomerById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取客户isort值
 * @returns
 */
export const getCustomerMaxSortApi = () => {
  return request({
    url: '/customer/customer/getCustomerMaxSort',
    method: 'get'
  })
}
// //////////////////////////////////////////////////////////////////////////////////////附件资料
/**
 * 根据客户id获取客户的附件资料
 */
export const getCustomerFilesListApi = (data: string | undefined) => {
  return request({
    url: '/customer/customerfiles/getCustomerFilesList',
    method: 'get',
    params: { customerid: data }
  })
}
/**
 * 根据Id获取文件信息
 */
export const getCustomerFilesByIdApi = (data: string) => {
  return request({
    url: '/customer/customerfiles/getCustomerFilesById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 客户新增附件
 * @param data
 * @returns
 */
export const saveCustomerFilesApi = (data: Twcustomerfiles) => {
  return request({
    url: '/customer/customerfiles/saveCustomerFiles',
    method: 'post',
    data: data
  })
}
/**
 * 客户修改附件信息
 * @param data
 * @returns
 */
export const updateCustomerFilesApi = (data: Twcustomerfiles) => {
  return request({
    url: '/customer/customerfiles/updateCustomerFiles',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除
 */
export const deleteCustomerFilesByIdApi = (data: string) => {
  return request({
    url: '/customer/customerfiles/deleteCustomerFilesById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 根据客户Id删除附件
 */
export const deleteCustomerFilesByCustomerIdApi = (data: string) => {
  return request({
    url: '/customer/customerfiles/deleteCustomerFilesByCustomerId',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 恢复已删文件
 */
export const recoveryCustomerFilesByIdApi = (data: string) => {
  return request({
    url: '/customer/customerfiles/recoveryCustomerFilesById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 恢复已删客户附件
 */
export const recoveryCustomerFilesByCustomerIdApi = (data: string) => {
  return request({
    url: '/customer/customerfiles/recoveryCustomerFilesByCustomerId',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
// //////////////////////////////////////////////////////////////////////////////////////////////客户归属
/**
 * 获取客户归属用户
 */
export const getCustomerbelongListApi = (data: string) => {
  return request({
    url: '/customer/customerbelong/getCustomerbelongList',
    method: 'get',
    params: { customerid: data }
  })
}
/**
 * 根据Id获取客户归属信息
 */
export const getCustomerbelongByIdApi = (data: string) => {
  return request({
    url: '/customer/customerbelong/getCustomerbelongById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 客户归属新增
 * @param data
 * @returns
 */
export const saveCustomerbelongApi = (data: Twcustomerfiles) => {
  return request({
    url: '/customer/customerbelong/saveCustomerbelong',
    method: 'post',
    data: data
  })
}
/**
 * 客户归属修改
 * @param data
 * @returns
 */
export const updateCustomerbelongApi = (data: Twcustomerfiles) => {
  return request({
    url: '/customer/customerbelong/updateCustomerbelong',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除归属
 */
export const deleteCustomerbelongByIdApi = (data: string) => {
  return request({
    url: '/customer/customerbelong/deleteCustomerbelongById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 根据客户Id删除归属
 */
export const deleteCustomerbelongByCustomerIdApi = (data: string) => {
  return request({
    url: '/customer/customerbelong/deleteCustomerbelongByCustomerId',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
// /////////////////////////////////////////////////////////////////////////////////客户合并
/**
 * 根据客户Id删除归属
 */
export const combineCustomerApi = (data: TcombineCustomer) => {
  return request({
    url: '/customer/customer/combineCustomer',
    method: 'post',
    data: qs.stringify({ sMainId: data.sMainId, subIds: data.subIds })
  })
}
