/*
 * @Author: lhl
 * @Date: 2024-01-05
 * @Description: 广告项目汇总API
 */

import request from '@/utils/request'

/**
 * 广告项目汇总
 * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd
 * @returns
 */
export const getAdProjectCountListApi = (data: {
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
    url: '/ad/adprojectcount/getAdProjectCountList',
    method: 'get',
    params: data
  })
}

/**
 * 获取广告项目列表
 */
export const getAdProjectListApi = () => {
  return request({
    url: '/ad/adprojectcount/getAdProjectList',
    method: 'get'
  })
}

/**
 * 广告项目明细订单
 * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus
 * @returns
 */
export const getAdProjectOrderDetailsApi = (data: {
  adProjectId: string
  mediaId: string
  pageNum: number
  pageSize: number
  detailtype: string
}) => {
  return request({
    url: '/ad/adprojectcount/getAdProjectOrderDetails',
    method: 'get',
    params: data
  })
}

/**
 * 广告项目成本明细
 * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus
 * @returns
 */
export const getAdProjectCostDetailsApi = (data: {
  adProjectId: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/ad/adprojectcount/getAdProjectCostDetails',
    method: 'get',
    params: data
  })
}

/**
 * 获取广告项目相关合同
 * @param param
 * @returns
 */
export const getAdProjectContractApi = (data: {
  adProjectId: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/ad/adprojectcount/getAdProjectContract',
    method: 'get',
    params: data
  })
}
/**
 * 获取订单明细
 * @param param
 * @returns
 */
export const getAdOrderItemApi = (data: {
  adOrderId: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/ad/adprojectcount/getAdOrderItem',
    method: 'get',
    params: data
  })
}
