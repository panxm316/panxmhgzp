/*
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2024-01-26 13:13:39
 * @LastEditors: wanghl
 * @Description:
 */
import request from '@/utils/request'
import { TQEmployByRoleVO, TQEmployVO, TSEmployVO } from '@/types/views/system/employ'
import qs from 'qs'
import { TMymenu } from '@/types/views/system'

/**
 * 更新个人头像
 * @param data
 * @returns
 */
export const updateEmployImgApi = (data: string) => {
  return request({
    url: '/system/emp/updateEmployImg',
    method: 'post',
    data: qs.stringify({ imgBase64: data })
  })
}
/**
 * 修改个人密码
 * @param oldPassword
 * @param newPassword
 * @returns
 */
export const updatePassWordApi = (oldPassword: string, newPassword: string) => {
  return request({
    url: '/system/emp/updatePassWord',
    method: 'post',
    data: qs.stringify({ oldPassword: oldPassword, newPassword: newPassword })
  })
}
/**
 * 更新个人信息
 * @param data
 * @returns
 */
export const updatePersonSettingApi = (data: TSEmployVO) => {
  return request({
    url: '/system/emp/updatePersonSetting',
    method: 'post',
    data: data
  })
}
/**
 * 获取个人信息
 * @returns
 */
export const getEmployInfoApi = () => {
  return request({
    url: '/system/emp/getEmployInfo',
    method: 'get'
  })
}
/**
 * 保存我的常用菜单
 */
export const saveMymenuApi = (data: TMymenu) => {
  return request({
    url: '/system/mymenu/saveMymenu',
    method: 'post',
    data: data
  })
}
/**
 * 删除我的常用菜单
 */
export const deleteMymenuApi = (data: any) => {
  return request({
    url: '/system/mymenu/delete',
    method: 'post',
    params: data
  })
}
/**
 * 获取我的角色树
 * @returns
 */
export const getMyRolemenuApi = () => {
  return request({
    url: '/system/mymenu/getMyRolemenu',
    method: 'get'
  })
}
/**
 * 获取我的常用角色树
 * @returns
 */
export const getMymenuApi = () => {
  return request({
    url: '/system/mymenu/getMymenu',
    method: 'get'
  })
}
