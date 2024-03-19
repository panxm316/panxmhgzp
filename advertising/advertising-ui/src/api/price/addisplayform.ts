/*
 * @Author: suny
 * @Date: 2023-11-06 10:47:12
 * @LastEditTime: 2023-11-06 10:57:28
 * @LastEditors: suny
 * @Description:广告形式API
 */
import { ITbaddisplayform, TAdDisplayFormQuery } from '@/types/views/price/addisplayform'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取广告形式分页列表
 * @param param
 * @returns
 */
export const getAdDisplayFormPageListApi = (param: TAdDisplayFormQuery) => {
  return request({
    url: '/price/addisplayform/getAdDisplayFormPageList',
    method: 'get',
    params: param
  })
}

/**
 * 保存广告形式接口
 * @param data
 * @returns
 */
export const saveAdDisplayFormApi = (data: ITbaddisplayform) => {
  return request({
    url: '/price/addisplayform/saveAdDisplayForm',
    method: 'post',
    data: data
  })
}

/**
 * 更新广告形式接口
 * @param data
 * @returns
 */
export const updateAdDisplayFormApi = (data: ITbaddisplayform) => {
  return request({
    url: '/price/addisplayform/updateAdDisplayForm',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除广告形式接口
 * @param data
 * @returns
 */
export const deleteAdDisplayFormByIdApi = (data: { ids: string }) => {
  return request({
    url: '/price/addisplayform/deleteAdDisplayFormById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取广告形式最大isort值
 * @returns
 */
export const getAdDisplayFormMaxSortApi = () => {
  return request({
    url: '/price/addisplayform/getAdDisplayFormMaxSort',
    method: 'get'
  })
}

/**
 * 根据媒体类型获取可用的广告形式列表,已排序
 * @param mediaType
 */
export const listUsableAdDisplayFormApi = (mediaType: string) => {
  return request({
    url: '/price/addisplayform/listUsableAdDisplayForm',
    method: 'get',
    params: {
      mediaType
    }
  })
}
