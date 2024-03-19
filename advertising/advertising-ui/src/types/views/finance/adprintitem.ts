/*
 * @Author: wanghl
 * @Date: 2023-08-30 13:12:25
 * @LastEditTime: 2023-11-01 13:25:00
 * @LastEditors: caogd
 * @Description:广告项目
 */

/**
 * 添加广告项目
 */
export interface IAdprintitem {
  /**
   * 是否默认
   */
  bdefault: boolean
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * id
   */
  id: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 名称
   */
  sname?: string
  /**
   * 税收编码
   */
  staxcode?: string
}
