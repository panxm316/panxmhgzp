/* AdTypead
 * @Author: muyn
 * @Date: 2023-08-24 15:52:31
 * @LastEditTime: 2023-08-24 13:34:52
 * @LastEditors: muyn
 * @Description:广告来源
 */
import request from '@/utils/request'
import { TAdFrom, TAdFromSearch } from '@/types/views/ad/adfrom'
import qs from 'qs'
/**
 * 获取广告来源列表
 */
export const getTbAdFromPageListApi = (data: TAdFromSearch) => {
  return request({
    url: '/ad/adfrom/getTbAdFromPageList',
    method: 'get',
    params: data
  })
}
/**
 * 根据ID获取单个广告来源
 */
export const getTbAdFromByIdApi = (data: string) => {
  return request({
    url: '/ad/adfrom/getTbAdFromById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 新增广告来源
 * @param data
 * @returns
 */
export const saveTbAdFromApi = (data: TAdFrom) => {
  return request({
    url: '/ad/adfrom/saveTbAdFrom',
    method: 'post',
    data: data
  })
}
/**
 * 修改广告来源
 * @param data
 * @returns
 */
export const updateTbAdFromApi = (data: TAdFrom) => {
  return request({
    url: '/ad/adfrom/updateTbAdFrom',
    method: 'post',
    data: data
  })
}
/**
 * 删除广告类型
 */
export const deleteTbAdFromByIdApi = (data: string) => {
  return request({
    url: '/ad/adfrom/deleteTbAdFrom',
    method: 'post',
    data: qs.stringify({ tbAdFromIds: data })
  })
}

/**
 * 获取广告来源最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/ad/adfrom/getMaxSort',
    method: 'get'
  })
}

/**
 * 重置来源扩展
 */
export const resetAdFromExtendApi = () => {
  return request({
    url: '/ad/adfrom/resetAdFromExtend',
    method: 'get'
  })
}
