/*
 * @Author: suny
 * @Date: 2023-11-09 11:11:40
 * @LastEditTime: 2023-11-09 12:46:43
 * @LastEditors: suny
 * @Description: 客户服务相关API
 */
import { IcustomerserviceDTO, Tcustomerservicequery } from '@/types/views/customer/customerservice'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取客户服务分页列表
 * @param param
 * @returns
 */
export const getCustomerServicePageListApi = (param: Tcustomerservicequery) => {
  return request({
    url: '/customer/customerservice/getCustomerServicePageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存客户服务接口
 * @param data
 * @returns
 */
export const saveCustomerServiceApi = (data: IcustomerserviceDTO) => {
  return request({
    url: '/customer/customerservice/saveCustomerService',
    method: 'post',
    data: data
  })
}

/**
 * 更新客户服务接口
 * @param data
 * @returns
 */
export const updateCustomerServiceApi = (data: IcustomerserviceDTO) => {
  return request({
    url: '/customer/customerservice/updateCustomerService',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除客户服务接口
 * @param data
 * @returns
 */
export const deleteCustomerServiceByIdApi = (data: { ids: string }) => {
  return request({
    url: '/customer/customerservice/deleteCustomerServiceById',
    method: 'post',
    data: qs.stringify(data)
  })
}
