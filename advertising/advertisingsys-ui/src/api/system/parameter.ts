

import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { TParameterDTO } from '@/types/views/system/parameter'
import request from '@/utils/request'
import qs from 'qs'

/** 媒体查询实体类 */
export type IParameterQuery = IPageRequest & IBaseQueryInfo

/**
 * 获取媒体信息列表接口
 * @param param
 * @returns
 */
export const getParameterListApi = (param: IParameterQuery) => {
  return request({
    url: '/system/twparameter/getParameterPageList',
    method: 'get',
    params: param
  })
}

/**
 * 保存媒体信息接口
 * @param data
 * @returns
 */
export const saveParameterApi = (data: TParameterDTO) => {
  return request({
    url: '/system/parameter/saveParameter',
    method: 'post',
    data: data
  })
}

/**
 * 更新媒体信息接口
 * @param data
 * @returns
 */
export const updateParameterApi = (data: TParameterDTO) => {
  return request({
    url: '/system/parameter/updateParameter',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除媒体信息接口
 * @param data
 * @returns
 */
export const deleteParameterApi = (data: { ids: string }) => {
  return request({
    url: '/system/parameter/deleteParameterById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取媒体类型下拉列表
 * @returns
 */
export const getParameterTypeComboApi = () => {
  return request({
    url: '/system/twparameter/getParameterTypeCombo',
    method: 'get'
  })
}
/**
 * 判断媒体是否有子集，如果有则不允许删除，获取相关说明，没有说明则说明没有子集
 * @returns
 */
export const getBParameterChildApi = (data: { ids: string }) => {
    return request({
      url: '/system/parameter/getBParameterChild',
      method: 'get',
      params: data
    })
  }
  /**
 * 获取媒体最大isort值
 * @returns
 */
export const getParameterMaxSortApi = () => {
    return request({
      url: '/system/parameter/getParameterMaxSort',
      method: 'get'
    })
  }

