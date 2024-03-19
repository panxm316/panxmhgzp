/*
 * @Author: songly
 * @Date: 2023-08-25 09:37:30
 * @LastEditTime: 2023-08-25 09:48:28
 * @LastEditors: songly
 * @Description:打包类型
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common/index'
export type TPricePackageType = {
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
   * 次数
   */
  iitemcount: number
}
