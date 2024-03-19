/*
 * @Author: suny
 * @Date: 2023-10-17 12:37:43
 * @LastEditTime: 2023-11-02 14:21:57
 * @LastEditors: peij
 * @Description: 我的发起任务相关type
 */
/**
 * PageDto
 */
export interface PageDto {
  /**
   * 页码
   */
  pageNum?: number
  /**
   * 每页的数量
   */
  pageSize?: number
  [property: string]: any
}

export type TProcessInstancePageDto = {
  processInstanceId: string
} & PageDto

/**
 * 流程记录
 *
 * ProcessInstanceRecord
 */
export interface ProcessInstanceRecord {
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 逻辑删除字段
   */
  delFlag?: boolean
  /**
   * 结束时间
   */
  endTime?: string
  /**
   * 流程id
   */
  flowId?: string
  /**
   * 表单数据
   */
  formData?: string
  /**
   * 组id
   */
  groupId?: string
  /**
   * 组名称
   */
  groupName?: string
  /**
   * 用户id
   */
  id?: number
  /**
   * 头像
   */
  logo?: string
  /**
   * 流程名字
   */
  name?: string
  /**
   * 上级流程实例id
   */
  parentProcessInstanceId?: string
  /**
   * 流程实例id
   */
  processInstanceId?: string
  /**
   * 审批结果
   */
  result?: boolean
  /**
   * 状态
   */
  status?: number
  /**
   * 更新时间
   */
  updateTime?: string
  /**
   * 用户id
   */
  userId?: string
  [property: string]: any
}

export interface IProps {
  value: object
  options: object
  self: boolean
  multi: boolean
  oriForm: object
  minLength: number
  maxLength: number
  maxSize: number
  regex: string
  regexDesc: string

  suffixArray: object
  max: object
  min: object
}

export interface IFormItemVO {
  id: string

  perm: string

  icon: string

  name: string

  type: string
  required: boolean

  typeName: string

  placeholder: string

  props: IProps
}
