/*
 * @Author: songly
 * @Date: 2023-10-12 11:06:13
 * @LastEditTime: 2024-03-16 08:58:03
 * @LastEditors: wanghl
 * @Description:订单增删改审核流程类型
 */

import { TQueryInfo } from '@/types/common/index'
/**
 * 新增类型
 */
export type TAddFlowOrder = {
  /**
   * 审批流程id
   */
  applicationid?: string
  /**
   * 审批类型
   */
  approvetype?: string
  /**
   * 审批类型名称
   */
  approvetypename?: string
  /**
   * 是否调整归属
   */
  bchangebelong?: boolean
  /**
   * 是否改加刊
   */
  bchangeitem?: string
  /**
   * 是否停刊
   */
  bstopitem?: string
  /**
   * 业务从id(比如orderitemid)
   */
  childid?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者
   */
  createempname?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 最后修改时间
   */
  dlastmodifydate?: string
  /**
   * 流程类型
   */
  flowtype?: string
  /**
   * 流程类型名称
   */
  flowtypename?: string
  /**
   * 审批状态
   */
  iapprovestatus?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 业务主id(比如orderid)
   */
  mainid?: string
  /**
   * 新经营主体id
   */
  newbusinessentityid?: string
  /**
   * 新客户id
   */
  newcustomerid?: string
  /**
   * 最后审批意见
   */
  sapprovalopinions?: string
  /**
   * 调整要求
   */
  schangecontent?: string
  /**
   * 调整原因
   */
  schangereason?: string
}
/**
 * 修改类型
 */
export type TDEditFlowOrder = {
  /**
   * 审批流程id
   */
  applicationid?: string
  /**
   * 审批类型
   */
  approvetype?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 业务从id(比如orderitemid)
   */
  childid?: string
  /**
   * 流程类型
   */
  flowtype?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 业务主id(比如orderid)
   */
  mainid?: string
  /**
   * 新经营主体id
   */
  newbusinessentityid?: string
  /**
   * 新客户id
   */
  newcustomerid?: string
}
/**
 * 删除类型
 */
export type TDeleteFlowOrder = {
  /**
   * 审批流程id
   */
  applicationid?: string
  /**
   * 审批类型
   */
  approvetype?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 业务从id(比如orderitemid)
   */
  childid?: string
  /**
   * 流程类型
   */
  flowtype?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 业务主id(比如orderid)
   */
  mainid?: string
  /**
   * 新经营主体id
   */
  newbusinessentityid?: string
  /**
   * 新客户id
   */
  newcustomerid?: string
}
/**
 * 根据某些值查询审批流程关联信息
类型
 */
export type TQFlowOrderdata = {
  /**
   * 审批流程id
   */
  applicationid?: string
  /**
   * 审批类型
   */
  approvetype?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 业务从id(比如orderitemid)
   */
  childid?: string
  /**
   * 流程类型
   */
  flowtype?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 业务主id(比如orderid)
   */
  mainid?: string
  /**
   * 新经营主体id
   */
  newbusinessentityid?: string
  /**
   * 新客户id
   */
  newcustomerid?: string
}
/**
 * 根据的某些值查询审批流程关联信息列表
 */
export type TQFlowOrderlist = {
  /**
   * 审批流程id
   */
  applicationid?: string
  /**
   * 审批类型
   */
  approvetype?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 业务从id(比如orderitemid)
   */
  childid?: string
  /**
   * 流程类型
   */
  flowtype?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 业务主id(比如orderid)
   */
  mainid?: string
  /**
   * 新经营主体id
   */
  newbusinessentityid?: string
  /**
   * 新客户id
   */
  newcustomerid?: string
}
/**
 * 根据某些值查询审批流程关联信息分页数据
 */
export type TQFlowOrderlistpage = {
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页
   */
  page?: string
  /**
   * 每页显示记录数
   */
  pageSize?: string
  /**
   * 排序字段
   */
  sort?: string
}
