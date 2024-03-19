/*
 * @Author: caogd
 * @Date: 2023-09-19 15:53:38
 * @LastEditTime: 2023-11-08 13:49:38
 * @LastEditors: caogd
 * @Description 广告规格类型
 */

import { TQueryInfo } from '@/types/common'

/**
 * 展示列表类型
 */
export type TAdspecModelVO = {
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * id
   */
  id?: string
  /**
   * 格子数
   */
  ipknum?: number
  /**
   * 是否分类广告
   */
  bclassified?: boolean

  /**
   * 是否大分类广告
   */
  bbigclassified?: boolean
  /**
   * 序号
   */
  isort?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 面积
   */
  narea?: number
  /**
   * 高
   */
  nheight?: number
  /**
   * 宽
   */
  nwidth?: number
  /**
   * 规格名称
   */
  sname?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 显示设置
   */
  sspecdisplay?: string
  /**
   * 类型
   */
  stype?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
}
/**
 * 添加修改类型
 */
export type TAdspec = {
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * 结束日期
   */
  denddate: string
  /**
   * 开始日期
   */
  dstartdate: string
  /**
   * id
   */
  id?: string
  /**
   * 格子数
   */
  ipknum?: number
  /**
   * 是否分类广告
   */
  bclassified?: boolean

  /**
   * 是否大分类广告
   */
  bbigclassified?: boolean
  /**
   * 序号
   */
  isort: number
  /**
   * 媒体id
   */
  mediaid: string
  /**
   * 面积
   */
  narea?: number
  /**
   * 高
   */
  nheight: number
  /**
   * 宽
   */
  nwidth: number
  /**
   * 规格名称
   */
  sname: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 显示设置
   */
  sspecdisplay?: string
  /**
   * 类型
   */
  stype?: string
}

export type TQAdspec = TQueryInfo & {
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
}

export type TAdspecDateModelDTO = {
  /**
   * 原结束日期
   */
  olddenddate?: string

  /**
   * 新结束日期
   */
  newdenddate?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
}
