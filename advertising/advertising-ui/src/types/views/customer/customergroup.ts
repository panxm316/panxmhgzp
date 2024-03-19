/*
import { type } from './../../../utils/type/index';
 * @Author: wanghl
 * @Date: 2023-11-24 15:30:46
 * @LastEditTime: 2024-03-06 19:46:46
 * @LastEditors: songly
 * @Description: 客户属性设置
 */

/**
 * 客户分类相关实体对象
 */
export type TAdCustomerCategory = {
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 分类名称
   */
  categoryname?: string
  /**
   * 创建id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建时间
   */
  createtime?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 是否需要审核
   */
  bapprove?: boolean
}
/**
 * 客户状态相关实体对象
 */
export type TAdCustomerStatus = {
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 创建id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建时间
   */
  createtime?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 状态名称
   */
  statusname?: string
}
/**
 * 客户信誉度相关实体对象
 */
export type TAdCustomerCredit = {
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 创建id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建时间
   */
  createtime?: string
  /**
   * 信誉度名称
   */
  creditname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 备注
   */
  sremark?: string
}
