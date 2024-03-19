/*
 * @Author: songly
 * @Date: 2023-09-19 10:49:17
 * @LastEditTime: 2023-11-08 12:36:56
 * @LastEditors: songly
 * @Description:叠次版本类型
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { type } from 'jquery'
export interface IFoldAreaver {
  /**
   * id
   */
  id?: string

  /**
   * 叠次版本名称
   */
  sname: string

  /**
   * 叠次id
   */
  foldid: string

  /**
   * 开始日期
   */
  dstartdate: Date | string

  /**
   * 结束日期
   */
  denddate: Date | string

  /**
   * 备注
   */
  sremark: string

  /**
   * 序号
   */
  isort: number

  /**
   * 启用标志
   */
  buse: boolean
  /**
   * 媒体类型key
   */
  mediatypekey: string
}
export type TFoldAreaverVo = IFoldAreaver & {
  foldname: string
  medianame: String
  mediaid: string
}
/** 叠次版本查询实体类 */
export type TFoldAreaverQuery = IPageRequest & IBaseQueryInfo
