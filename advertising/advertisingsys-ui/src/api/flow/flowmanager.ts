/*
 * @Author: songly
 * @Date: 2024-02-16 11:06:55
 * @LastEditTime: 2024-02-16 15:30:08
 * @LastEditors: songly
 * @Description:流程类型管理
 */
import request from '@/utils/request'
import { TFlowType } from '@/types/views/flow/flowmanager'
import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取工作流程类型列表
 */
export const getFlowTypePageListApi = (data: TQueryInfo) => {
  return request({
    url: '/flowset/getFlowTypePageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取工作流程类型列表
 */
export const getFlowTypeListApi = (data: TQueryInfo) => {
  return request({
    url: '/flowset/getFlowTypeList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取工作流程类型信息
 */
export const getFlowTypeByIdApi = (data: string) => {
  return request({
    url: '/flowset/getFlowTypeById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 新增工作流程类型
 * @param data
 * @returns
 */
export const saveFlowTypeApi = (data: TFlowType) => {
  return request({
    url: '/flowset/saveFlowType',
    method: 'post',
    data: data
  })
}
/**
 * 修改工作流程类型
 * @param data
 * @returns
 */
export const updateFlowTypeApi = (data: TFlowType) => {
  return request({
    url: '/flowset/updateFlowType',
    method: 'post',
    data: data
  })
}
/**
 * 删除工作流程类型
 */
export const deleteFlowTypeByIdApi = (data: string) => {
  return request({
    url: '/flowset/deleteFlowTypeById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取工作流程类型最大序号
 * @returns
 */
export const getFlowTypeMaxSortApi = () => {
  return request({
    url: '/flowset/getFlowTypeMaxSort',
    method: 'get'
  })
}
