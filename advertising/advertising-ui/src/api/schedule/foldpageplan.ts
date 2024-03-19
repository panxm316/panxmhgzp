/*
 * @Author: wanghl
 * @Date: 2023-10-25 11:17:07
 * @LastEditTime: 2023-12-19 14:25:40
 * @LastEditors: wanghl
 * @Description:版面计划api
 */
import request from '@/utils/request'
import {
  TAFoldPagePlan,
  TAFoldPagePlanOne,
  TQFoldPagePlan,
  TQPageplanList
} from '@/types/views/schedule/foldpageplan'
import qs from 'qs'
/**
 * 按媒体获取版面计划
 */
export const getMeiaPageplanListApi = (data: TQFoldPagePlan) => {
  return request({
    url: '/schedule/foldpageplan/getMeiaPageplanList',
    method: 'get',
    params: data
  })
}
/**
 * 获取版面计划分页列表
 */
export const getFoldPageplanListApi = (data: TQFoldPagePlan) => {
  return request({
    url: '/schedule/foldpageplan/getFoldPageplanList',
    method: 'get',
    params: data
  })
}
/**
 * 按媒体、叠、类别、色彩、日期等获取版面计划列表
 */
export const getPageplanListApi = (data: TQPageplanList) => {
  return request({
    url: '/schedule/foldpageplan/getPageplanList',
    method: 'get',
    params: data
  })
}
/**
 * 根据Id获取版面计划
 * @param data
 * @returns
 */
export const getFoldPageplanByIdApi = (data: string) => {
  return request({
    url: '/schedule/foldpageplan/getFoldPageplanById',
    method: 'get',
    params: { id: data }
  })
}
/**
 * 方法功能:单个保存版面计划
 * @param data
 * @returns
 */
export const saveFoldPageplanApi = (data: TAFoldPagePlanOne) => {
  return request({
    url: '/schedule/foldpageplan/saveFoldPageplan',
    method: 'post',
    data: data
  })
}
/**
 * 方法功能:批量添加版面计划
 * @param data
 * @returns
 */
export const saveBatchPagePlaneApi = (data: TAFoldPagePlan) => {
  return request({
    url: '/schedule/foldpageplan/saveBatchPagePlane',
    method: 'post',
    data: data
  })
}
/**
 * 方法功能:按照版数新增版面计划
 * @param data
 * @returns
 */
export const savePagePlaneByPageNumApi = (data: TAFoldPagePlanOne) => {
  return request({
    url: '/schedule/foldpageplan/savePagePlaneByPageNum',
    method: 'post',
    data: data
  })
}
/**
 * 修改版面计划
 * @param data
 * @returns
 */
export const updateFoldPageplanApi = (data: TAFoldPagePlanOne) => {
  return request({
    url: '/schedule/foldpageplan/updateFoldPageplan',
    method: 'post',
    data: data
  })
}
/**
 * 单个删除版面计划
 */
export const deleteFoldPageplanApi = (data: string) => {
  return request({
    url: '/schedule/foldpageplan/deleteFoldPageplan',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 批量删除版面计划
 */
export const deleteMediaPageplanApi = (data: TAFoldPagePlan | undefined) => {
  return request({
    url: '/schedule/foldpageplan/deleteMediaPageplan',
    method: 'post',
    data: data
  })
}
/**
 * 复制版面计划
 */
export const duplicateFoldPageplanApi = (data: TAFoldPagePlanOne) => {
  return request({
    url: '/schedule/foldpageplan/duplicateFoldPageplan',
    method: 'post',
    data: data
  })
}
/**
 * 获取版面计划的最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/schedule/foldpageplan/getMaxSort',
    method: 'get'
  })
}
/**
 * 修改版面计划版心
 */
export const updatePagePlanePageSizeApi = (data: TAFoldPagePlanOne) => {
  return request({
    url: '/schedule/foldpageplan/updatePagePlanePageSize',
    method: 'post',
    data: data
  })
}

/**
 * 获取排期选项，用来下拉选择 todo 待删除
 */
export const getPlanComboApi = () => {
  return request({
    url: '/schedule/foldpageplan/getPlanCombo',
    method: 'get'
  })
}

/**
 * 根据媒体类型key-媒体id-叠次id拼接的字符串和预刊日期获取排期计划列表
 */
export const listPlanOnPreOrderApi = (
  date: string,
  mediaTypeKey: string,
  mediaId: string,
  foldId: string
) => {
  return request({
    url: '/schedule/foldpageplan/listPlanOnPreOrder',
    method: 'get',
    params: { date, mediaTypeKey, mediaId, foldId }
  })
}
