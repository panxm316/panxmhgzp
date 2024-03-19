/*
 * @Author: suny
 * @Date: 2023-12-19 13:41:31
 * @LastEditTime: 2024-03-08 15:59:10
 * @LastEditors: yanz
 * @Description: 发票相关实体对象
 */

import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { TworderforPreinvoapplyVO } from '@/types/views/ad/adorder'
/** 订单查询对象 */
export type TInvoiceQuery = IInvoiceVO & IBaseQueryInfo & IPageRequest

/** 订单查询 */
export interface IInvoiceVO {
  /**
   * 是否欠费(查询是否仅欠款的数据，0:否 1：是)
   */
  barrearsflag?: string
  /**
   * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
   */
  bcurflag?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: number
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票编码
   */
  invoicecode?: string
  /**
   * 1-有效、0-无效，2-冲红退回
   */
  istatus?: number
  /**
   * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
   */
  itype?: number
}
/**
 * 打印发票对象
 * InvoiceDTO
 */
export interface IInvoiceDTO {
  /**
   * 经营主体ID
   */
  businessentityid?: string
  /**
   * 经营主体
   */
  businessentityname?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 创建时间
   */
  dcreatetime?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 0-直接客户、1-代理公司、2-内容生产方
   */
  icusttype?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 发票编码
   */
  invoicecode?: string
  /**
   * 1-有效、0-无效，2-冲红退回
   */
  istatus?: number
  /**
   * 发票样式  2-普通发票 0-专用发票 51-电子发票 81-数电专票 82-数电普票
   */
  istype?: number
  /**
   * 0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
   */
  itype?: number
  /**
   * 最后操作员
   */
  lastoperator?: string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string
  /**
   * 发票金额
   */
  namount?: number
  /**
   * 创建者
   */
  operator?: string
  /**
   * 创建者id
   */
  operatorid?: string
  /**
   * 付款方式id
   */
  paymethodid?: string
  /**
   * 付款方式
   */
  paymethodname?: string
  /**
   * 开票项目id
   */
  printitemid?: string
  /**
   * 开票项目
   */
  printitemname?: string
  /**
   * 关联发票(红冲发票)
   */
  relateinvoiceid?: string
  /**
   * 收款人
   */
  scashier?: string
  /**
   * 复核人
   */
  schecker?: string
  /**
   * 电子发票sha1
   */
  sinvoicefilesha1?: string
  /**
   * 电子发票原文件名
   */
  soriginalfile?: string
  /**
   * 付款方地址电话
   */
  spayeraddr?: string
  /**
   * 付款方银行账户
   */
  spayerbank?: string
  /**
   * 付款方识别号
   */
  spayercreditcode?: string
  /**
   * 客户发票打印名称
   */
  sprintname?: string
  sremark?: string
  /**
   * 税收编码（取开票项目中对应的值）
   */
  staxcode?: string
  /**
   * 并发标志
   */
  version?: number
  /**
   * 收款方名称
   */
  recipient?: string
  /**
   * 税目
   */
  taxitems?: string
  /**
   * 税率
   */
  taxrate?: string
  /**
   * 大写金额
   */
  amountinwords?: string
  fileList?: IInvoiceFilesDTO[]
  /**
   * 欠款金额（预开发票：发票金额减去twcustomercharge中所有入账金额，【如果找不到该发票数据则入账金额为0】）
   */
  amountarrearage?: number
  /**
   * 已用金额（关联twcustomercharge，查询所有该发票号数据计算）
   */
  namounspent?: number
  /**
   * 剩余金额（预收款：关联twcustomercharge，查询该发票剩余金额）
   */
  namountbalance?: number
  /**
   * 入账金额（关联twcustomercharge，查询所有入账金额）
   */
  namountreceived?: number
  /**
   * 合同号
   */
  contractVos?: TworderforPreinvoapplyVO[]
}

/**
 * <p>
 * 发票文件表
 * </p>
 *
 * IInvoiceFilesDTO
 */
export interface IInvoiceFilesDTO {
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 创建人id
   */
  createempid?: string
  /**
   * 创建人名称
   */
  createempname?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 文件下载url  视频转码播放地址
   */
  durl?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 发票表id
   */
  invoiceid?: string
  /**
   * 发票编码
   */
  invoicecode?: string
  /**
   * 发票号
   */
  invoice?: string
  /**
   * 文件格式
   */
  sfileformat?: string
  /**
   * 统一文件ID
   */
  sfileid?: string
  /**
   * 文件大小
   */
  sfilesize?: string
  /**
   * 文件格式类型(Photo、Video、Audio、Office、Application)
   */
  sfiletype?: string
  /**
   * 源文件名
   */
  soriginalfile?: string
  /**
   * 文件统一文件地址
   */
  url?: string
}
