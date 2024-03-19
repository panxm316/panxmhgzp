/*
 * @Author: songly
 * @Date: 2023-08-29 16:49:00
 * @LastEditTime: 2023-08-31 16:04:36
 * @LastEditors: songly
 * @Description:报纸版心设置
 */
import request from '@/utils/request'
import { TPageSize } from '@/types/views/ad/pagesize'
import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取报纸版心列表
 */
export const getPageSizeListApi = (data: TQueryInfo & { buse?: string }) => {
  return request({
    url: '/media/pagesize/getPageSizePageList',
    method: 'get',
    params: data
  })
}
/**
 * 新增报纸版心
 * @param data
 * @returns
 */
export const savePageSizeApi = (data: TPageSize) => {
  return request({
    url: '/media/pagesize/savePageSize',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取报纸版心信息
 */
export const getPageSizeByIdApi = (data: string) => {
  return request({
    url: '/media/pagesize/getPageSizeById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 修改报纸版心
 * @param data
 * @returns
 */
export const updatePageSizeApi = (data: TPageSize) => {
  return request({
    url: '/media/pagesize/updatePageSize',
    method: 'post',
    data: data
  })
}
/**
 * 删除报纸版心
 */
export const deletePageSizeByIdApi = (data: string) => {
  return request({
    url: '/media/pagesize/deletePageSizeById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取报纸版心最大序号
 * @returns
 */
export const getPageSizeMaxSortApi = () => {
  return request({
    url: '/media/pagesize/getPageSizeMaxSort',
    method: 'get'
  })
}
