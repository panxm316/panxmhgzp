/*
 * @Author: songly
 * @Date: 2023-11-17 15:34:42
 * @LastEditTime: 2024-01-10 14:29:03
 * @LastEditors: songly
 * @Description:
 */
import request from '@/utils/request'
import { TMessageSearch, TMessageVo } from '@/types/views/system/message'
import qs from 'qs'

export const updateMessageStatusApi = (messageVo: TMessageVo) => {
  return request({
    url: '/system/message/updateMessageStatus',
    method: 'post',
    data: messageVo
  })
}

export const getMessageCountByEmpIdApi = (receiveEmpId: string) => {
  return request({
    url: '/system/message/getMessageCountByEmpId',
    method: 'get',
    params: { receiveEmpId }
  })
}
export const getMessagePageListApi = (messageVo: TMessageSearch) => {
  return request({
    url: '/system/message/getMessagePageList',
    method: 'get',
    params: messageVo
  })
}
/** 获取消息类型: TodoTask="待办任务",  "ApprovePass"="审批通过",
 * "ApproveReject"="审批驳回","TodoDebtReason"="欠款原因待办",  "TaskNotice"="任务额度完成情况通知"
 * */
export const getMessageTypeComboApi = () => {
  return request({
    url: '/enums/emnusset/getMessageTypeCombo',
    method: 'get'
  })
}
/**
 * 根据id删除消息
 * @param data
 * @returns
 */
export const deleteMessageInfoByIdApi = (data: string) => {
  return request({
    url: '/system/message/deleteMessageInfoById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}

/**
 * 根据id修改消息已读状态
 * @param data
 * @returns
 */
export const updateMessageStatusByIdApi = (data: string) => {
  return request({
    url: '/system/message/updateMessageStatusById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
