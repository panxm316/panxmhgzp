/*
 * @Author: wanghl
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-15 15:24:18
 * @LastEditors: caogd
 * @Description:
 */

import { TBaseDTO, TQueryInfo } from '@/types/common/index'

/**
 * @description: 人员列表展示类型
 * @return {*}
 */
export type TSEmployVO = TBaseDTO & {
  id?: string
  /**
   * 是否管理员0：一般人员；1：普通管理员；2：超级管理员 3：兼职超管
   */
  badminflag?: number
  /**
   * cas人员是否推送
   */
  bcaspush?: boolean
  /**
   * 内外部我员标志内部为1
   */
  binner?: boolean
  /**
   * 是否单独赋权
   */
  bselfrole?: boolean
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 部门id
   */
  deptid: string
  /**
   * 部门名称
   */
  deptName?: string
  /**
   * 上次密码修改时间
   */
  dpasslastmodtime?: string
  /**
   * 查询结束时间
   */
  endTime?: string

  /**
   * 序号
   */
  isort?: number
  /**
   * 兼职部门id
   */
  parttimedeptid?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 角色id
   */
  roleIds?: string
  /**
   * 角色名称
   */
  roleNames?: string
  /**
   * 个推id
   */
  sclientid?: string
  /**
   * 职务
   */
  dutiesid?: string
  /**
   * 职务名称
   */
  sdutiesname?: string
  /**
   * cas人员id
   */
  sguidcas?: string
  /**
   * 头像
   */
  simg?: string
  /**
   * 登录名
   */
  sloginname?: string
  /**
   * 兼职部门
   */
  snameparttimedept?: string
  /**
   * 密码
   */
  spassword?: string
  /**
   * 电话
   */
  sphone?: string
  /**
   * 姓名
   */
  susername?: string
  /**
   * 微信号
   */
  sweixin?: string
  /**
   * 是否负责人
   */
  blead?: boolean
  /**
   * 是否业务员
   */
  bsalesman?: boolean
  /**
   * 子管理员管理部门
   */
  smanagedepts?: string
  /**
   * 性别
   */
  bsex?: boolean
  /**
   * 邮箱
   */
  semail?: string
}

export type TQEmployVO = TQueryInfo & {
  /**
   * 部门id
   */
  deptid?: string | undefined
}
/**
 * EmployDTO
 */
export type TEmployDTO = TBaseDTO & {
  id?: string
  /**
   * 部门id
   */
  deptid: string
  /**
   * 登录名
   */
  sloginname: string
  /**
   * 密码
   */
  spassword: string
  /**
   * 电话
   */
  sphone: string
  /**
   * 姓名
   */
  susername: string
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * 是否管理员0：一般人员；1：普通管理员；2：超级管理员；3：兼职超管
   */
  badminflag: number
  /**
   * cas人员是否推送
   */
  bcaspush?: boolean
  /**
   * 内外部我员标志内部为1
   */
  binner?: boolean
  /**
   * 是否单独赋权
   */
  bselfrole?: boolean
  /**
   * 上次密码修改时间
   */
  dpasslastmodtime?: string
  /**
   * 序号
   */
  isort?: number
  /**
   * 兼职部门id
   */
  parttimedeptid?: string
  /**
   * 角色id
   */
  roleIds?: string
  /**
   * 个推id
   */
  sclientid?: string

  /**
   * cas人员id
   */
  sguidcas?: string
  /**
   * 头像
   */
  simg?: string
  /**
   * 兼职部门
   */
  snameparttimedept?: string
  /**
   * 微信号
   */
  sweixin?: string
  /**
   * 是否负责人
   */
  blead?: boolean
  /**
   * 职务
   */
  dutiesid?: string
  /**
   * 职务名称
   */
  sdutiesname?: string
  /**
   * 是否业务员
   */
  bsalesman: boolean
  /**
   * 子管理员管理部门
   */
  smanagedepts?: string
  /**
   * 性别
   */
  bsex: boolean
  /**
   * 邮箱
   */
  semail?: string
}
/**
 * 根据角色查询人员列表的查询参数类型
 */
export type TQEmployByRoleVO = TQueryInfo & {
  /**
   * 部门id
   */
  deptid?: string | undefined
  /**
   * 角色id
   */
  roleIds?: string
}
