/*
 * @Author: wanghl
 * @Date: 2023-12-06 10:21:07
 * @LastEditTime: 2023-12-06 10:22:35
 * @LastEditors: wanghl
 * @Description: 广告类别返回值类型
 */

/**
 * 广告类别返回值类型
 */
export type TAdtype = {
  /**
   * 是否默认
   */
  bdefault?: boolean
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * id
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 类型名称
   */
  sname?: string
  /**
   * 备注
   */
  sremark?: string
}
