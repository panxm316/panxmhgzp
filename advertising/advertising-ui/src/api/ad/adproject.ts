/*
 * @Author: songly
 * @Date: 2023-09-01 09:53:21
 * @LastEditTime: 2024-03-12 12:46:31
 * @LastEditors: wanghl
 * @Description: 广告项目
 */
import request from '@/utils/request'
import { TAdProject } from '@/types/views/ad/adproject'
import qs from 'qs'
import type { TQueryInfo } from '@/types/common'
/**
 * 获取广告项目列表(分页)
 */
export const getAdProjectPageListApi = (data: TQueryInfo) => {
  return request({
    url: '/ad/adproject/getAdProjectPageList',
    method: 'get',
    params: data
  })
}
/**
 * 获取广告项目列表
 */
export const getByAdProjectIdApi = (id: string) => {
  return request({
    url: '/ad/adproject/getByAdProjectId',
    method: 'get',
    params: { id: id }
  })
}
/**
 * 新增广告项目
 * @param data
 * @returns
 */
export const saveAdProjectApi = (data: TAdProject) => {
  return request({
    url: '/ad/adproject/saveAdProject',
    method: 'post',
    data: data
  })
}
/**
 * 修改广告项目
 * @param data
 * @returns
 */
export const updateAdProjectApi = (data: TAdProject) => {
  return request({
    url: '/ad/adproject/updateAdProject',
    method: 'post',
    data: data
  })
}

/**
 * 删除广告项目
 */
export const deleteAdProjectApi = (data: string) => {
  return request({
    url: '/ad/adproject/deleteAdProject',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取广告项目最大序号
 * @returns
 */
export const getAdProjectMaxSortApi = () => {
  return request({
    url: '/ad/adproject/getMaxSort',
    method: 'get'
  })
}
/**
 * 结项广告项目
 */
export const endAdProjectApi = (data: string) => {
  return request({
    url: '/ad/adproject/endAdProject',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取广告项目列表
 */
export const getAdProjectListApi = () => {
  return request({
    url: '/ad/adproject/getAdProjectList',
    method: 'get'
  })
}
