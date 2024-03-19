/*
 * @Author: suny
 * @Date: 2023-08-24 12:37:14
 * @LastEditTime: 2023-11-06 10:53:39
 * @LastEditors: suny
 * @Description: 媒体类型管理相关接口
 */
import request from '@/utils/request'

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
 * 获取可用的媒体列表,已排序
 * @author wangxk
 */
export const listUsableMediaTypeApi = () => {
  return request({
    url: '/media/mediatype/listUsableMediaType',
    method: 'get'
  })
}
