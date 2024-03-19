/*
 * @Author: lhl
 * @Date: 2024-03-14
 * @Description: 特刊项目管理
 */
import request from '@/utils/request'
import { TSpecialProject } from '@/types/views/ad/specialproject'
import qs from 'qs'
import type { TQueryInfo } from '@/types/common'
/**
 * 获取广告项目列表(分页)
 */
export const getSpecialProjectPageListApi = (data: TQueryInfo) => {
  return request({
    url: '/ad/specialproject/getSpecialProjectPageList',
    method: 'get',
    params: data
  })
}

/**
 * 新增特刊项目
 * @param data
 * @returns
 */
export const saveSpecialProjectApi = (data: TSpecialProject) => {
  return request({
    url: '/ad/specialproject/saveSpecialProject',
    method: 'post',
    data: data
  })
}
/**
 * 修改特刊项目
 * @param data
 * @returns
 */
export const updateSpecialProjectApi = (data: TSpecialProject) => {
  return request({
    url: '/ad/specialproject/updateSpecialProject',
    method: 'post',
    data: data
  })
}

/**
 * 删除广告项目
 */
export const deleteSpecialProjectApi = (data: string) => {
  return request({
    url: '/ad/specialproject/deleteSpecialProject',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 获取特刊项目最大序号
 * @returns
 */
export const getSpecialProjectMaxSortApi = () => {
  return request({
    url: '/ad/specialproject/getMaxSort',
    method: 'get'
  })
}

/**
 * 获取特刊项目实体
 */
export const getBySpecialProjectIdApi = (id: string) => {
  return request({
    url: '/ad/specialproject/getBySpecialProjectId',
    method: 'get',
    params: { id: id }
  })
}

/**
 * 广告项目汇总
 * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd
 * @returns
 */
export const getSpecialProjectCountListApi = (data: {
  stratTime: string
  endTime: string
  adProjectId: string
  pageNum: number
  pageSize: number
  publistStatus: string
  queryKey: string
  projectEnd: string
}) => {
  return request({
    url: '/ad/specialproject/getSpecialProjectCountList',
    method: 'get',
    params: data
  })
}
