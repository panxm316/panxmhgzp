/*
 * @Author: songly
 * @Date: 2023-08-24 15:59:09
 * @LastEditTime: 2023-09-06 12:44:07
 * @LastEditors: songly
 * @Description: 广告类型
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common/index'
export type TAdType = {
  /**
   * 主键
   */
  id?: number
  /**
   * 名称
   */
  sname: string
  /**
   * 序号
   */
  isort: number
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 是否默认
   */
  bdefault: boolean
  /**
   * 备注(用于记录菜单使用范围)
   */
  sremark?: string
}
