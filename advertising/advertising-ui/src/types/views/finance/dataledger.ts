/*
 * @Author: suny
 * @Date: 2024-01-05 14:40:39
 * @LastEditTime: 2024-01-08 08:45:30
 * @LastEditors: suny
 * @Description: 数据台账相关类型定义
 */

import { IAdResourceApplicationDTO } from '../ad/adresourceapplication'
import { Tworder, Tworderitem } from '../ad/adtworder'
import { ICustomerChargeBankDTO } from '../business/bankaccountallocate'
import { IOrderItemCostDTO } from '../business/orderitemcost'
import { IPreInvoiceApplicationDTO } from '../business/preinvoiceapplication'
import { IOrderApportiondetailDTO } from './orderapportiondetail'

/**
 * 订单台账详情对象
 * OrderContractDetailDTO
 */
export interface IOrderContractDetailDTO {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 与此订单关联的客户的预收款信息
   */
  customerChargeBankDTOList?: ICustomerChargeBankDTO[]
  /**
   * 订单基本信息
   */
  order?: Tworder
  /**
   * 已分摊明细
   */
  orderApportiondetailDTOList?: IOrderApportiondetailDTO[]
  /**
   * 成本明细
   */
  orderItemCostDTOList?: IOrderItemCostDTO[]
  /**
   * 广告明细
   */
  orderitemList?: Tworderitem[]
  /**
   * 预开发票列表
   */
  preInvoiceApplicationDTOList?: IPreInvoiceApplicationDTO[]
  /**
   * 广告资源 - 要把所有的资源文件带出来，要查看文件详情的(AdResourceApplication 的 adorderid 列)
   */
  resourceApplicationDTOList?: IAdResourceApplicationDTO[]
}
/**
 * 发票台账详情对象
 * InvoiceDetailsDTO
 */
export interface InvoiceDetailsDTO {
  /**
   * 分配明细（已有分配）
   */
  allocationDetails?: ICustomerChargeBankDTO[]
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 预收款信息（预收款类型）
   */
  customerChargeBankDTOList?: ICustomerChargeBankDTO[]
  /**
   * 核销数据（已做核销）
   */
  orderApportiondetailDTOList?: IOrderApportiondetailDTO[]
  /**
   * 预开信息（预开类型）
   */
  preInvoiceApplicationDTOList?: IPreInvoiceApplicationDTO[]
  [property: string]: any
}
