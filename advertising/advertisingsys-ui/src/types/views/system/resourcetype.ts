/*
 * @Author: caogd
 * @Date: 2023-10-27 10:52:32
 * @LastEditTime: 2023-10-27 10:55:16
 * @LastEditors: caogd
 * @Description: 资源类型type
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
export interface IResourceType {
  /**
   * 启用
   */
  buse?: boolean
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 文件格式
   */
  sfileformat?: string
  /**
   * 类型名称
   */
  sname: string
  /**
   * 中文类型名称
   */
  stypename: string
}
export type IResourceTypeQuery = IPageRequest & IBaseQueryInfo
