/*
 * @Author: yanz
 * @Date: 2023-11-30 10:45:18
 * @LastEditors: yanz
 * @LastEditTime: 2023-12-12 10:51:52
 * @Description:订单表相关类型
 *
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/**
 * 订单表（预开发票申请用）查询、展示对象
 */
export type TworderforPreinvoapplyVO = {
  /** 订单id */
  orderid: number
  /** 合同编号 */
  scontractnum: string
  /** 广告标题 */
  sadtitle: string
  /** 广告分类 */
  adtypeid: number
  adtypename: string
  /** 欠款金额 */
  amountarrearage: number
  /**
   * 广告行业
   */
  adindustryid: string | number
  adindustryname: string
  /** 经营主体 */
  businessentityid: string | number
  businessentityname: string
  /** 客户id */
  customerid: string | number
  /** 客户名称 */
  customername: string
  /** 代理公司id */
  agencytid: string | number
  /** 代理公司名称 */
  agencyname: string
  /** 内容生产方id */
  agentid: string | number
  /** 内容生产方名称 */
  agentname: string
}

/**
 * 申请审核
 */
export type TContractforEmpCustomerQuery = {
  // 业务员id
  employeeid: string
  // 客户id - 将用于匹配3种类型的客户
  customerid: string
} & IPageRequest &
  IBaseQueryInfo
