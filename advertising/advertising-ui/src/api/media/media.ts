/*
 * @Author: wanghl
 * @Date: 2023-09-20 10:54:57
 * @LastEditTime: 2024-01-22 09:48:01
 * @LastEditors: lhl
 * @Description:
 */
/* '@/utils/request'
 * @Author: suny
 * @Date: 2023-09-19 15:16:39
 * @LastEditTime: 2023-09-19 15:17:06
 * @LastEditors: suny
 * @Description:
 */
import request from '@/utils/request'
import qs from 'qs'
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
 * 根据媒体类型获取媒体下拉列表
 * @param data
 * @returns
 */
export const getMediaDataComboByTypeApi = (data: { type: string }) => {
  return request({
    url: '/media/media/getMediaDataComboByType',
    method: 'get',
    params: data
  })
}
/**
 *  获取有效媒体信心
 * @returns
 */
export const getMediaDataListApi = () => {
  return request({
    url: '/media/media/getMediaDataList',
    method: 'get'
  })
}
/**
 *  获取媒体叠次树
 * @returns
 */
export const getMediaFloadTreeApi = () => {
  return request({
    url: '/media/media/getMediaFloadTree',
    method: 'get'
  })
}
/**
 *  获取媒体叠次版本树
 * @returns
 */
export const getMediaFloadAreaverTreeApi = () => {
  return request({
    url: '/media/media/getMediaFloadAreaverTree',
    method: 'get'
  })
}
