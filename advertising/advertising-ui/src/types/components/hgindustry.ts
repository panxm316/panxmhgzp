/*
 * @Author: songly
 * @Date: 2023-09-01 10:52:01
 * @LastEditTime: 2023-11-14 17:05:20
 * @LastEditors: wanghl
 * @Description:日期类型 从汇畅复制
 */
export interface Tadindustrylist {
  bdefault?: string | boolean
  id: string
  name: string
}
/**
 * 获取流程类型
 */
export interface Tadindustry {
  adindustryid?: string
  adindustryname?: string
  show?: string
}
export type TypeCurrentData = {
  /**
   * 审核实例id
   */
  processInstanceId?: string
  /**
   * 流程id
   */
  flowId?: string
  /**
   * 流程类型
   */
  formData?: {}
  /**
   * 审核时间
   */
  createTime?: string
  /**
   * 审核结果
   */
  result?: string | boolean
}
