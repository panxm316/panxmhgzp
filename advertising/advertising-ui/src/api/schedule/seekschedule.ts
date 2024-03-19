/*
 * @Author: lhl
 * @Date: 2023-11-27
 * @LastEditTime: 2024-01-12 14:07:24
 * @LastEditors: lhl
 * @Description: 查看排期API
 */

import request from '@/utils/request'

/**
 * 根据叠次id获取叠次版本下拉框列表
 * @param foldid
 * @returns
 */
export const getFoldAreaverComboApi = (data: string) => {
  return request({
    url: '/schedule/scheduleseek/getFoldAreaverCombo',
    method: 'get',
    params: { foldid: data }
  })
}

/**
 * 根据叠次id、叠次版本id、日期范围获取版面计划列表
 * @param foldId、areaverId、startTome、endTime
 * @returns
 */
export const getFoldPagePlanListApi = (data: {
  foldId: string
  areaverId: string
  startTome: string
  endTime: string
}) => {
  return request({
    url: '/schedule/scheduleseek/getFoldPagePlanList',
    method: 'get',
    params: data
  })
}

/**
 * 根据媒体id,叠次id、叠次版本id、日期获取版面列表
 * @param mediaId，foldId、areaverId、publishTime
 * @returns
 */
export const getPageListtApi = (data: {
  mediaId: string
  foldId: string
  areaverId: string
  publishTime: string
}) => {
  return request({
    url: '/schedule/scheduleseek/getPageList',
    method: 'get',
    params: data
  })
}

/**
 * 根据媒体id,叠次id、叠次版本id、日期获取广告订单
 * @param mediaId，foldId、areaverId、strartTime、endTime
 * @returns
 */
export const getAdvOrderList = (data: {
  mediaId: string
  foldId: string
  areaverId: string
  strartTime: string
  endTime: string
}) => {
  return request({
    url: '/schedule/scheduleseek/getAdvOrderList',
    method: 'get',
    params: data
  })
}

/**
 * 根据媒体ID、时间范围、广告形式获取日广告订单
 * @param mediaId、startTime、endTime
 * @returns
 */
export const getAdvOrderofDay = (data: {
  mediaId: string
  foldId: string
  areaverId: string
  strartTime: string
  endTime: string
}) => {
  return request({
    url: '/schedule/scheduleseek/getAdvOrderofDay',
    method: 'get',
    params: data
  })
}

/**
 * 获取预定广告订单
 * @param mediaId、startTime、endTime
 * @returns
 */
export const queryOredrPredetermineListAPI = (data: {
  mediaId: string
  foldId: string
  areaverId: string
  strartTime: string
  endTime: string
}) => {
  return request({
    url: '/schedule/scheduleseek/queryOredrPredetermineList',
    method: 'get',
    params: data
  })
}
