/*
 * @Author: suny
 * @Date: 2023-12-25 16:58:27
 * @LastEditTime: 2024-01-08 14:09:28
 * @LastEditors: suny
 * @Description: 广告分摊明细表对象实体
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 广告分摊明细查询对象 */
export type TOrderApportiondetailVO = IOrderApportiondetailVO & IBaseQueryInfo & IPageRequest
export interface IOrderApportiondetailVO {
  /**
   * 收费表id
   */
  customerchargeid?: string
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 流水表id
   */
  bankaccountid?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票表id
   */
  invoiceid?: string
  /**
   * 指定订单id
   */
  orderid?: string
  /**
   * 合同号
   */
  scontractnum?: string
}

/**
 * <p>
 * 广告分摊明细表 DTO 数据传输对象
 * </p>
 *
 * OrderApportiondetailDTO
 */
export interface IOrderApportiondetailDTO {
  /**
   * 订单号
   */
  sordernum?: string
  /**
   * 代理公司名称(关联订单表)
   */
  agencyname?: string
  /**
   * 内容生产方(关联订单表)
   */
  agentname?: string
  /**
   * 欠款金额
   */
  amountarrearage?: number
  /**
   * 应收金额
   */
  amountreceivable?: number
  /**
   * 已收金额
   */
  amountreceived?: number
  /**
   * 经营主体名称（关联发票表找到）
   */
  businessentityname?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 收费表id
   */
  customerchargeid?: string
  /**
   * 客户名称(关联订单表)
   */
  customername?: string
  /**
   * 分摊日期
   */
  dapportiondate?: string
  /**
   * 收费日期
   */
  dpaydate?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 业务员id(关联订单表)
   */
  employid?: string
  /**
   * 业务员名称(关联订单表)
   */
  employname?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票表id
   */
  invoiceid?: string
  /**
   * 媒体名称(关联订单明细表)
   */
  medianame?: string
  /**
   * 分摊金额
   */
  namountapportion?: number
  /**
   * 订单id
   */
  orderid?: string
  /**
   * 刊期编码
   */
  orderitemcode?: number
  /**
   * 刊期id
   */
  orderitemid?: string
  /**
   * 预见报开始日期(关联订单明细表)--刊发日期
   */
  prestartdate?: string
  /**
   * 广告标题(关联订单明细表)
   */
  sadtitle?: string
  /**
   * 分摊各类(0-预收款 1-直接收款 2-银行流水)
   */
  sapportiontype?: number
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 并发标记
   */
  version?: number
}
