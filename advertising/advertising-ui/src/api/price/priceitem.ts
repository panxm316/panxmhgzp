/*
 * @Author: caogd
 * @Date: 2023-11-13 13:27:07
 * @LastEditTime: 2023-12-13 14:14:15
 * @LastEditors: wanghl
 * @Description: 价格明细设置API
 */

import {
  TPriceItemQuery,
  TPriceItemDTO,
  TPriceItemCopy,
  TPriceItemQuerylist
} from '@/types/views/price/priceitem'
import request from '@/utils/request'
import qs from 'qs'
/**
 * 获取价格明细分页列表
 * @param param
 * @returns
 */
export const getPriceItemPageListApi = (param: TPriceItemQuery) => {
  return request({
    url: '/price/priceitem/getPriceItemPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存价格明细接口
 * @param data
 * @returns
 */
export const savePriceItemApi = (data: TPriceItemDTO) => {
  return request({
    url: '/price/priceitem/savePriceItem',
    method: 'post',
    data: data
  })
}
/**
 * 批量修改价格明细接口
 * @param data
 * @returns
 */
export const batchUpdatePriceItemApi = (data: TPriceItemCopy) => {
  return request({
    url: '/price/priceitem/batchUpdatePriceItem',
    method: 'post',
    data: qs.stringify(data)
  })
}

/**
 * 更新价格明细接口
 * @param data
 * @returns
 */
export const updatePriceItemApi = (data: TPriceItemDTO) => {
  return request({
    url: '/price/priceitem/updatePriceItem',
    method: 'post',
    data: data
  })
}
/**
 * 删除价格明细
 * @param ids
 * @returns
 */
export const deletePriceItemApi = (ids: string) => {
  return request({
    url: '/price/priceitem/deletePriceItem',
    method: 'post',
    data: qs.stringify({ ids: ids })
  })
}
/**
 * 获取价格明细最大isort值
 * @returns
 */
export const getPriceItemMaxSortApi = () => {
  return request({
    url: '/price/priceitem/getPriceItemMaxSort',
    method: 'get'
  })
}
/**
 * 获取价格明细分页列表
 * @param param
 * @returns
 */
export const getPriceItemListApi = (param: TPriceItemQuerylist) => {
  return request({
    url: '/price/priceitem/getPriceItemList',
    method: 'get',
    params: param
  })
}
