/*
 * @Author: wanghl
 * @Date: 2023-08-23 12:41:52
 * @LastEditTime: 2023-09-06 13:14:33
 * @LastEditors: songly
 * @Description:菜单类型
 */
export type TMenu = {
  /**
   * 是否需要在菜单列表中显示1显示0不显示(只赋权使用)
   */
  bmenushow: boolean
  /**
   * 是否在菜单权限使用
   */
  broleflag?: boolean
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 主键
   */
  id: number | null | string
  /**
   * 深度
   */
  idepth: number
  /**
   * 序号
   */
  isort: number
  /**
   * 父id
   */
  parentid: number | null | string
  /**
   * 菜单所属模块
   */
  sclass?: string
  /**
   * 前端组件名称
   */
  scomponent: string
  /**
   * url
   */
  sfunctionurl: string
  /**
   * 菜单分组名称
   */
  sgroup: string
  /**
   * 图片
   */
  simageurl: string
  /**
   * 名称
   */
  sname?: string
  /**
   * 菜单权限使用别名
   */
  sothername: string
  /**
   * 备注(用于记录菜单使用范围)
   */
  sremark?: string
  /**
   * 前端路由地址
   */
  srouterpath: string
}
export type TMenuAction = {
  /**
   * 主键
   */
  id: string
  /**
   * 菜单id
   */
  menuid?: string
  /**
   * 名称
   */
  sname: string
  /**
   * 操作代码
   */
  skeycode: string
  /**
   * 设置中是否可以修改1可修改0不可修改
   */
  bmodify: boolean
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 序号
   */
  isort?: number
  /**
   * 范围id
   */
  scopeId?: string
  /**
   * 按钮url
   */
  sfunctionurl?: string
  /**
   * 按钮组，用于同组赋权
   */
  sgroupkey?: string
}
export type TCheckActionKey = {
  /**
   * 行为id
   */
  actionId: string
  /**
   * 行为
   */
  keyCode: string
}
