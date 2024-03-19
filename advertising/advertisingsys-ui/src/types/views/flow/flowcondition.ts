/*
 * @Author: songly
 * @Date: 2023-10-12 11:06:13
 * @LastEditTime: 2023-10-19 16:10:52
 * @LastEditors: songly
 * @Description:工作流条件
 */

import { IDataCombo } from '@/types/common/DataCombo'
import { TQueryInfo } from '@/types/common/index'
export type TFlowCondition = {
  /**
   * id
   */
  id?: string
  /**
   * 条件名称
   */
  sname: string

  /**
   * 条件key(流程申请DTO对象列名)
   */
  sconditionkey: string

  /**
   * 条件类型(Money、SelectMultiDept、SelectMultiUser)
   */
  sconditiontype: string
  /**
   * 条件数据表名
   */
  sconditiontable: string

  /**
   * 流程类型
   */
  sflowtype: string

  /**
   * 序号
   */
  isort: number

  /**
   * 启用
   */
  buse: boolean
  /**
   * 条件类型名称
   */
  sconditiontypename: string

  /**
   * 流程类型名称
   */
  sflowtypename: string
}
export type TFlowConditionSearch = {
  buse: number | string
  sconditiontype: string
  sflowtype: string
} & TQueryInfo
export type TFlowConditionEx = TFlowCondition & { data: IDataCombo[] }
