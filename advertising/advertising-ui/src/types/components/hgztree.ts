/*
 * @Author: suny
 * @Date: 2023-11-07 09:58:23
 * @LastEditTime: 2023-11-13 17:12:43
 * @LastEditors: caogd
 * @Description:
 */
import { EHgDeptZtreeUrl, EHgSingleZtreeUrl } from '@/types/common/enumindex'

/**
 * 树组件传入对象
 */
export type THgZtreeProps = {
  /** 显示更多操作 true 为显示 */
  operate?: boolean
  /** tree开启多选 true为多选 */
  checkboxtype?: boolean
  /** 传入需要选中的id */
  selectids?: string[]
  /** 传入对应标志表示请求url（使用枚举值 EHgDeptZtreeUrl 定义） */
  scopeflag?: EHgDeptZtreeUrl
  /** 树传入高度 */
  treeheight?: number
  /** 树查询参数 */
  treeparams?: THgTreeParams
  /** 是否开启查询筛选 默认开启*/
  filterable?: boolean
}
/**
 * 树组件传入查询参数的类型
 */
export type THgTreeParams = {
  /** 是否显示 【所有部门】节点 */
  showRoot?: boolean
  /** 要排除不显示的id字符串，多个用逗号分隔 */
  excludeIds?: string
  /** 是否忽略权限，true：忽略权限，false：需要权限  人员接口*/
  bIgnorePermissions?: boolean
  /** 是否显示“未启用”部门，true显示，false不显示 */
  showInactiveDepts?: boolean
}
/**
 * 单选树传入props对象
 */
export type THgSingleZtreeProps = {
  /** 树传入高度 */
  treeheight?: number
  /** 传入对应标志表示请求url（使用枚举值 EHgSingleZtreeUrl 定义） */
  scopeflag?: EHgSingleZtreeUrl
  /** 是否显示icon */
  showIcon?: boolean
  /** 树的查询参数都由此对象传入 */
  treeparams?: THgSingleZtreeParams
}
/**
 * 单选树请求传入参数
 */
export type THgSingleZtreeParams = {
  /** 是否显示 【所有】节点 */
  showRoot?: boolean
  /** 媒体类型 */
  mediaType?: string
  /** 媒体id */
  mediaId?: string
  /** 叠次id */
  foldId?: string
  /** 预刊发日期*/
  publishdate?: string
}
