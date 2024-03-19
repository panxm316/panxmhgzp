/*
 * @Author: wanghl
 * @Date: 2023-09-19 16:19:29
 * @LastEditTime: 2023-11-08 14:40:07
 * @LastEditors: songly
 * @Description:版面类别Api
 */

import request from '@/utils/request'
import { TPageSortQueryInfo, TPageSortRequest } from '@/types/views/media/PageSort'
import qs from 'qs'
/**
 * 分页查询版面类别列表
 */
export const getPagesortListApi = (data: TPageSortQueryInfo) => {
  return request({
    url: '/media/pagesort/getPagesortList',
    method: 'get',
    params: data
  })
}
/**
 * 保存版面类别信息
 * @param data
 * @returns
 */
export const savePagesortApi = (data: TPageSortRequest) => {
  return request({
    url: '/media/pagesort/savePagesort',
    method: 'post',
    data: data
  })
}
/**
 * 广告规格修改
 * @param data
 * @returns
 */
export const updatePagesortApi = (data: TPageSortRequest) => {
  return request({
    url: '/media/pagesort/updatePagesort',
    method: 'post',
    data: data
  })
}
/**
 * 删除广告规格
 */
export const deteleByIdApi = (data: string) => {
  return request({
    url: '/media/pagesort/deteleById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 获取版面类别最大isort值
 * @returns
 */
export const getMaxIsortApi = () => {
  return request({
    url: '/media/pagesort/getMaxIsort',
    method: 'get'
  })
}
/**
 * 获取可用的版面类别列表,已排序
 * @returns
 */
export const listUsablePageSortApi = (mediaType: string, foldId: string) => {
  return request({
    url: '/media/pagesort/listUsablePageSort',
    method: 'get',
    params: { mediaType, foldId }
  })
}
