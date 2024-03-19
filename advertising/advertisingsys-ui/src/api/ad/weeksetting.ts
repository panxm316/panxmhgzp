/*
 * @Author: songly
 * @Date: 2023-08-28 16:27:48
 * @LastEditTime: 2023-08-28 17:14:12
 * @LastEditors: songly
 * @Description:星期定义
 */
import request from '@/utils/request'
import { TWeekSetting } from '@/types/views/ad/weeksetting'
import qs from 'qs'
import { TQueryInfo } from '@/types/common'
/**
 * 获取星期设置列表
 */
export const getWeekSettingListApi = (data: TQueryInfo) => {
  return request({
    url: '/ad/weeksetting/getWeekSettingPageList',
    method: 'get',
    params: data
  })
}
/**
 * 新增星期设置
 * @param data
 * @returns
 */
export const saveWeekSettingApi = (data: TWeekSetting) => {
  return request({
    url: '/ad/weeksetting/saveWeekSetting',
    method: 'post',
    data: data
  })
}
/**
 * 根据Id获取星期设置信息
 */
export const getWeekSettingByIdApi = (data: string) => {
  return request({
    url: '/ad/weeksetting/getWeekSettingById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}
/**
 * 修改星期设置
 * @param data
 * @returns
 */
export const updateWeekSettingApi = (data: TWeekSetting) => {
  return request({
    url: '/ad/weeksetting/updateWeekSetting',
    method: 'post',
    data: data
  })
}
/**
 * 删除星期设置
 */
export const deleteWeekSettingByIdApi = (data: string) => {
  return request({
    url: '/ad/weeksetting/deleteWeekSettingById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取星期设置最大序号
 * @returns
 */
export const getWeekSettingMaxSortApi = () => {
  return request({
    url: '/ad/weeksetting/getWeekSettingMaxSort',
    method: 'get'
  })
}
