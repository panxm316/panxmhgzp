/*
 * @Author: suny
 * @Date: 2023-08-23 12:53:26
 * @LastEditTime: 2023-09-07 15:29:49
 * @LastEditors: caogd
 * @Description: 媒体管理相关接口
 */
import { IMedia, IMediaQuery } from '@/types/views/media/media'
import request from '@/utils/request'
import qs from 'qs'
/**
 * 获取媒体信息列表接口
 * @param param
 * @returns
 */
export const getMediaPageListApi = (param: IMediaQuery) => {
  return request({
    url: '/media/media/getMediaPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存媒体信息接口
 * @param data
 * @returns
 */
export const saveMediaApi = (data: IMedia) => {
  return request({
    url: '/media/media/saveMedia',
    method: 'post',
    data: data
  })
}

/**
 * 更新媒体信息接口
 * @param data
 * @returns
 */
export const updateMediaApi = (data: IMedia) => {
  return request({
    url: '/media/media/updateMedia',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除媒体信息接口
 * @param data
 * @returns
 */
export const deleteMediaApi = (data: { ids: string }) => {
  return request({
    url: '/media/media/deleteMediaById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取媒体最大isort值
 * @returns
 */
export const getMediaMaxSortApi = () => {
  return request({
    url: '/media/media/getMediaMaxSort',
    method: 'get'
  })
}
/**
 *  媒体下拉列表
 * @returns
 */
export const getMediaDataComboApi = () => {
  return request({
    url: '/media/media/getMediaDataCombo',
    method: 'get'
  })
}
/**
 * 判断媒体是否有子集，如果有则不允许删除，获取相关说明，没有说明则说明没有子集
 * @returns
 */
export const getBMediaChildApi = (data: { ids: string }) => {
  return request({
    url: '/media/media/getBMediaChild',
    method: 'get',
    params: data
  })
}
