/*
 * @Author: songly
 * @Date: 2023-08-25 09:37:47
 * @LastEditTime: 2023-08-31 16:07:57
 * @LastEditors: songly
 * @Description:价格打包类型
 */
import request from '@/utils/request'
import { TPricePackageType } from '@/types/views/ad/pricepackagetype'

import type { TQueryInfo } from '@/types/common/index'
import qs from 'qs'
/**
 * 获取打包类型列表
 */
export const getPricePackageTypePageListApi = (data: TQueryInfo & { buse?: string }) => {
  return request({
    url: '/ad/pricepackagetype/getPricePackageTypePageList',
    method: 'get',
    params: data
  })
}
/**
 * 新增打包类型
 * @param data
 * @returns
 */
export const savePricePackageTypeApi = (data: TPricePackageType) => {
  return request({
    url: '/ad/pricepackagetype/savePricePackageType',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取打包类型
 */
export const getPricePackageTypeByIdApi = (data: string) => {
  return request({
    url: '/ad/pricepackagetype/getPricePackageTypeById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 修改打包类型
 * @param data
 * @returns
 */
export const updatePricePackageTypeApi = (data: TPricePackageType) => {
  return request({
    url: '/ad/pricepackagetype/updatePricePackageType',
    method: 'post',
    data: data
  })
}
/**
 * 删除打包类型
 */
export const deletePricePackageTypeByIdApi = (data: string) => {
  return request({
    url: '/ad/pricepackagetype/deletePricePackageTypeById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取打包类型最大序号
 * @returns
 */
export const getPricePackageTypeMaxSortApi = () => {
  return request({
    url: '/ad/pricepackagetype/getPricePackageTypeMaxSort',
    method: 'get'
  })
}
