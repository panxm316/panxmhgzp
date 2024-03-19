/* AdTypead
 * @Author: muyn
 * @Date: 2023-08-24 15:52:31
 * @LastEditTime: 023-09-02 15:39:19
 * @LastEditors: songly
 * @Description:广告行业
 */
import request from '@/utils/request'
import { TAdIndustry, TAdIndustrySearch } from '@/types/views/ad/adindustry'
import qs from 'qs'
/**
 * 获取广告行业列表
 */
export const getTbAdIndustryPageListApi = (data: TAdIndustrySearch) => {
  return request({
    url: '/ad/adindustry/getTbAdIndustryPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据ID获取单个广告行业
 */
export const getTbAdIndustryByIdApi = (data: string) => {
  return request({
    url: '/ad/adindustry/getTbAdIndustryById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 新增广告行业
 * @param data
 * @returns
 */
export const saveTbAdIndustryApi = (data: TAdIndustry) => {
  return request({
    url: '/ad/adindustry/saveTbAdIndustry',
    method: 'post',
    data: data
  })
}
/**
 * 修改广告行业
 * @param data
 * @returns
 */
export const updateTbAdIndustryApi = (data: TAdIndustry) => {
  return request({
    url: '/ad/adindustry/updateTbAdIndustry',
    method: 'post',
    data: data
  })
}
/**
 * 删除广告类型
 */
export const deleteTbAdIndustryByIdApi = (data: string) => {
  return request({
    url: '/ad/adindustry/deleteTbAdIndustry',
    method: 'post',
    data: qs.stringify({ tbAdIndustryIds: data })
  })
}

/**
 * 获取广告行业最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/ad/adindustry/getMaxSort',
    method: 'get'
  })
}
/**
 * 重置行业扩展
 */
export const resetAdIndustryExtendApi = () => {
  return request({
    url: '/ad/adindustry/resetAdIndustryExtend',
    method: 'get'
  })
}
