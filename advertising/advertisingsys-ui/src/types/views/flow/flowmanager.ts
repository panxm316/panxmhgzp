/*
 * @Author: songly
 * @Date: 2024-02-16 11:01:05
 * @LastEditTime: 2024-02-16 13:31:53
 * @LastEditors: songly
 * @Description:流程类型
 */
export type TFlowType = {
  /**
   * id
   */
  id?: string
  /**
   * 基础流程名称
   */
  sname: string
  /**
   * 流程关键字
   */
  skey: string
  /**
   * 描述
   */
  sdesc: string
  /**
   * 排序
   */
  isort: number
  /**
   * 是否启用
   */
  bactive: boolean

  /**
   * 是否启用
   */
  buse: boolean
}
