/*
 * @Author: wanghl
 * @Date: 2023-12-01 12:44:03
 * @LastEditTime: 2024-02-22 09:25:41
 * @LastEditors: songly
 * @Description: 广告预定
 */

/**
 * OrderDTO
 */
export interface Tworder {
  /**
   * 行业名称
   */
  adindustryname?: string
  /**
   * 行业id
   */
  adindustyid?: string
  /**
   * 广告项目id
   */
  adprojectid?: string
  /**
   * 广告项目名称
   */
  adprojectname?: string
  /**
   * 资源Id
   */
  adresourceapplicationid?: string[]
  // adresourceapplicationid?: string
  /**
   * 广告类型id
   */
  adtypeid?: string
  /**
   * 广告类型名称
   */
  adtypename?: string
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string
  /**
   * 代理公司业务员名称
   */
  agencyemployname?: string
  /**
   * 代理公司名称
   */
  agencyname?: string
  /**
   * 代理公司id
   */
  agencytid?: string
  /**
   * 内容生产方业务员id
   */
  agentemployid?: string
  /**
   * 内空生产方业务员名称
   */
  agentemployname?: string
  /**
   * 内容生产方id
   */
  agentid?: string
  /**
   * 内容生产方
   */
  agentname?: string
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 经营主体id
   */
  businessentityid?: string
  /**
   * 经营主体名称
   */
  businessentityname?: string
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建日期
   */
  createtime?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 主业务员名称
   */
  employname?: string
  /**
   * 流程Id
   */
  flowId?: string
  /**
   * 加刊审批状态
   */
  iaddapprovestatus?: number
  /**
   * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  iapprovestatus?: number
  /**
   * 状态标志 预约审批状态:preapproveStatus,
   * 加刊审批状态:addapproveStatus,
   * 改刊审批状态:changeapproveStatus,
   * 停刊审批状态:stopapproveStatus,
   * 折扣审批状态:discountapproveStatus
   */
  iapproveType?: number
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype?: number
  /**
   * 改刊审批状态
   */
  ichangeapprovestatus?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 折扣审批状态
   */
  idiscountapprovestatus?: number
  /**
   * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
   */
  iorderkind?: number
  /**
   * 预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  ipreapprovestatus?: number
  /**
   * 停刊审批状态
   */
  istopapprovestatus?: number
  /**
   * 订单明细
   */
  orderitem?: Tworderitem[]
  /**
   * 订单明细归属
   */
  orderitembelong?: Tworderitembelong[]
  /**
   * 广告内容
   */
  sadcontent?: string
  /**
   * 联系人地址
   */
  saddress?: string
  /**
   * 广告标题
   */
  sadtitle?: string
  /**
   * 联系人
   */
  scontacts?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 联系人电话
   */
  smobilephone?: string
  /**
   * 负责人意见
   */
  sopinion?: string
  /**
   * 订单编号
   */
  sordernum?: string
  /**
   * 邮编
   */
  spostcode?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 并发标志
   */
  version?: number
  /**
   * 是否特殊订单是否特殊订单(0 否 1-是)
   */
  bspecial: number
  /**
   * 特殊原因
   */
  sspecialreason?: string
  /**
   * 客户Vip
   */
  bcustomeVip?: boolean
  /**
   * 代理公司Vip
   */
  bagencyVip?: boolean
  /**
   * 内容生产方Vip
   */
  bagentVip?: boolean
  /**
   * 用于快速预约补刊功能
   */
  relateorderid?: string
}

/**
 * <p>
 * 订单刊期表
 * </p>
 *
 * Tworderitem
 */
