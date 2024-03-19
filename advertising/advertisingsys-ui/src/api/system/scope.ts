/*
 * @Author: songly
 * @Date: 2023-08-30 16:07:49
 * @LastEditTime: 2023-08-30 16:27:54
 * @LastEditors: songly
 * @Description:范围
 */

import request from '@/utils/request'
import { Scope } from '@/types/views/system/scope'
import qs from 'qs'
import type { TQueryInfo } from '@/types/common/index'
/**
 * 获取范围列表(分页)
 */
export const getScopePageListApi = (data: TQueryInfo) => {
  return request({
    url: '/system/scope/getScopePageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取范围列表
 */
export const getScopeListApi = () => {
  return request({
    url: '/system/scope/getScopeList',
    method: 'get'
  })
}
/**
 * 新增范围
 * @param data
 * @returns
 */
export const saveScopeApi = (data: Scope) => {
  return request({
    url: '/system/scope/saveScope',
    method: 'post',
    data: data
  })
}
/**
 * 修改范围
 * @param data
 * @returns
 */
export const updateScopeApi = (data: Scope) => {
  return request({
    url: '/system/scope/updateScope',
    method: 'post',
    data: data
  })
}
/**
 * 删除范围
 */
export const deleteScopeApi = (data: string) => {
  return request({
    url: '/system/scope/deleteScope',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取范围最大序号
 * @returns
 */
export const getScopeMaxSortApi = () => {
  return request({
    url: '/system/scope/getMaxIsort',
    method: 'get'
  })
}
