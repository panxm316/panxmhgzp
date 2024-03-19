import { type } from 'jquery'
/*
 * @Author: suny
 * @Date: 2023-11-11 15:16:08
 * @LastEditTime: 2024-03-07 16:47:20
 * @LastEditors: songly
 * @Description: 广告资源申请表
 */

import { IAdResourceFilesDTO } from './adresourcefiles'
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/** 广告资源申请表查询对象 */
export type TAdResourceApplicationQuery = IAdResourceApplicationVO & IPageRequest & IBaseQueryInfo

export interface IAdResourceApplicationVO {
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户ids
   */
  customerIds?: string[]
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
   */
  iapprovestatus?: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: string
}

/**
 * 广告资源申请表对象
 */
export interface IAdResourceApplicationDTO {
  /**
   * 流程id
   */
  flowid?: string
  /**
   * 申请表id
   */
  applicationid?: string
  /**
   * 是否快速上版标记
   */
  bquickly?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 第三方合同ID(预留)
   */
  contractid?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername: string
  /**
   * 申请时间
   */
  dapplytime?: string
  /**
   * 停止使用日期
   */
  denddate?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 开始使用日期
   */
  dstartdate?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 广告资源文件列表
   */
  filelist?: IAdResourceFilesDTO[]
  /**
   * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
   */
  iapprovestatus?: number
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 广告内容
   */
  sadcontent?: string
  /**
   * 广告标题
   */
  sadtitle?: string
  /**
   * 最后一次审批意见
   */
  sapprovalopinions?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 一审意见
   */
  sfirstopinion?: string
  /**
   * 并发标志
   */
  version?: number
}

/**
 * 广告资源申请表 弹出框修改模式
 */
export enum ResourceApplicationDialogMode {
  /** 新增 */
  Add,
  /** 修改 */
  Edit,
  /** 来自广告预约的新增 */
  PreOrderAdd,
  /** 来自广告预约的修改 */
  PreOrderEdit
}
