/*
 * @Author: songly
 * @Date: 2023-08-28 16:27:48
 * @LastEditTime: 2024-01-10 14:11:00
 * @LastEditors: songly
 * @Description:星期定义
 */
import request from '@/utils/request'
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
 * 获取星期设置最大序号
 * @returns
 */
export const getWeekSettingMaxSortApi = () => {
  return request({
    url: '/ad/weeksetting/getWeekSettingMaxSort',
    method: 'get'
  })
}

/**
 * 获取可用的星期设置,已排序
 */
export const listUsableWeekSettingApi = () => {
  return request({
    url: '/price/weeksetting/listUsableWeekSetting',
    method: 'get'
  })
}
