import { TQueryInfo } from '@/types/common'

/*
 * @Author: caogd
 * @Date: 2023-09-07 13:42:31
 * @LastEditTime: 2023-09-08 15:45:51
 * @LastEditors: caogd
 * @Description: 媒体刊期类型定义
 */
/**
 * 保存展示对象类型
 */
export type Tmediapublicnum = {
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 发布日期
   */
  dpublishtime: string
  /**
   * 主键
   */
  id?: string
  /**
   * 媒体id
   */
  mediaid: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 发布期号
   */
  spublishno: string
}
/**
 * 查询对象类型
 */
export type TMediapublicnumQueryInfo = TQueryInfo & {
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 发布期号
   */
  spublishno?: string
}
/**
 * 批量新增类型
 */
export type TMediaPublicNumDto = {
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 结束日期
   */
  endTime: string
  /**
   * 媒体id
   */
  mediaid: string
  /**
   * 媒体名称
   */
  medianame: string
  /**
   * 刊期类型
   */
  mediapublictype: string
  /**
   * 起始期号
   */
  spublishno: string
  /**
   * 发布日期
   */
  spublishnum: string
  /**
   * 开始日期
   */
  startTime: string
}
