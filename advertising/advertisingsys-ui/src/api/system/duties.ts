/*
 * @Author: caogd yanz
 * @Date: 2023-08-28 16:36:52
 * @LastEditors: yanz
 * @LastEditTime: 2023-09-04 08:40:42
 * @Description: 人员职务api
 */

import request from '@/utils/request'
import qs from 'qs'
import type { TQueryInfo } from '@/types/common/index'
import { IDuties } from '@/types/views/system/duties'

/**
 * @description: 人员职务分页查询
 * @param {TQueryInfo} data
 * @return {*}
 */
export const getDutyPageListApi = (data: TQueryInfo & { buse?: string }) => {
  return request({
    url: '/system/duties/getDutyPageList',
    method: 'get',
    params: data
  })
}

/**
 * @description: 根据Id获取人员职务
 * @param {string} data
 * @return {*}
 */
export const getDutyByIdApi = (data: string) => {
  return request({
    url: '/system/duties/getDutyById',
    method: 'post',
    data: qs.stringify({ id: data })
  })
}

/**
 * @description: 新增人员职务
 * @param {IDuties} data
 * @return {*}
 */
export const saveDutyApi = (data: IDuties) => {
  return request({
    url: '/system/duties/saveDuty',
    method: 'post',
    data: data
  })
}

/**
 * @description: 修改人员职务
 * @param {IDuties} data
 * @return {*}
 */
export const updateDutyApi = (data: IDuties) => {
  return request({
    url: '/system/duties/updateDuty',
    method: 'post',
    data: data
  })
}

/**
 * @description: 删除人员职务
 * @param {string} data
 * @return {*}
 */
export const deleteDutyByIdApi = (data: string) => {
  return request({
    url: '/system/duties/deleteDutyById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * @description: 获取人员职务最大序号
 * @return {*}
 */
export const getDutyMaxSortApi = () => {
  return request({
    url: 'system/duties/getDutyMaxSort',
    method: 'get'
  })
}
/**
 * @description: 获取职务下拉
 * @return {*}
 */
export const getDutiesComboApi = () => {
  return request({
    url: '/system/duties/getDutiesCombo',
    method: 'get'
  })
}
