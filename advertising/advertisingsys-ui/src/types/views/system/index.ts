/*
 * @Author: wanghl
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-06 13:15:12
 * @LastEditors: songly
 * @Description:
 */
export type TSystemMenu = {
  /**
   * 是否需要在菜单列表中显示1显示0不显示(只赋权使用)
   */
  bmenushow?: boolean
  /**
   * 是否在菜单权限使用
   */
  broleflag?: boolean
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 主键
   */
  id?: number
  /**
   * 深度
   */
  idepth?: number
  /**
   * 序号
   */
  isort?: number
  /**
   * 父id
   */
  parentid?: number
  /**
   * 菜单所属模块
   */
  sclass?: string
  /**
   * 前端组件名称
   */
  scomponent?: string
  /**
   * url
   */
  sfunctionurl?: string
  /**
   * 菜单分组名称
   */
  sgroup?: string
  /**
   * 图片
   */
  simageurl?: string
  /**
   * 名称
   */
  sname?: string
  /**
   * 菜单权限使用别名
   */
  sothername?: string
  /**
   * 备注(用于记录菜单使用范围)
   */
  sremark?: string
  /**
   * 前端路由地址
   */
  srouterpath?: string
}
