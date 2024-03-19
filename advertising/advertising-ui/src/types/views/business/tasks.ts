/**
 * @Author: lhl
 * @Date: 2023-11-07
 * @LastEditTime: 2023-11-07
 * @LastEditors: lhl
 * @Description: 任务设置常量
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'

export default {
  // 任务设置类型
  TaskType: [
    {
      id: '0',
      value: '部门'
    },
    {
      id: '1',
      value: '人员'
    }
  ],
  // 任务设置类型
  TaskCategory: [
    {
      id: '-1',
      value: '全部'
    },
    {
      id: '0',
      value: '年度'
    },
    {
      id: '1',
      value: '月度'
    }
  ],

  // 年度/月度
  DateType: [
    {
      id: '0',
      value: '年度'
    },
    {
      id: '1',
      value: '月度'
    }
  ],
  // 部门级别
  DepLevel: [
    {
      id: '0',
      value: '1级部门'
    },
    {
      id: '1',
      value: '2级部门'
    },
    {
      id: '2',
      value: '3级部门'
    },
    {
      id: '3',
      value: '4级部门'
    },
    {
      id: '4',
      value: '5级部门'
    }
  ],

  // 任务时间类型
  TaskTime: [
    {
      id: '0',
      value: '年度'
    },
    {
      id: '1',
      value: '月度'
    }
  ]
}

/**
 * 任务设置查询对象
 */

export interface ITaskQuery {
  /**
   * 任务设置类型 0：部门 1：人员
   */
  sTaskType?: string

  /**
   * 任务设置类别 0：年度 1：月度
   */
  sTaskCategory?: string

  /**
   * 媒体ID
   */

  medidId: string

  /**
   * 关键字
   */

  querykey: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
   */
  bcurflag?: string
  /**
   * 任务日期 年：2023 月2023-01
   */
  staskdate?: string
  /**
   * 按部门/人员查询（0：部门 1：人员，如果是人员则按照选择的部门查找所有部门下人员分别统计）
   */
  bdeptflag?: boolean

  /**
   * 排序字段 0：日期 1：部门名称 2：人员名称 3：媒体名称
   */
  sortType: number
  /**
   * 排序规则 0：升序  1：降序
   */
  sortRule: number
}

export type ITaskReportQuery = ITaskQuery & IBaseQueryInfo & IPageRequest

/**
 * 任务设置对象
 */
export interface ITaskReportsDTO {
  /**
   * 主键
   */
  id?: number

  /**
   * 操作员id
   */
  operatorid?: number

  /**
   * 操作员
   */
  operatorname?: string

  /**
   * 任务分类：年度、月度
   */
  stasktype?: string

  /**
   * 人员分类：部门、人员
   */
  spersonaltype?: string

  /**
   * 部门id
   */
  deptid?: number

  /**
   * 部门
   */
  deptname?: string

  /**
   * 人员id
   */
  employid?: number

  /**
   * 人员
   */
  employname?: string

  /**
   * 媒体id
   */
  mediaid: string

  /**
   * 媒体
   */
  medianame?: string

  /**
   * 任务日期 年：2023 月2023-01
   */
  staskdate?: string

  /**
   * 任务额度（万元）
   */
  ntaskamount: number

  /**
   * 创建日期
   */
  dcreatetime?: string

  /**
   * 启用标记
   */
  buse?: boolean

  /**
   * 备注
   */
  sremark?: string
}

/**
 * 批量添加请求对象
 */
export interface ITaskQuotaAddDTO {
  /**
   * 任务额度类型 0：部门 1：人员
   */
  sTaskType: string

  /**
   * 任务额度类别 0：年度 1：月度
   */
  sCategoryType?: number | string

  /**
   * 起始年度
   */
  sStartYear: string

  /**
   * 终止年度
   */
  sEndYear: string

  /**
   * 额度
   */
  dQuota: number

  /**
   * 备注
   */
  remarks?: string

  /**
   * id列表
   */
  idList?: any

  /**
   * 媒体id
   */
  mediaId: string
}

/**
 * 批量更新请求对象
 */
export interface ITaskQuotaUpdateDTO {
  /**
   * 额度
   */
  dQuota: number

  /**
   * 备注
   */
  sremark?: string

  /**
   * id列表
   */
  idList?: any
}

/**
 * 任务额度实体对象
 */
export interface TwtaskDTO {
  /**
   * 主键
   */
  id?: any
  /**
   * 操作员id
   */
  operatorid?: any

  /**
   * 操作员
   */
  operatorname?: string

  /**
   * 任务分类：年度、月度
   */
  stasktype?: string

  /**
   * 人员分类：部门、人员
   */
  spersonaltype?: string

  /**
   * 部门id
   */
  deptid?: any

  /**
   * 部门
   */
  deptname?: string

  /**
   * 人员id
   */
  employid: string

  /**
   * 人员
   */
  employname?: string

  /**
   * 媒体id
   */
  mediaid?: any

  /**
   * 媒体
   */
  medianame?: string

  /**
   * 任务日期 年：2023 月2023-01
   */
  staskdate?: any

  /**
   * 任务额度（万元）
   */
  ntaskamount?: number

  /**
   * 创建日期
   */
  dcreatetime?: any

  /**
   * 启用标记
   */
  buse?: any

  /**
   * 备注
   */
  sremark?: string
}

/**
 * 任务额度通知发送请求对象
 */
export interface TwtaskMessageDTO {
  /**
   * 消息接收类型 0：部门 1：人员
   */
  messagetype?: string
  /**
   * 部门id
   */
  departid?: string

  /**
   * 人员ID
   */
  emplyeid?: string

  /**
   * 消息标题
   */
  title?: string

  /**
   * 消息内容
   */
  content?: string

  /**
   * 消息内容
   */
  departName?: string
}

/**
 * MyTaskDTO
 * 创建人：suny
 * 类描述：我的任务统计DTO
 * 创建日期：2024/1/23 14:26
 *
 * MyTaskDTO
 */
export interface IMyTaskDTO {
  /**
   * 签订金额
   */
  amountreceivable?: number
  /**
   * 业绩金额
   */
  amountachievement?: number
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门
   */
  deptname?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 人员
   */
  employname?: string
  /**
   * 完成比例(业绩金额/任务额度)
   */
  nfinishrate?: number
  /**
   * 任务额度（万元）
   */
  ntaskamount?: number
  /**
   * 人员分类：部门、人员
   */
  spersonaltype?: string
  /**
   * 任务日期 年：2023 月2023-01
   */
  staskdate?: string
  /**
   * 任务分类：年度、月度
   */
  stasktype?: string
}
