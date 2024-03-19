/*
 * @Author: suny
 * @Date: 2023-11-30 15:15:22
 * @LastEditTime: 2023-11-30 15:24:22
 * @LastEditors: suny
 * @Description: 折扣降点实现API
 */

import { IDiscountReduceDTO, TDiscountReduceQuery } from '@/types/views/price/discountreduce'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取分页列表
 * @param param
 * @returns
 */
export const getDiscountReducePageListApi = (param: TDiscountReduceQuery) => {
  return request({
    url: '/price/discountreduce/getDiscountReducePageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存折扣降点信息接口
 * @param data
 * @returns
 */
export const saveDiscountReduceApi = (data: IDiscountReduceDTO) => {
  return request({
    url: '/price/discountreduce/saveDiscountReduce',
    method: 'post',
    data: data
  })
}

/**
 * 更新折扣降点信息接口
 * @param data
 * @returns
 */
export const updateDiscountReduceApi = (data: IDiscountReduceDTO) => {
  return request({
    url: '/price/discountreduce/updateDiscountReduce',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除折扣降点信息接口
 * @param data
 * @returns
 */
export const deleteDiscountReduceApi = (data: { ids: string }) => {
  return request({
    url: '/price/discountreduce/deleteDiscountReduce',
    method: 'post',
    data: qs.stringify(data)
  })
}
