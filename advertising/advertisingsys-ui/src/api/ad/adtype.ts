/*
 * @Author: songly
 * @Date: 2023-08-23 15:52:31
 * @LastEditTime: 2023-08-31 15:36:14
 * @LastEditors: songly
 * @Description:广告类型
 */
import request from '@/utils/request'
import { TAdType } from '@/types/views/ad/adtype'
import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取广告类型列表
 */
export const getAdTypePageListApi = (data: TQueryInfo & { buse?: string }) => {
  return request({
    url: '/ad/adtype/getAdTypePageList',
    method: 'get',
    params: data
  })
}
/**
 * 新增广告类型
 * @param data
 * @returns
 */
export const saveAdTypeApi = (data: TAdType) => {
  return request({
    url: '/ad/adtype/saveAdType',
    method: 'post',
    data: data
  })
}
/**
 * 修改广告类型
 * @param data
 * @returns
 */
export const updateAdTypeApi = (data: TAdType) => {
  return request({
    url: '/ad/adtype/updateAdType',
    method: 'post',
    data: data
  })
}
/**
 * 删除广告类型
 */
export const deleteAdTypeByIdApi = (data: string) => {
  return request({
    url: '/ad/adtype/deleteAdTypeById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 根据Id获取广告类型
 */
export const getAdTypeByIdApi = (data: string) => {
  return request({
    url: '/ad/adtype/getAdTypeById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}

/**
 * 获取广告最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/ad/adtype/getAdTypeMaxSort',
    method: 'get'
  })
}
