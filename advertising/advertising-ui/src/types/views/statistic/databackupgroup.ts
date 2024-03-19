/*
 * @Author: yanz
 * @Date: 2024-01-19 17:21:30
 * @LastEditors: yanz
 * @LastEditTime: 2024-01-20 10:14:14
 * @Description:数据轧账明细相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'
/** 轧账总表查询条件对象 */
export type TDatabackupGroupVO = IDatabackupGroupVO & IPageRequest & IBaseQueryInfo
/** 轧账明细查询条件对象 */
export type TDataBackupDetailVO = IDataBackupDetailVO & IPageRequest & IBaseQueryInfo

export interface IDatabackupGroupVO {
  /**
   * 创建者id
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 名称
   */
  databackupname?: string
  /**
   * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
   */
  datatype?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 记录数
   */
  nrecordsize?: number
  /**
   * 说明
   */
  sdescription?: string
  /**
   * 明细表名
   */
  sdetailtablename?: string
}
export interface IDataBackupDetailVO {
  /**
   * 广告形式id
   */
  addisplayformid?: string
  /**
   * 行业id
   */
  adindustyid?: string
  /**
   * 广告类型id
   */
  adtypeid?: string
  /**
   * 是否特殊订单(0 否 1-是)
   */
  bspecial?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 客户名称
   */
  customername?: string
  /**
   * 数据备份组表id
   */
  databackupgroupid?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  iorderapprovestatus?: number
  /**
   * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
   */
  iorderkind?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  orderibooktype?: number
  /**
   * 合同号
   */
  scontractnum?: string
  /**
   * 订单号
   */
  sordernum?: string
}
export interface Twdatabackupgroup {
  /**
   * 创建者id
   */
  createempid?: string | null
  /**
   * 创建者名称
   */
  createempname?: null | string
  /**
   * 名称
   */
  databackupname?: null | string
  /**
   * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
   */
  datatype?: null | string
  /**
   * 创建日期
   */
  dcreatetime?: null | string
  /**
   * 结束日期
   */
  denddate?: null | string
  /**
   * 开始日期
   */
  dstartdate?: null | string
  /**
   * 主键
   */
  id?: string | null
  /**
   * 记录数
   */
  nrecordsize?: number | null
  /**
   * 说明
   */
  sdescription?: null | string
  /**
   * 明细表名
   */
  sdetailtablename?: null | string
}
export interface Twdatabackupdetail1 {
  /**
   * 色彩id
   */
  adcolorid?: string | null
  /**
   * 色彩名称
   */
  adcolorname?: null | string
  /**
   * 广告形式id
   */
  addisplayformid?: string | null
  /**
   * 广告形式名称
   */
  addisplayformname?: null | string
  /**
   * 行业名称
   */
  adindustryname?: null | string
  /**
   * 行业id
   */
  adindustyid?: string | null
  /**
   * 广告项目id
   */
  adprojectid?: string | null
  /**
   * 广告项目名称
   */
  adprojectname?: null | string
  /**
   * 规格id
   */
  adspecid?: string | null
  /**
   * 规格名称
   */
  adspecname?: null | string
  /**
   * 广告类型id
   */
  adtypeid?: string | null
  /**
   * 广告类型名称
   */
  adtypename?: null | string
  /**
   * 代理公司业务员id
   */
  agencyemployid?: string | null
  /**
   * 代理公司业务员名称
   */
  agencyemployname?: null | string
  /**
   * 代理公司名称
   */
  agencyname?: null | string
  /**
   * 代理公司id
   */
  agencytid?: string | null
  /**
   * 内容生产方业务员id
   */
  agentemployid?: string | null
  /**
   * 内空生产方业务员名称
   */
  agentemployname?: null | string
  /**
   * 内容生产方id
   */
  agentid?: string | null
  /**
   * 内容生产方
   */
  agentname?: null | string
  /**
   * 业绩金额
   */
  amountachievement?: number | null
  /**
   * 欠款金额
   */
  amountarrearage?: number | null
  /**
   * 应收金额
   */
  amountreceivable?: number | null
  /**
   * 已收金额
   */
  amountreceived?: number | null
  /**
   * 刊例价
   */
  baseprice?: number | null
  /**
   * 是否删除
   */
  bdelete?: boolean | null
  /**
   * 是否删除
   */
  borderdelete?: boolean | null
  /**
   * 是否有效
   */
  borderuse?: boolean | null
  /**
   * 是否推送填报欠款原因
   */
  breportreason?: boolean | null
  /**
   * 是否特殊订单(0 否 1-是)
   */
  bspecial?: boolean | null
  /**
   * 是否有效
   */
  buse?: boolean | null
  /**
   * 经营主体id
   */
  businessentityid?: string | null
  /**
   * 经营主体名称
   */
  businessentityname?: null | string
  /**
   * 创建者id
   */
  createempid?: string | null
  /**
   * 创建者名称
   */
  createempname?: null | string
  /**
   * 创建日期
   */
  createtime?: null | string
  /**
   * 客户id
   */
  customerid?: string | null
  /**
   * 客户名称
   */
  customername?: null | string
  /**
   * 业绩时间
   */
  dachievementdate?: null | string
  /**
   * 数据备份组表id
   */
  databackupgroupid?: string | null
  /**
   * 部门id
   */
  deptid?: string | null
  /**
   * 部门名称
   */
  deptname?: null | string
  /**
   * 主业务员id
   */
  employid?: string | null
  /**
   * 主业务员名称
   */
  employname?: null | string
  /**
   * 叠次版本id
   */
  foldareaverid?: string | null
  /**
   * 叠次版本名称
   */
  foldareavername?: null | string
  /**
   * 叠次id
   */
  foldid?: string | null
  /**
   * 叠次名称
   */
  foldname?: null | string
  /**
   * 版面id
   */
  foldpageplanid?: string | null
  /**
   * 版面名称
   */
  foldpageplanname?: null | string
  /**
   * 加刊审批状态
   */
  iaddapprovestatus?: number | null
  /**
   * 订单审批状态
   */
  iapprovestatus?: number | null
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype?: number | null
  /**
   * 改刊审批状态
   */
  ichangeapprovestatus?: number | null
  /**
   * 自增列
   */
  id?: string | null
  /**
   * 折扣审批状态
   */
  idiscountapprovestatus?: number | null
  /**
   * 刊期编码(自增列)
   */
  iitemcode?: number | null
  /**
   * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  iorderapprovestatus?: number | null
  /**
   * 订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告
   */
  iorderkind?: number | null
  /**
   * 刊发检测状态(0-正常 1-未刊发 2-刊发错误
   */
  ipublishcheckstatus?: number | null
  /**
   * 发布状态0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架
   */
  ipublishstatus?: number | null
  /**
   * 停刊审批状态
   */
  istopapprovestatus?: number | null
  /**
   * 媒体id
   */
  mediaid?: string | null
  /**
   * 媒体名称
   */
  medianame?: null | string
  /**
   * 媒体类型key
   */
  mediatypekey?: null | string
  /**
   * 媒体类型名称
   */
  mediatypename?: null | string
  /**
   * 成本金额
   */
  namountcost?: number | null
  /**
   * 折扣率
   */
  ndiscountrate?: number | null
  /**
   * 高
   */
  nheight?: number | null
  /**
   * 标准价格
   */
  normalprice?: number | null
  /**
   * 宽
   */
  nwidth?: number | null
  /**
   * 订单创建者id
   */
  ordercreateempid?: string | null
  /**
   * 订单创建者名称
   */
  ordercreateempname?: null | string
  /**
   * 订单创建日期
   */
  ordercreatetime?: null | string
  /**
   * 订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  orderibooktype?: number | null
  /**
   * 订单id
   */
  orderid?: string | null
  /**
   * 广告明细主键
   */
  orderitemid?: string | null
  /**
   * 版面类别id
   */
  pagesortid?: string | null
  /**
   * 版面类别名称
   */
  pagesortname?: null | string
  /**
   * 预见报结束日期
   */
  preenddate?: null | string
  /**
   * 预见报开始日期
   */
  prestartdate?: null | string
  /**
   * 价格明细表id
   */
  priceitemid?: string | null
  /**
   * 广告内容
   */
  sadcontent?: null | string
  /**
   * 地址
   */
  saddress?: null | string
  /**
   * 广告标题
   */
  sadtitle?: null | string
  /**
   * 联系人
   */
  scontacts?: null | string
  /**
   * 合同号
   */
  scontractnum?: null | string
  /**
   * 联系人电话
   */
  smobilephone?: null | string
  /**
   * 负责人意见
   */
  sopinion?: null | string
  /**
   * 广告内容
   */
  sorderadcontent?: null | string
  /**
   * 广告标题
   */
  sorderadtitle?: null | string
  /**
   * 订单号
   */
  sordernum?: null | string
  /**
   * 邮编
   */
  spostcode?: null | string
  /**
   * 刊发检测报告
   */
  spublishcheckcontent?: null | string
  /**
   * 备注
   */
  sremark?: null | string
  /**
   * 特殊原因
   */
  sspecialreason?: null | string
  /**
   * 星期id
   */
  weeksettingid?: string | null
  /**
   * 星期名称
   */
  weeksettingname?: null | string
}
export interface IDatabackupGroupDTO {
  /**
   * 创建者id
   */
  createempid?: number
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 名称
   */
  databackupname?: string
  /**
   * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
   */
  datatype?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 主键
   */
  id?: number
  /**
   * 记录数
   */
  nrecordsize?: number
  /**
   * 说明
   */
  sdescription?: string
  /**
   * 明细表名
   */
  sdetailtablename?: string
}
