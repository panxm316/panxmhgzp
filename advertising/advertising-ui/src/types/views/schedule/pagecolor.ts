/*
import { type } from './../../common/index';
 * @Author: wanghl
 * @Date: 2023-11-15 09:37:41
 * @LastEditTime: 2023-11-15 16:13:05
 * @LastEditors: wanghl
 * @Description:色彩结构类别
 */
/**
 * 编辑客户
 */
export type TAPageColor = {
  /**
   * 启用标志
   */
  buse?: boolean
  /**
   * 色彩串
   */
  colorlist?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 版面色彩列表
   */
  lsColors?: string[] | undefined
  /**
   * 色彩结构名称
   */
  sname?: string
  /**
   * 备注
   */
  sremark?: string
}
export type TQPageColor = {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  loginUserId?: string
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页
   */
  page?: number | undefined
  /**
   * 每页显示记录数
   */
  pageSize?: number | undefined
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 排序字段
   */
  sort?: string
  startTime?: string
}
