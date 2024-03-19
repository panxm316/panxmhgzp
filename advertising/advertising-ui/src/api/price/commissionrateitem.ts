/*
 * @Author: suny
 * @Date: 2023-11-29 12:45:18
 * @LastEditTime: 2023-11-30 09:38:57
 * @LastEditors: suny
 * @Description: 计提明细表API
 */

import {
  ITbcommissionrateitem,
  TCommissionRateItemQuery
} from '@/types/views/price/commissionrateitem'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取分页列表
 * @param param
 * @returns
 */
export const getCommissionRateItemPageListApi = (param: TCommissionRateItemQuery) => {
  return request({
    url: '/price/commissionrateitem/getCommissionRateItemPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存计提明细表信息接口
 * @param data
 * @returns
 */
export const saveCommissionRateItemApi = (data: ITbcommissionrateitem) => {
  return request({
    url: '/price/commissionrateitem/saveCommissionRateItem',
    method: 'post',
    data: data
  })
}

/**
 * 更新计提明细表信息接口
 * @param data
 * @returns
 */
export const updateCommissionRateItemApi = (data: ITbcommissionrateitem) => {
  return request({
    url: '/price/commissionrateitem/updateCommissionRateItem',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除计提明细表信息接口
 * @param data
 * @returns
 */
export const deleteCommissionRateItemApi = (data: { ids: string }) => {
  return request({
    url: '/price/commissionrateitem/deleteCommissionRateItem',
    method: 'post',
    data: qs.stringify(data)
  })
}
