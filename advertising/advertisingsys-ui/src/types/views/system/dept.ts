/*
 * @Author: wanghl suny
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-06 13:48:36
 * @LastEditors: songly
 * @Description:
 */
/**
 * 系统管理部门对象
 */
export type TsystemDept = {
  /**
   * cas部门是否推送
   */
  bcaspush?: boolean
  /**
   * 是否有默认角色标志
   */
  bflagrole?: boolean
  /**
   * 是否导航节点
   */
  bflagroot?: boolean
  /**
   * 内外部部门标志
   */
  binner?: boolean
  /**
   * 启用标志
   */
  buse?: boolean
  /**
   * 主键
   */
  id: string
  /**
   * 数据版本号
   */
  idataversion?: number
  /**
   * 部门深度
   */
  idepth?: number
  /**
   * 序号
   */
  isort?: number
  /**
   * 父部门id
   */
  parentid: string
  /**
   * 部门别名
   */
  sdeptalias?: string
  /**
   * 部门名称
   */
  sdeptname?: string
  /**
   * cas部门id
   */
  sguidcas?: string
  /**
   * 域组织映射
   */
  sndomainou?: string
}

/**
 * 批量调整部门提交对象
 */
export type TbatchDeptData = {
  /** 调整的部门id字符串，“,”间隔 */
  ids: string
  /** 调整目标部门id */
  pid: string
}
