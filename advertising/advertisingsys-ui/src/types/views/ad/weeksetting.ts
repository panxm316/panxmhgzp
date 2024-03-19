/*
 * @Author: songly
 * @Date: 2023-08-28 16:27:30
 * @LastEditTime: 2023-09-06 12:39:24
 * @LastEditors: songly
 * @Description:星期定义
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common/index'
export type TWeekSetting = {
  /**
   * 主键
   */
  id?: number
  /**
   * 名称
   */
  sname: string
  /**
   * 数值(1;2;3;4;5;6;7/4)
   */
  svalue: string
  /**
   * 序号
   */
  isort: number
  /**
   * 是否有效
   */
  buse?: boolean
}
