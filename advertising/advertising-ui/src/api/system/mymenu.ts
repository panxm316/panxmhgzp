/*
 * @Author: songly
 * @Date: 2024-01-23 15:39:51
 * @LastEditTime: 2024-01-24 10:50:58
 * @LastEditors: songly
 * @Description:我的常用菜单接口
 */
import request from '@/utils/request'
import qs from 'qs'

export const getMymenuApi = () => {
  return request({
    url: '/system/mymenu/getMymenu',
    method: 'get'
  })
}
export const getMyRolemenuApi = () => {
  return request({
    url: '/system/mymenu/getMyRolemenu',
    method: 'get'
  })
}

export const saveMymenuApi = (data: string) => {
  return request({
    url: '/system/mymenu/saveMymenu',
    method: 'get',
    params: qs.stringify({ sMenuIds: data })
  })
}

export const deleteMymenuApi = (data: string) => {
  return request({
    url: '/system/mymenu/delete',
    method: 'get',
    params: qs.stringify({ sMenuIds: data })
  })
}
