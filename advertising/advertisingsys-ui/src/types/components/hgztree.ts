import { EHgDeptZtreeUrl } from '@/types/common/enumindex'

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
}
