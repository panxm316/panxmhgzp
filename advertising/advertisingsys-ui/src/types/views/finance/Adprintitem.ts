/*
 * @Author: wanghl
 * @Date: 2023-08-30 13:12:25
 * @LastEditTime: 2023-11-01 13:01:00
 * @LastEditors: wanghl
 * @Description:广告项目
 */
import { IPageRequest } from '@/types/common/index'
/**
 *查询广告项目
 */
export type TAdprintitemQ = {
  /**
   * 名称
   */
  sname?: string
  /**
   * 排序字段
   */
  buse?: string
} & IPageRequest
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
  id?: string
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
