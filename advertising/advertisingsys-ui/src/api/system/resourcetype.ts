/*
 * @Author: caogd
 * @Date: 2023-10-27 10:43:01
 * @LastEditTime: 2024-03-16 13:50:19
 * @LastEditors: songly
 * @Description:资源类型接口api
 */
import { IResourceType, IResourceTypeQuery } from '@/types/views/system/resourcetype'
import request from '@/utils/request'
import qs from 'qs'

export const getResourceTypePageListApi = (param: IResourceTypeQuery) => {
  return request({
    url: '/system/resourcetype/getResourcetypePageList',
    method: 'get',
    params: param
  })
}

export const updateResourceTypeApi = (data: IResourceType) => {
  return request({
    url: '/system/resourcetype/updateResourceType',
    method: 'post',
    data: data
  })
}
/**
 * 获取文件类型列表
 * @returns
 */
export const getResourceTypeFormatListApi = () => {
  return request({
    url: '/system/resourcetype/getResourceTypeFormatList',
    method: 'get'
  })
}
