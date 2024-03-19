/*
 * @Author: wanghl
 * @Date: 2024-03-08 10:21:42
 * @LastEditTime: 2024-03-08 10:36:23
 * @LastEditors: wanghl
 * @Description: 常用审批意见api
 */

import request from '@/utils/request'
import { TwOpinionsPageList, TwOpinionsList, TwsaveOpinions } from '@/types/views/task/opinions'
import qs from 'qs'
/**
 * 获取常用审批意见分页列表
 */
export const getOpinionsPageListApi = (data: TwOpinionsPageList) => {
  return request({
    url: '/system/opinions/getOpinionsPageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取常用审批意见列表
 */
export const getOpinionsListApi = (data: TwOpinionsList) => {
  return request({
    url: '/system/opinions/getOpinionsList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id常用审批意见详情
 * @param data
 * @returns
 */
export const getopinionsByIdApi = (data: string) => {
  return request({
    url: '/system/opinions/getopinionsById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 方法功能:新增常用审批意
 * @param data
 * @returns
 */
export const saveOpinionsApi = (data: TwsaveOpinions) => {
  return request({
    url: '/system/opinions/saveOpinions',
    method: 'post',
    data: data
  })
}
/**
 * 更新常用审批意
 * @param data
 * @returns
 */
export const updateOpinionsApi = (data: TwsaveOpinions) => {
  return request({
    url: '/system/opinions/updateOpinions',
    method: 'post',
    data: data
  })
}
/**
 * 删除常用审批意
 */
export const deleteOpinionsApi = (data: string) => {
  return request({
    url: '/system/opinions/deleteOpinions',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取版面色彩的最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/system/opinions/getMaxSort',
    method: 'get'
  })
}
