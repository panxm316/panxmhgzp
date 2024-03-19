/*
 * @Author: songly
 * @Date: 2023-11-10 13:10:18
 * @LastEditTime: 2024-02-21 13:05:47
 * @LastEditors: songly
 * @Description:
 */
import { TQueryInfo } from '@/types/common/index'

export type TMessageVo = {
  messageId: string
  readed: boolean
} & TQueryInfo
export type TMessageList = {
  /**
   * 主键
   */
  id?: string
  /**
   * 消息标题
   */
  stitle: string

  /**
   * 消息内容
   */
  scontent: string

  /**
   * 消息参数
   */
  sparams: string

  /**
   * 消息类型(TodoTask:工作流待办任务)
   */
  stype: string

  /**
   * 创建时间
   */
  dcreatetime: Date

  /**
   * 创建人id
   */
  createempid: number

  /**
   * 接收人id
   */
  receiveempid: number

  /**
   * 是否已读
   */
  breaded: boolean

  /**
   * 是否删除
   */
  bdeleted: boolean

  /**
   * 流程id
   */
  sflowid: string

  /**
   * 流程实例id
   */
  sprocessinstanceid: string

  /**
   * 唯一id
   */
  suniqueid: string

  /**
   * 流程创建日期
   */
  dprocessinstancecreatedate: Date
}
export type TMessageSearch = {
  stype: string
  breaded: number
} & TQueryInfo
