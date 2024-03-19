/*
 * @Author: suny
 * @Date: 2023-10-23 17:07:17
 * @LastEditTime: 2023-10-28 10:47:10
 * @LastEditors: suny
 * @Description: 人员休假相关API
 */

import { ITwempholiday } from '@/types/views/task/empholiday'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取休假分页列表
 * @param param
 * @returns
 */
export const getEmpHolidayPageListApi = (param: ITwempholiday) => {
  return request({
    url: '/flow/empholiday/getHolidayPageList',
    method: 'get',
    params: param
  })
}
/**
 * 保存休假信息接口
 * @param data
 * @returns
 */
export const saveEmpHolidayApi = (data: ITwempholiday) => {
  return request({
    url: '/flow/empholiday/saveEmpHoliday',
    method: 'post',
    data: data
  })
}
/**
 * 更新休假接口
 * @param data
 * @returns
 */
export const updateEmpHolidayApi = (data: ITwempholiday) => {
  return request({
    url: '/flow/empholiday/updateEmpHoliday',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除休假接口
 * @param data
 * @returns
 */
export const deleteHolidayByIdApi = (data: { ids: string }) => {
  return request({
    url: '/flow/empholiday/deleteEmpHolidayById',
    method: 'post',
    data: qs.stringify(data)
  })
}
