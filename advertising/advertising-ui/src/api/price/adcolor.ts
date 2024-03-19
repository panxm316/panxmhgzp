/*
 * @Author: peij
 * @Date: 2023-08-24 13:35:00
 * @LastEditTime: 2023-11-16 16:02:57
 * @LastEditors: wanghl
 * @Description:
 */
import request from '@/utils/request'
import type { TQueryInfo } from '@/types/common/index'
import type { TAdcolor } from '@/types/views/price/adcolor'
import qs from 'qs'
/**
 * 广告色彩列表
 * @param data
 * @returns
 */
export const getAdcolorListApi = (data: TQueryInfo & { mediatypekey?: string }) => {
  return request({
    url: '/price/adcolor/getAdcolorList',
    method: 'get',
    params: data
  })
}

/**
 * 获取最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/price/adcolor/getMaxSort',
    method: 'get'
  })
}
/**
 * 色彩保存
 * @param data
 * @returns
 */
export const saveAdcolorApi = (data: TAdcolor) => {
  return request({
    url: '/price/adcolor/saveAdcolor',
    method: 'post',
    data: data
  })
}
/**
 * 色彩删除
 * @param ids
 * @returns
 */
export const deleteAdcolorApi = (ids: string) => {
  return request({
    url: '/price/adcolor/deleteAdcolor',
    method: 'post',
    data: qs.stringify({ ids })
  })
}
/**
 * 色彩更新
 * @param data
 * @returns
 */
export const updateAdcolor = (data: TAdcolor) => {
  return request({
    url: '/price/adcolor/updateAdcolor',
    method: 'post',
    data: data
  })
}
/**
 * 广告色彩列表
 * @param data
 * @returns
 */
export const getAdColorTreeListApi = (data: string) => {
  return request({
    url: '/price/adcolor/getAdColorTreeList',
    method: 'get',
    params: { mediaType: data }
  })
}
/**
 * 根据媒体类型获取可用的广告颜色列表,已排序
 */
export const listUsableAdColorApi = (mediaType: string) => {
  return request({
    url: '/price/adcolor/listUsableAdColor',
    method: 'get',
    params: { mediaType }
  })
}
