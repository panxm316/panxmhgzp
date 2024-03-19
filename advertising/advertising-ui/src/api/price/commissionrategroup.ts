/*
 * @Author: suny
 * @Date: 2023-11-29 10:32:12
 * @LastEditTime: 2023-12-01 08:45:55
 * @LastEditors: suny
 * @Description: 提成比例总表API
 */

import {
  ITbcommissionrategroup,
  TCommissionRateGroupQuery
} from '@/types/views/price/commissionrategroup'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取分页列表
 * @param param
 * @returns
 */
export const getCommissionRateGroupPageListApi = (param: TCommissionRateGroupQuery) => {
  return request({
    url: '/price/commissionrategroup/getCommissionRateGroupPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存计提总表信息接口
 * @param data
 * @returns
 */
export const saveCommissionRateGroupApi = (
  data: ITbcommissionrategroup & { copyFlag: boolean }
) => {
  return request({
    url: '/price/commissionrategroup/saveCommissionRateGroup',
    method: 'post',
    data: data
  })
}

/**
 * 更新计提总表信息接口
 * @param data
 * @returns
 */
export const updateCommissionRateGroupApi = (data: ITbcommissionrategroup) => {
  return request({
    url: '/price/commissionrategroup/updateCommissionRateGroup',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除计提总表信息接口
 * @param data
 * @returns
 */
export const deleteCommissionRateGroupApi = (data: { ids: string }) => {
  return request({
    url: '/price/commissionrategroup/deleteCommissionRateGroup',
    method: 'post',
    data: qs.stringify(data)
  })
}
