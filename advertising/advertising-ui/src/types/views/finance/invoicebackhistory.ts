/*
 * @Author: suny
 * @Date: 2023-12-27 13:31:12

 * @LastEditTime: 2023-12-27 14:31:25
 * @LastEditors: suny
 * @Description: 分摊退回历史相关实体
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

export type TInvoiceBackHistoryVO = IBaseQueryInfo & IPageRequest

/**
 * <p>
 * 发票(收款)回退历史表 DTO 数据传输对象
 * </p>
 *
 * InvoiceBackHistoryDTO
 */
export interface IInvoiceBackHistoryDTO {
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
   * 分摊日期
   */
  dapportiondate?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
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
   * 媒体名称（关联orderitem表）
   */
  medianame?: string
  /**
   * 原分摊金额(退回金额)
   */
  namountapportion?: number
  /**
   * 分摊明细表id
   */
  orderapportiondetailid?: string
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
   * 预见报开始日期(刊期开始日期，关联orderitem表)
   */
  prestartdate?: string
  /**
   * 广告标题（关联orderitem表）
   */
  sadtitle?: string
  /**
   * 分摊种类(0-预收款 1-直接收款 2-银行流水)
   */
  sapportiontype?: number
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 说明
   */
  sdescription?: string
}
