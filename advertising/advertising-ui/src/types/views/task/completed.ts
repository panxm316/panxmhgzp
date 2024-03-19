/*
 * @Author: songly
 * @Date: 2023-10-17 13:22:15
 * @LastEditTime: 2024-03-15 13:08:00
 * @LastEditors: wanghl
 * @Description:已办任务查询
 */
import { PageDto } from '@/types/views/task/start'
export interface IRoleQuery extends PageDto {
  /**
   * 查询开始时间
   */
  startTime?: string
  /** * 查询结束时间   */
  endTime?: string
  keywords?: string
  /** 任务结束时间标志 */
  bendFlag: boolean
}

export interface ITaskDto {
  /** 流程id */
  flowId: string
  /** 参数集合  */
  paramMap: Map<String, Object>
  /** 流程实例id   */
  processInstanceId: string
  /** 执行id  */
  executionId: string
  /**  耗时  */
  durationInMillis: number
  /** 任务id  */
  taskId: string
  /** 执行人  */
  assign: string
  /** 任务名称  */
  taskName: string
  /** 节点id*/
  nodeId: string
  /** 任务创建时间  */
  taskCreateTime: string
  /** 任务结束时间  */
  taskEndTime: string
  /**   流程组名字  */
  groupName: string
  /** 发起人id  */
  rootUserId: string
  /** 发起人名字  */
  rootUserName: string
  /** 发起人头像  */
  rootUserAvatarUrl?: string
  /** 发起时间  */
  startTime: string
  /** 流程名称  */
  processName: string
  businessId?: string
  groupId?: string
}
