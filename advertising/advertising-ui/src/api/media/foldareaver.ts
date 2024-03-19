/*
 * @Author: songly
 * @Date: 2023-09-19 10:49:44
 * @LastEditTime: 2023-11-11 09:52:04
 * @LastEditors: caogd
 * @Description:叠次版本接口
 */
import request from '@/utils/request'
import { IFoldAreaver } from '@/types/views/media/foldareaver'
import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取叠次版本列表
 */
export const getFoldAreaverPageListApi = (
  data: TQueryInfo & { foldid?: string; mediaid: string; mediatypekey: string }
) => {
  return request({
    url: '/media/foldareaver/getFoldAreaverList',
    method: 'get',
    params: data
  })
}
/**
 * 新增叠次版本
 * @param data
 * @returns
 */
export const saveFoldAreaverApi = (data: IFoldAreaver) => {
  return request({
    url: '/media/foldareaver/saveFoldAreaver',
    method: 'post',
    data: data
  })
}
/**
 * 修改叠次版本
 * @param data
 * @returns
 */
export const updateFoldAreaverApi = (data: IFoldAreaver) => {
  return request({
    url: '/media/foldareaver/updateFoldAreaver',
    method: 'post',
    data: data
  })
}
/**
 * 删除叠次版本
 */
export const deleteFoldAreaverByIdApi = (data: string) => {
  return request({
    url: '/media/foldareaver/deleteFoldAreaverById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 根据Id获取叠次版本
 */
export const getFoldAreaverByIdApi = (data: string) => {
  return request({
    url: '/ad/FoldAreaver/getFoldAreaverById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}

/**
 * 获取最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/media/foldareaver/getMaxIsort',
    method: 'get'
  })
}

/**
 * 获取媒体列表
 */
export const getMediaDataComboByTypeApi = (data: string) => {
  return request({
    url: '/media/media/getMediaDataComboByType',
    method: 'get',
    params: { type: data }
  })
}
/**
 * 获取媒体叠次列表
 */
export const getFoldComboApi = (data: string) => {
  return request({
    url: '/media/fold/getFoldCombo',
    method: 'get',
    params: { mediaid: data }
  })
}
/**
 * 获取媒体类型-媒体-叠次树,前端使用 Element Plus 树组件
 */
export const getMediaFoldElTreeApi = () => {
  return request({
    url: '/media/foldareaver/getMediaFoldElTree',
    method: 'get'
  })
}