export interface Tworderitem {
  /**
   * 订单明细归属
   */
  orderitembelong?: Tworderitembelong[]
  /**
   * 色彩id
   */
  adcolorid?: string
  /**
   * 色彩名称
   */
  adcolorname?: string
  /**
   * 广告形式id
   */
  addisplayformid?: string
  /**
   * 广告形式名称
   */
  addisplayformname?: string
  /**
   * 规格id
   */
  adspecid?: string
  /**
   * 规格名称
   */
  adspecname?: string
  /**
   * 业绩金额
   */
  amountachievement?: number
  /**
   * 欠款金额
   */
  amountarrearage?: number
  /**
   * 签订金额
   */
  amountreceivable?: number
  /**
   * 已收金额
   */
  amountreceived?: number
  /**
   * 刊例价
   */
  baseprice?: number
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 是否填报欠款原因
   */
  breportreason?: boolean
  /**
   * 是否有效
   */
  buse?: boolean
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建日期
   */
  createtime?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次版本名称
   */
  foldareavername?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 叠次名称
   */
  foldname?: string
  /**
   * 版面id
   */
  foldpageplanid?: string
  /**
   * 版面名称
   */
  foldpageplanname?: number | string
  /**
   * 加刊审批状态
   */
  iaddapprovestatus?: number
  /**
   * 订单审批状态
   */
  iapprovestatus?: number
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype?: number
  /**
   * 改刊审批状态
   */
  ichangeapprovestatus?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 折扣审批状态
   */
  idiscountapprovestatus?: number
  /**
   * 发布状态
   */
  ipublishstatus?: number
  /**
   * 停刊审批状态
   */
  istopapprovestatus?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称
   */
  medianame?: string
  /**
   * 媒体类型id
   */
  mediatypekey?: string
  /**
   * 媒体类型名称
   */
  mediatypename?: string
  /**
   * 成本金额
   */
  namountcost?: number
  /**
   * 折扣率
   */
  ndiscountrate?: number
  /**
   * 高
   */
  nheight?: number
  /**
   * 标准价格
   */
  normalprice?: number
  /**
   * 宽
   */
  nwidth?: number
  /**
   * 订单id
   */
  orderid?: string
  /**
   * 版面类别id
   */
  pagesortid?: string
  /**
   * 版面类别名称
   */
  pagesortname?: string
  /**
   * 预见报结束日期
   */
  preenddate?: string
  /**
   * 预见报开始日期
   */
  prestartdate?: string
  /**
   * 价格明细表id
   */
  priceitemid?: string
  /**
   * 广告内容
   */
  sadcontent?: string
  /**
   * 广告标题
   */
  sadtitle?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 订单号
   */
  sordernum?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 并发标记
   */
  version?: number
  /**
   * 星期id
   */
  weeksettingid?: string
  /**
   * 星期名称
   */
  weeksettingname?: string
  /**
   * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
   */
  ipublishcheckstatus?: number
  /**
   * 刊发检测报告内容
   */
  spublishcheckcontent?: string
  /**
   *业绩时间
   */
  dachievementdate?: string

  /**
   * 刊期编号(自增列)
   */
  iitemcode?: number
}

/**
 * <p>
 * 订单刊期归属表
 * </p>
 *
 * Tworderitembelong
 */
export interface Tworderitembelong {
  /**
   * 业绩比例
   */
  archievementrate?: number
  /**
   * 是否主业务员
   */
  bprimary?: boolean
  /**
   * 佣金比例
   */
  commissionrate?: number
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建日期
   */
  createtime?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 业务id
   */
  employid?: string
  /**
   * 业务员名称
   */
  employname?: string
  /**
   * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
   */
  employtype?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 订单id
   */
  orderitemid?: string
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 订单号
   */
  sordernum?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 任务比例
   */
  taskrate?: number
}
export interface TworderCustomer {
  customerId?: string
  employId?: string
}
/**
 * 查询订单列表
 */
export interface QTworder {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  loginUserId?: string
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页
   */
  page?: number
  /**
   * 每页显示记录数
   */
  pageSize?: number
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 排序字段
   */
  sort?: string
  startTime?: string
  adresourceapplicationid?: string
  /**
   * 订单状态
   */
  iapprovestatus?: number | string
  /**
   * 是否包含待审订单
   */
  bshowedit?: boolean
}
