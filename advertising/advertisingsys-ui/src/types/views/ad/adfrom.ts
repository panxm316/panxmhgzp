/*
 * @Author: muyn
 * @Date: 2023-08-24 15:59:09
 * @LastEditTime: 2023-09-01 09:19:41
 * @LastEditors: muyn
 * @Description: 广告来源
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common/index'
export type TAdFrom = {
  /**
   * id
   */
  id: string
  /**
   * 父id
   */
  parentid: string
  /**
   * 来源名称
   */
  sname: string
  /**
   * 来源解释
   */
  sdesc: string
  /**
   * 本级id号
   */
  slocalid: string
  /**
   * 来源级别
   */
  idepth: number
  /**
   * 备注
   */
  sremark: string
  /**
   * 序号
   */
  isort: number
  /**
   * 是否有效
   */
  buse: boolean
}
export type TAdFromSearch = {
  sname: string
  buse: number | string
} & IPageRequest
