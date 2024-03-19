/*
 * @Author: wanghl
 * @Date: 2024-03-08 10:21:15
 * @LastEditTime: 2024-03-08 14:37:22
 * @LastEditors: wanghl
 * @Description:常用审批意见类型
 */
/**
 * 查询、展示意见分页列表
 */
export type TwOpinionsPageList = {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 是否用于同意 0-否决 1 -同意
   */
  ipasstype?: number | string
  loginUserId?: string
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页
   */
  page?: number
  /**
   * 每页显示记录数
   */
  pageSize?: number
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 审批流程类型
   */
  sflowtype?: string
  /**
   * 排序字段
   */
  sort?: string
  startTime?: string
}

/**
 * 查询、展示意见列表
 */
export type TwOpinionsList = {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 是否用于同意 0-否决 1 -同意
   */
  ipasstype?: number | string
  loginUserId?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 审批流程类型
   */
  sflowtype?: string
  startTime?: string
}
/**
 * 新增,修改意见列表
 */
export type TwsaveOpinions = {
  /**
   * 是否通用
   */
  bcommon?: boolean
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 创建日期
   */
  createdate?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * id
   */
  id?: string
  /**
   * 是否用于同意 0-否决 1 -同意
   */
  ipasstype?: number
  /**
   * 排序
   */
  isort?: number
  /**
   * 审批流程类型
   */
  sflowtype?: string
  /**
   * 意见内容
   */
  sopinion?: string
}
/**
 * 意见列表列表类型
 */
export type TwopinionsList = {
  /**
   * 是否通用
   */
  bcommon?: boolean
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 创建日期
   */
  createdate?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * id
   */
  id?: string
  /**
   * 是否用于同意 0-否决 1 -同意
   */
  ipasstype?: number | string
  /**
   * 排序
   */
  isort?: number
  /**
   * 审批流程类型
   */
  sflowtype?: string
  /**
   * 审批流程类型
   */
  sflowtypename?: string
  /**
   * 意见内容
   */
  sopinion?: string
}
