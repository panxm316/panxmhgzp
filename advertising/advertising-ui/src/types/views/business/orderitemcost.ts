/*
 * @Author: suny
 * @Date: 2023-12-14 10:07:36
 * @LastEditTime: 2024-03-18 18:44:38
 * @LastEditors: suny
 * @Description: 广告成本相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
import { Tworderitembelong } from '../ad/adtworder'

/** 广告成本订单信息查询对象 */
export type TOrderAndItemVO = IOrderAndItemVO & IPageRequest & IBaseQueryInfo

/**
 * 订单详细信息查询对象
 */
export interface IOrderAndItemVO {
  /**
   * 订单明细id
   */
  orderitemid?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 发布状态
   */
  ipublishstatus?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 是否欠费(查询是否仅欠款的数据，0:否 1：是)
   */
  barrearsflag?: string
  /**
   * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
   */
  bcurflag?: boolean
}
/**
 * orderAndItemDTO
 * 创建人：suny
 * 类描述：TODO 成本核对相关订单明细DTO
 * 创建日期：2023/12/13 14:52
 *
 * OrderAndItemDTO
 */
export interface IOrderAndItemDTO {
  /**
   * 订单id
   */
  orderid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /* 广告项目id
   */
  adprojectid?: string
  /**
   * 广告项目编码
   */
  projectcode?: string
  /**
   * 广告形式名称
   */
  addisplayformname?: string
  /**
   * 行业名称
   */
  adindustryname?: string
  /**
   * 广告项目名称
   */
  adprojectname?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 代理公司名称
   */
  agencyname?: string
  /**
   * 内容生产方
   */
  agentname?: string
  /**
   * 业绩金额
   */
  amountachievement?: number
  /**
   * 业绩时间(用于统计)
   */
  dachievementdate?: string
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
   * 分摊金额
   */
  namountapportion?: number
  /**
   * 经营主体名称（关联发票表找到）
   */
  businessentityname?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 业务员id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 业务员名称
   */
  employtype?: number
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 版面名称
   */
  foldpageplanname?: string
  /**
   * 关联发票号（英文逗号隔开）
   */
  invoices?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体名称
   */
  mediaid?: string
  /**
   * 媒体类型名称
   */
  mediatypename?: string
  /**
   * 成本金额
   */
  namountcost?: number
  /**
   * 高
   */
  nheight?: number
  /**
   * 宽
   */
  nwidth?: number
  /**
   * 成本核对明细
   */
  orderItemCostDTOList?: IOrderItemCostDTO[]
  /**
   * 订单明细id
   */
  orderitemid?: string
  /**
   * 版面类别名称
   */
  pagesortname?: string
  /**
   * 预见报开始日期
   */
  prestartdate?: string
  /**
   * 广告标题
   */
  sadtitle?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 星期名称
   */
  weeksettingname?: string
  /**
   * 刊期编号(自增列,广告号)
   */
  iitemcode?: number
  /**
   * 发布状态
   */
  ipublishstatus?: number
  /**
   * 是否可编辑
   */
  bEditFlag?: boolean
  /**
   * 刊期归属数据
   */
  orderitembelongList?: Tworderitembelong[]
  /**
   * 付款日期
   */
  dpaydate?: string
  /**
   * 分摊日期
   */
  dapportiondate?: string
}

/**
 * OrderItemCostDTO
 * 创建人：suny
 * 类描述：TODO 广告成本DTO
 * 创建日期：2023/12/13 15:07
 *
 * OrderItemCostDTO
 */
export interface IOrderItemCostDTO {
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
  dcreatedate?: string
  /**
   * 证明文件列表
   */
  fileList?: IOrderItemCostFilesDTO[]
  /**
   * 主键
   */
  id?: string
  /**
   * 状态 0-待提交 1-已提交 2-已确认 3-已退回
   */
  istatus?: number
  /**
   * 成本金额
   */
  namountcost?: number
  /**
   * 订单明细表
   */
  orderitemid?: string
  /**
   * 说明
   */
  sdescription?: string
}

/**
 * <p>
 * 广告成本证明文件表
 * </p>
 *
 * OrderItemCostFilesDTO
 */
export interface IOrderItemCostFilesDTO {
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
   * 9-成本证明
   */
  ifilecategory?: number
  /**
   * 资源申请id
   */
  orderitemcostid?: string
  /**
   * 文件描述
   */
  sdescription?: string
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
