/*
 * @Author: caogd
 * @Date: 2023-08-29 17:07:52
 * @LastEditTime: 2023-09-06 08:52:45
 * @LastEditors: peij
 * @Description: 公共枚举
 */

/**
 * 部门树URL标志枚举 命名规则url进行拼接
 */
export enum EHgDeptZtreeUrl {
  /** 部门管理查询所有部门树 */
  DEPT_GETSYSDEPTTREE,
  /** 人员管理显示的部门树 */
  DEPT_GETSYSEMPDEPTTREE,
  /** 媒体信息树 */
  MEDIA_GETSYSMEDIATREE
}

/**
 * 角色按钮操作标示枚举
 */
export enum EHgActiveOperation {
  /** 查询操作 */
  QUERY = 'query',
  /** 修改操作 */
  MODIFY = 'modify'
}
