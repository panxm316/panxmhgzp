/*
 * @Author: wanghl
 * @Date: 2023-10-25 11:17:07
 * @LastEditTime: 2023-11-16 16:07:25
 * @LastEditors: wanghl
 * @Description:色彩结构类别
 */
import request from '@/utils/request'
import { TAPageColor, TQPageColor } from '@/types/views/schedule/pagecolor'
import qs from 'qs'
/**
 * 获取版面色彩分页列表
 */
export const getPagecolorListApi = (data: TQPageColor) => {
  return request({
    url: '/schedule/pagecolor/getPagecolorList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取版面色彩
 * @param data
 * @returns
 */
export const getPagecolorByIdApi = (data: string) => {
  return request({
    url: '/schedule/pagecolor/getPagecolorById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 方法功能:新增版面色彩
 * @param data
 * @returns
 */
export const savePagecolorApi = (data: TAPageColor) => {
  return request({
    url: '/schedule/pagecolor/savePagecolor',
    method: 'post',
    data: data
  })
}
/**
 * 更新版面色彩
 * @param data
 * @returns
 */
export const updatePagecolorApi = (data: TAPageColor) => {
  return request({
    url: '/schedule/pagecolor/updatePagecolor',
    method: 'post',
    data: data
  })
}
/**
 * 删除版面色彩
 */
export const deletePagecolorApi = (data: string) => {
  return request({
    url: '/schedule/pagecolor/deletePagecolor',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取版面色彩的最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/schedule/pagecolor/getMaxSort',
    method: 'get'
  })
}
/**
 * 获取所有版面色彩
 * @returns
 */
export const getPagecolorAllApi = () => {
  return request({
    url: '/schedule/pagecolor/getPagecolorAll',
    method: 'get'
  })
}
