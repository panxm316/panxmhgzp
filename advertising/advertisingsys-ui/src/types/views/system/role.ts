/*
 * @Author: peij
 * @Date: 2023-08-25 14:58:42
 * @LastEditTime: 2023-09-16 09:31:55
 * @LastEditors: peij
 * @Description: 角色类型
 */
import { TQueryInfo, TBaseDTO } from '@/types/common'
import { EHgActiveOperation } from '@/types/common/enumindex'
import { THgZtreeProps } from '@/types/components/hgztree'
/**
 * 角色查询
 */
export type TQRole = TQueryInfo & {
  sname: string
}

/**
 * 列表返回
 */
export type TRoleVO = {
  /**
   * 是否全部权限
   */
  ball: boolean
  /**
   * 是否单独赋权
   */
  bselfrole: boolean
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 角色关联的部门名称
   */
  deptNames: string
  /**
   * 单服赋权时的人员id
   */
  employId: string
  /**
   * 主键
   */
  id: string
  /**
   * 角色类型：0-普通，1-子报管理员，2-超级管理员
   */
  iroletype: null
  /**
   * 序号
   */
  isort: number
  /**
   * 菜单id
   */
  menuIds: string
  /**
   * 菜单名称
   */
  menuNames: string
  /**
   * 说明
   */
  sdesc: string
  /**
   * 名称
   */
  sname: string
}

/**
 * 角色子页面传递对象
 */
export type TRolePage = {
  /**
   * 角色id
   */
  roleId: string
  /**
   * 角色名称
   */
  roleName: string
  /**
   * 角色列表当前页号
   */
  pageIndex: number
  /**
   * 跳转组件名
   */
  compName: string
}

/**
 * 角色保存类型
 */
export type TRoleDTO = {
  id: string
  /**
   * 名称
   */
  sname: string
  /**
   * 菜单id
   */
  menuIds: string
  /**
   * 是否全部权限
   */
  ball: boolean
  /**
   * 是否单独赋权
   */
  bselfrole: boolean
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 单独赋权时的人员id
   */
  employId: string
  /**
   * 角色类型：0-普通，1-子报管理员，2-超级管理员
   */
  iroletype: null
  /**
   * 序号
   */
  isort: number
  /**
   * 说明
   */
  sdesc: string
} & TBaseDTO

/**
 * RoleMenuModel
 */
export type TRoleMenuModel = {
  /**
   * 菜单包含的按钮数组
   */
  actives: TActiveModel[]
  /**
   * 是否选中
   */
  checked: boolean
  /**
   * 菜单id
   */
  menuid: string
  /**
   * 菜单别名
   */
  menuname: string
  /**
   * 菜单分组名称
   */
  menutype: string
}

/**
 * 菜单包含的按钮
 *
 * ActiveModel
 */
export type TActiveModel = {
  /**
   * 操作代码
   */
  activekeycode: string
  /**
   * 按钮名称
   */
  activename: string
  /**
   * 按钮范围分类
   */
  activeScopeClass: string
  /**
   * 是否全部
   */
  ball: boolean
  /**
   * 能否修改
   */
  bmodify: boolean
  /**
   * 是否选中
   */
  checked: boolean
  /**
   * 部门深度
   */
  idepth: number
  /**
   * 范围表id
   */
  scopeid: string
  /**
   * 范围数组
   */
  scopes: TScopeModel[]
  /**
   * 按钮url
   */
  sfunctionurl: string
  /**
   * 按钮组，用于同组赋权
   */
  sgroupkey: string
  /**
   * 是否树显示
   */
  treeScope: boolean
  /**
   *
   */
  isshow: boolean
  /**
   * 页面隐藏按钮dialog弹出单独赋权
   */
  ishide?: boolean
}

/**
 * 范围
 *
 * ScopeModel
 */
export type TScopeModel = {
  /**
   * 业务id
   */
  businessid: string
  /**
   * 业务名称
   */
  businessname: string
  /**
   * 构造树的父id
   */
  businesspid?: string
  /**
   * 是否选中
   */
  checked: boolean
  /**
   * 图标
   */
  iconSkin?: string
  /**
   * 范围id
   */
  scopeid: string
}

/**
 * 按钮与选择范围传递类型
 */
export type TActiveScope = THgZtreeProps & {
  /** 菜单id */
  menuid: string
  /** 按钮keycode */
  activekeycode: string
  /** 树宽度 */
  idepth: number
  /** 范围id */
  ids: string
  /** 应用到其他范围 */
  isapplyother: boolean
  /** 范围id */
  scopeid: string
  /** 范围名称 */
  names: string
  /** 操作按钮 */
  checkoperationList: EHgActiveOperation[]
}

/**
 * 菜单按钮范围更新类型
 */
export type TRoleActionDTO = {
  roleId: string
  roleMenuModelList: TRoleMenuModel[]
} & TBaseDTO
