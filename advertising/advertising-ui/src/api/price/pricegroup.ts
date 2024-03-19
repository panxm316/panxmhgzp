/*
 * @Author: suny
 * @Date: 2023-11-10 14:25:01
 * @LastEditTime: 2023-12-12 16:40:10
 * @LastEditors: wanghl
 * @Description: 价格组相关API
 */
import { IPriceGroupDTO, TPriceGroupQuery, TQIPriceGroupD } from '@/types/views/price/pricegroup'
import request from '@/utils/request'
import qs from 'qs'
/**
 * 获取叠次分页列表
 * @param param
 * @returns
 */
export const getPriceGroupPageListApi = (param: TPriceGroupQuery) => {
  return request({
    url: '/price/pricegroup/getPriceGroupPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存价格组信息接口
 * @param data
 * @returns
 */
export const savePriceGroupApi = (
  data: IPriceGroupDTO & { startTime?: string; endTime?: string; copyFlag: boolean }
) => {
  return request({
    url: '/price/pricegroup/savePriceGroup',
    method: 'post',
    data: data
  })
}

/**
 * 更新价格组信息接口
 * @param data
 * @returns
 */
export const updatePriceGroupApi = (data: IPriceGroupDTO) => {
  return request({
    url: '/price/pricegroup/updatePriceGroup',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除价格组信息接口
 * @param data
 * @returns
 */
export const deletePriceGroupApi = (data: { ids: string }) => {
  return request({
    url: '/price/pricegroup/deletePriceGroup',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取价格组最大isort值
 * @returns
 */
export const getPriceGroupMaxSortApi = () => {
  return request({
    url: '/price/pricegroup/getPriceGroupMaxSort',
    method: 'get'
  })
}
/**
 * 获取价格组列表
 */
export const getPriceGroupListApi = (data: TQIPriceGroupD) => {
  return request({
    url: '/price/pricegroup/getPriceGroupList',
    method: 'get',
    params: data
  })
}
