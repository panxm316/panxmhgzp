/*
 * @Author: caogd
 * @Date: 2023-10-27 10:43:01
 * @LastEditTime: 2023-11-01 10:23:38
 * @LastEditors: suny
 * @Description:资源类型接口api
 */
import { IResourceType, IResourceTypeQuery } from '@/types/views/system/resourcetype'
import request from '@/utils/request'
import qs from 'qs'

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
