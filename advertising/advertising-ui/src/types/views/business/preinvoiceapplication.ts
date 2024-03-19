/*
 * @Author: wanghl
 * @Date: 2023-11-08 13:36:51
 * @LastEditTime: 2024-03-11 18:25:13
 * @LastEditors: yanz
 * @Description:预开发票申请相关类型
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { TworderforPreinvoapplyVO } from '../ad/adorder'

/**
 * 预开发票申请查询、展示对象
 */
export interface IPreInvoiceApplicationVO {
  /**
   * 经营主体ID
   */
  businessentityid?: string
  /**
   * 经营主体名称
   */
  businessentityname?: string
  /**
   * 订单id
   */
  orderid?: number
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 客户识别号
   */
  spayercreditcode?: string
  /**
   * 客户地址电话
   */
  spayeraddr?: string
  /**
   * 客户银行账户
   */
  spayerbank?: string
  /**
   * 申请日期
   */
  dapplytime?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 审批状态
   */
  iapprovestatus?: number
  /**
   * 客户类型
   */
  icusttype?: number
  /**
   * 申请id
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 申请类型： 1-预开 2-挂开
   */
  iapplytype?: number
  /**
   * 开票类型(82-数电普票 81-数电专票)
   */
  iinvoicetype?: number
  /**
   * 申请金额
   */
  namountapply: number
  /**
   * 已还金额
   */
  namountreceived?: number
  /**
   * 开票项目id
   */
  printitemid?: string
  /**
   * 开票项目名称
   */
  printitemname?: string
  /**
   * 开票名称
   */
  sprintname?: string
  /**
   * 相关材料
   */
  applyfiles?: IPreInvoiceApplicationFile[]
  /**
   * 仅欠款
   */
  debtOnly?: boolean
  /**
   * 历史流程
   */
  customerProcessInstance?: []
  /**
   * 审批意见
   */
  sapprovalopinions?: string
}

export type TPreInvoiceApplicationQuery = IPreInvoiceApplicationVO & IBaseQueryInfo & IPageRequest
/** 发票打印查询对象 */
export type TPreInvoiceQuery = IBaseQueryInfo & IPageRequest

/**
 * 预开发票申请提交类型
 */
export interface IPreInvoiceApplicationDTO {
  /**
   * id
   */
  id?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 广告项目id
   */
  adprojectid?: string
  /**
   * 广告项目名称
   */
  adprojectname?: string
  /**
   * 订单id
   */
  orderid?: number
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: number
  /**
   * 合同号
   */
  contractVos?: TworderforPreinvoapplyVO[]
  /**
   * 申请时间
   */
  dapplytime?: string
  /**
   * 真实开票日期（关联发票表读取）
   */
  realInvoiceDate?: string
  /**
   * 申请金额
   */
  namountapply: number
  /**
   * 开票项目id
   */
  printitemid?: string
  /**
   * 开票项目
   */
  printitemname?: string
  /**
   * 已还金额
   */
  amountreceived?: number
  /**
   * 申请类型 1-预开 2-挂开
   */
  iapplytype?: number
  /**
   * 开票类型(82-数电普票 81-数电专票)
   */
  iinvoicetype?: number
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票编号
   */
  invoicecode?: string
  /**
   * 开票名称
   */
  sprintname?: string
  /**
   * 客户识别号
   */
  spayercreditcode?: string
  /**
   * 客户地址电话
   */
  spayeraddr?: string
  /**
   * 客户银行账户
   */
  spayerbank?: string
  /**
   * 经营主体id
   */
  businessentityid?: string
  /**
   * 经营主体名称
   */
  businessentityname?: string
  /**
   * 申请表id(流程表)
   */
  applicationid?: string
  /**
   * 上传的附件
   */
  applyfiles?: IPreInvoiceApplicationFile[]
  // /**
  //  * 审批状态(0待审、1在审、2通过、3否决、4撤销、5无效)
  //  */
  // iapprovestatus?: number
  /**
   * 备注
   */
  sremark?: string
  /**
   * 预开发票申请并发标志
   */
  version?: number
}

/**
 * 预开发票相关附件
 */
export interface IPreInvoiceApplicationFile {
  /**
   * 主键
   */
  id?: string
  /**
   * 预开发票申请表id
   */
  applicationid?: string
  /**
   * 创建人id
   */
  createempid?: string
  /**
   * 文件格式
   */
  sfileformat?: string
  /**
   * 统一文件id
   */
  sfileid?: string
  /**
   * 文件大小
   */
  sfilesize?: number
  /**
   * 源文件名
   */
  soriginalfile?: string
  /**
   * 上传时间
   */
  uploadtime?: string
  /**
   * 文件格式
   */
  sfiletype?: string
  /**
   * 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
   */
  ifilecategory?: number
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 文件描述
   */
  description?: string
  /**
   * 文件统一文件地址
   */
  url?: string
  /**
   * 预览链接
   */
  durl?: string
}

/**
 * 预开发票申请查询、展示对象
 */
export interface IPreinvoiceApplicationDetail {
  /**
   * ID
   */
  id?: string
  /**
   * 预开发票申请ID
   */
  preinvoapplicationid?: string
  /**
   * 广告合同ID（将来需要联系合同表）
   */
  contractid: string
  /**
   * 欠款金额
   */
  namountarrearage?: number
}

export type TPreinvoiceApplicationDetailQuery = IPreinvoiceApplicationDetail &
  IBaseQueryInfo &
  IPageRequest

/**
 * 申请审核
 */
export interface IPreInvoiceApplicationApprove {
  // 业务id - 实际使用放单个申请的id
  ids: string
  // 流程id - 与流程类型/流程组对应取出来的
  flowId: string
}
