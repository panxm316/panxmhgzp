/*
 * @Author: suny
 * @Date: 2023-09-19 13:31:12
 * @LastEditTime: 2023-10-11 09:29:03
 * @LastEditors: suny
 * @Description:媒体叠次API
 */
import { TFoldQuery, IFoldVO } from '@/types/views/media/fold'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取叠次分页列表
 * @param param
 * @returns
 */
export const getFoldPageListApi = (param: TFoldQuery) => {
  return request({
    url: '/media/fold/getFoldPageList',
    method: 'get',
    params: param
  })
}

/**
 * 保存叠次接口
 * @param data
 * @returns
 */
export const saveFoldApi = (data: IFoldVO) => {
  return request({
    url: '/media/fold/saveFold',
    method: 'post',
    data: data
  })
}

/**
 * 更新叠次接口
 * @param data
 * @returns
 */
export const updateFoldApi = (data: IFoldVO) => {
  return request({
    url: '/media/fold/updateFold',
    method: 'post',
    data: data
  })
}

/**
 * 根据id删除叠次接口
 * @param data
 * @returns
 */
export const deleteFoldByIdApi = (data: { ids: string }) => {
  return request({
    url: '/media/fold/deleteFoldById',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 获取媒体最大isort值
 * @returns
 */
export const getFoldMaxSortApi = () => {
  return request({
    url: '/media/fold/getFoldMaxSort',
    method: 'get'
  })
}
/**
 *  叠次下拉列表
 * @returns
 */
export const getFoldComboApi = () => {
  return request({
    url: '/media/fold/getFoldCombo',
    method: 'get'
  })
}
/**
 * 判断叠次是否有子集，如果有则不允许删除，获取相关说明，没有说明则说明没有子集
 * @returns
 */
export const getBFoldChildApi = (data: { ids: string }) => {
  return request({
    url: '/media/fold/getBFoldChild',
    method: 'get',
    params: data
  })
}
