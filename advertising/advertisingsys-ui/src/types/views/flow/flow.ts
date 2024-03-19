/*
 * @Author: suny
 * @Date: 2023-10-12 09:28:57
 * @LastEditTime: 2023-10-14 16:10:43
 * @LastEditors: suny
 * @Description:
 */
import { TBaseDTO } from '@/types/common'

/** 流程详情对象 */
export interface IProcessVO {
  /**
   * 管理员
   */
  admin?: string
  /**
   * 流程管理员
   */
  adminId?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 逻辑删除字段
   */
  delFlag?: boolean
  flowId?: string
  /**
   * 表单设置内容
   */
  formItems?: string
  /**
   * 分组ID
   */
  groupId?: string
  groupName?: string
  /**
   * 0 正常 1=隐藏
   */
  hidden?: boolean
  /**
   * 用户id
   */
  id?: number
  /**
   * 图标配置
   */
  logo?: string
  /**
   * 表单名称
   */
  name?: string
  /**
   * 流程设置内容
   */
  process?: string
  /**
   * 范围描述显示
   */
  rangeShow?: string
  /**
   * 备注
   */
  remark?: string
  /**
   * 需要发起人选择的节点id
   */
  selectUserNodeId?: string[]
  /**
   * 设置项
   */
  settings?: string
  sort?: number
  /**
   * 0 正常 1=停用
   */
  stop?: boolean
  /**
   * 唯一性id
   */
  uniqueId?: string
  /**
   * 更新时间
   */
  updateTime?: string
  /**
   * java.util.Map<java.lang.String,java.lang.Object>
   */
  variableMap?: Object
}
