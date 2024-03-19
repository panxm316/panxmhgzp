/*
 * @Author: lhl
 * @Date: 2023-12-27
 * @Description: 任务额度汇总API
 */

import request from '@/utils/request'
import { TwtaskMessageDTO } from '@/types/views/business/tasks'
/**
 * 任务额度部门汇总
 * @param mediaId,dateType,stratTime,endTime,pageNum,pageSize,publishstatus
 * @returns
 */
export const getDepartmentTaskCountListApi = (data: {
  mediaId: string
  dateType: string
  stratTime: string
  endTime: string
  pageNum: number
  pageSize: number
  publishstatus: string
  depLevel: String
}) => {
  return request({
    url: '/business/twtaskscount/getDepartmentTaskCountList',
    method: 'get',
    params: data
  })
}

/**
 * 任务额度人员汇总
 * @param mediaId,dateType,stratTime,endTime,pageNum,pageSize,publishstatus
 * @returns
 */
export const getEmployeTaskCountListApi = (data: {
  mediaId: string
  dateType: string
  stratTime: string
  endTime: string
  pageNum: number
  pageSize: number
  publishstatus: string
  depLevel: String
}) => {
  return request({
    url: '/business/twtaskscount/getEmployeTaskCountList',
    method: 'get',
    params: data
  })
}
/**
 * 任务额度消息通知
 * @param param
 * @returns
 */
export const sendTwtaskMessageApi = (data: TwtaskMessageDTO) => {
  return request({
    url: '/business/twtaskscount/sendTwtaskMessage',
    method: 'post',
    data: data
  })
}
