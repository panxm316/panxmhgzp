/*
 * @Author: suny
 * @Date: 2023-08-24 12:37:14
 * @LastEditTime: 2023-08-25 10:18:44
 * @LastEditors: suny
 * @Description: 媒体类型管理相关接口
 */
import { IMediaType, IMediaTypeQuery } from '@/types/views/media/mediatype'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取媒体类型分页列表
 * @returns
 */
export const getMediaTypePageListApi = (param: IMediaTypeQuery) => {
  return request({
    url: '/media/mediatype/getMediaTypePageList',
    method: 'get',
    params: param
  })
}
/**
 * 获取媒体类型下拉列表
 * @returns
 */
export const getMediaTypeComboApi = () => {
  return request({
    url: '/media/mediatype/getMediaTypeCombo',
    method: 'get'
  })
}
/**
 * 保存媒体类型接口
 * @param data
 * @returns
 */
export const saveMediaTypeApi = (data: IMediaType) => {
  return request({
    url: '/media/mediatype/saveMediaType',
    method: 'post',
    data: data
  })
}

/**
 * 更新媒体类型接口
 * @param data
 * @returns
 */
export const updateMediaTypeApi = (data: IMediaType) => {
  return request({
    url: '/media/mediatype/updateMediaType',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除媒体类型接口
 * @param data
 * @returns
 */
export const deleteMediaTypeApi = (data: { ids: string }) => {
  return request({
    url: '/media/mediatype/deleteMediaTypeById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取媒体类型最大isort值
 * @returns
 */
export const getMediaTypeMaxSortApi = () => {
  return request({
    url: '/media/mediatype/getMediaTypeMaxSort',
    method: 'get'
  })
}
