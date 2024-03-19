/*
 * @Author: songly
 * @Date: 2023-11-22 15:25:52
 * @LastEditTime: 2023-12-13 09:59:18
 * @LastEditors: wanghl
 * @Description: 折扣明细接口定义
 */

import request from '@/utils/request'
import type { TQueryInfo } from '@/types/common/index'
import type { TDiscountItem, TQDiscountGroup } from '@/types/views/price/discountitem'
import qs from 'qs'
/**
 * 折扣明细列表
 * @param data
 * @returns
 */
export const getDiscountItemListApi = (data: TQueryInfo & { mediatypekey?: string }) => {
  return request({
    url: '/price/discountitem/getdiscountItemPageList',
    method: 'get',
    params: data
  })
}

/**
 * 折扣明细列表
 * @param data
 * @returns
 */
export const getDiscountItemByIdApi = (data: string) => {
  return request({
    url: '/price/discountitem/getdiscountItemById',
    method: 'get',
    params: { mediaType: data }
  })
}
/**
 * 折扣明细保存
 * @param data
 * @returns
 */
export const saveDiscountItemApi = (data: TDiscountItem) => {
  return request({
    url: '/price/discountitem/savediscountItem',
    method: 'post',
    data: data
  })
}
/**
 * 折扣明细删除
 * @param ids
 * @returns
 */
export const deleteDiscountItemApi = (ids: string) => {
  return request({
    url: '/price/discountitem/deletediscountItem',
    method: 'post',
    data: qs.stringify({ ids })
  })
}
/**
 * 折扣明细更新
 * @param data
 * @returns
 */
export const updateDiscountItemApi = (data: TDiscountItem) => {
  return request({
    url: '/price/discountitem/updatediscountItem',
    method: 'post',
    data: data
  })
}
/**
 * 折扣明细下拉列表
 */
export const getdiscountItemApi = (data: TQDiscountGroup) => {
  return request({
    url: '/price/discountitem/getdiscountItemList',
    method: 'get',
    params: data
  })
}
