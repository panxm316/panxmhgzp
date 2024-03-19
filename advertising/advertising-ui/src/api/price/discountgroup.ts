/*
 * @Author: songly
 * @Date: 2023-11-22 15:25:52
 * @LastEditTime: 2023-12-14 11:02:18
 * @LastEditors: wanghl
 * @Description: 折扣总表接口定义
 */

import request from '@/utils/request'
import type { TQueryInfo } from '@/types/common/index'
import type { TDiscountGroup, TDiscountGroupQuery } from '@/types/views/price/discountgroup'
import type { TQDiscountGroup } from '@/types/views/price/discountitem'
import qs from 'qs'
/**
 * 折扣总表列表
 * @param data
 * @returns
 */
export const getDiscountGroupListApi = (data: TDiscountGroupQuery) => {
  return request({
    url: '/price/discountgroup/getdiscountGroupPageList',
    method: 'get',
    params: data
  })
}

/**
 * 折扣总表列表
 * @param data
 * @returns
 */
export const getDiscountGroupByIdApi = (data: string) => {
  return request({
    url: '/price/discountgroup/getdiscountGroupById',
    method: 'get',
    params: { mediaType: data }
  })
}
/**
 * 折扣总表保存
 * @param data
 * @returns
 */
export const saveDiscountGroupApi = (data: TDiscountGroup) => {
  return request({
    url: '/price/discountgroup/savediscountGroup',
    method: 'post',
    data: data
  })
}
/**
 * 折扣总表删除
 * @param ids
 * @returns
 */
export const deleteDiscountGroupApi = (ids: string) => {
  return request({
    url: '/price/discountgroup/deletediscountGroup',
    method: 'post',
    data: qs.stringify({ ids })
  })
}
/**
 * 折扣总表更新
 * @param data
 * @returns
 */
export const updateDiscountGroup = (data: TDiscountGroup) => {
  return request({
    url: '/price/discountgroup/updatediscountGroup',
    method: 'post',
    data: data
  })
}
/**
 * 折扣总表下拉列表
 */
export const getdiscountItemListApi = (data: string) => {
  return request({
    url: '/price/discountgroup/getdiscountItemList',
    method: 'get',
    params: { sYear: data }
  })
}
/**
 * 获取年度折扣(有符合条件的明细折扣取明细中定义的折扣，否则取总表折扣)
 */
export const getNdiscountApi = (data: TQDiscountGroup) => {
  return request({
    url: '/price/discountgroup/getNdiscount',
    method: 'get',
    params: data
  })
}
