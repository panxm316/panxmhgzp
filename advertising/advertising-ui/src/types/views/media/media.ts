/*
 * @Author: wanghl
 * @Date: 2023-12-12 08:32:46
 * @LastEditTime: 2023-12-21 14:02:26
 * @LastEditors: wanghl
 * @Description: 媒体类型
 */

export type TAmedia = {
  /**
   * 启用标志
   */
  buse?: boolean
  /**
   * id
   */
  id?: string
  /**
   * 媒体序列号
   */
  isort?: number
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型
   */
  mediatypename?: string
  /**
   * 媒体编码
   */
  scode?: string
  /**
   * 媒体名称
   */
  sname?: string
  /**
   * 类型名称
   */
  name?: string
}
