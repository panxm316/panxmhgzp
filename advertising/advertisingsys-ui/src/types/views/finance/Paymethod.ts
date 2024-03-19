/*
 * @Author: wanghl
 * @Date: 2023-08-29 15:55:18
 * @LastEditTime: 2023-09-06 12:44:38
 * @LastEditors: songly
 * @Description:付款方式
 */

/**
 * 付款方式返回类
 */
export interface IPaymethod {
  /* 是否启用
   */
  buse: boolean
  /**
   * id
   */
  id?: string
  /**
   * 序号
   */
  isort: number
  /**
   * 方式描述
   */
  sdesc: string
  /**
   * 方式名称
   */
  sname: string
  /**
   * 备注
   */
  sremark: string
  /**
   * 默认
   */
  bdefault?: boolean
}
