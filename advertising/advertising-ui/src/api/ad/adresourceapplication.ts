/*
 * @Author: suny
 * @Date: 2023-11-13 09:25:56
 * @LastEditTime: 2023-11-16 13:53:16
 * @LastEditors: suny
 * @Description: 广告资源申请表相关API
 */

import {
  IAdResourceApplicationDTO,
  TAdResourceApplicationQuery
} from '@/types/views/ad/adresourceapplication'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取广告资源申请表分页列表
 * @param param
 * @returns
 */
export const getAdResourceApplicationPageListApi = (param: TAdResourceApplicationQuery) => {
  return request({
    url: '/ad/adresourceapplication/getAdResourceApplicationPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存广告资源申请表接口
 * @param data
 * @returns
 */
export const saveAdResourceApplicationApi = (data: IAdResourceApplicationDTO) => {
  return request({
    url: '/ad/adresourceapplication/saveAdResourceApplication',
    method: 'post',
    data: data
  })
}

/**
 * 更新广告资源申请表接口
 * @param data
 * @returns
 */
export const updateAdResourceApplicationApi = (data: IAdResourceApplicationDTO) => {
  return request({
    url: '/ad/adresourceapplication/updateAdResourceApplication',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除广告资源申请表接口
 * @param data
 * @returns
 */
export const deleteAdResourceApplicationByIdApi = (data: { ids: string }) => {
  return request({
    url: '/ad/adresourceapplication/deleteAdResourceApplicationById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 *  提交保存审核信息
 * @param data
 * @returns
 */
export const saveCheckInfoApi = (data: IAdResourceApplicationDTO) => {
  return request({
    url: '/ad/adresourceapplication/saveCheckInfo',
    method: 'post',
    data: data
  })
}
/**
 * 广告资源提交审核
 * @param data
 * @returns
 */
export const updateCheckApi = (data: { ids: string; flowid: string }) => {
  return request({
    url: '/ad/adresourceapplication/updateCheck',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 广告资源提交审核
 * @param data
 * @returns
 */
export const getAdResourceApplicationByIdApi = (data: { id: string }) => {
  return request({
    url: '/ad/adresourceapplication/getAdResourceApplicationById',
    method: 'get',
    params: data
  })
}
/**
 * 获取快速预约订单对应的广告资源集合
 * @param orderId 订单id
 */
export const listAdResourceApplicationByQuickOrderIdApi = (orderId: string) => {
  return request({
    url: '/ad/adresourceapplication/listAdResourceApplicationByQuickOrderId',
    method: 'get',
    params: { orderId: orderId }
  })
}
/**
 * 广告资源审核历史
 * @param data
 * @returns
 */
export const getProcessInstanceByIdApi = (data: { id: string }) => {
  return request({
    url: '/ad/adresourceapplication/getProcessInstanceById',
    method: 'get',
    params: data
  })
}
