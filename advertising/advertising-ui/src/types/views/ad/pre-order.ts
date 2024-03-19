import dayjs from 'dayjs'
import useUserStore from '@/store/modules/user'
import { Tworder, Tworderitem } from '@/types/views/ad/adtworder'

export interface PreOrder {
  id?: string
  /**
   * 预定编号
   */
  sordernum: string
  /**
   * 申请日期
   */
  createtime: string
  /**
   * 订单类别
   */
  iorderkind: number
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype: number
  /**
   * 创建者id
   */
  createempid: string
  /**
   * 创建者名称
   */
  createempname: string
  /**
   * 部门id
   */
  deptid: string
  /**
   * 部门名称
   */
  deptname: string
  /**
   * 主业务员id || 代理公司业务员id || 内容生产方业务员id
   */
  employid: string
  /**
   * 主业务员名称 || 代理公司业务员名称  || 内容生产方业务员名称
   */
  employname: string
  /**
   * 业务员类型 默认直接客户
   */
  employtype: number
  /**
   * 经营主体id
   */
  businessentityid: string
  /**
   * 经营主体名称
   */
  businessentityname: string
  /**
   * 直接客户id
   */
  customerid: string
  /**
   * 直接客户名称
   */
  customername: string
  /**
   * 代理公司id
   */
  agencytid: string
  /**
   * 代理公司名称
   */
  agencyname: string
  /**
   * 代理公司业务员id
   */
  agencyemployid: string
  /**
   * 代理公司业务员名称
   */
  agencyemployname: string
  /**
   * 内容生产方id
   */
  agentid: string
  /**
   * 内容生产方
   */
  agentname: string
  /**
   * 内容生产方业务员id
   */
  agentemployid: string
  /**
   * 内空生产方业务员名称
   */
  agentemployname: string
  /**
   * 广告标题
   */
  sadtitle: string
  // 下面对应订单明细表
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 媒体类型名称
   */
  mediatypename?: string
  /**
   * 媒体id
   */
  mediaid: string
  /**
   * 媒体名称
   */
  medianame: string
  /**
   * 只用来作为媒体类型key||媒体id||叠次id的v-model
   */
  extId: string
  /**
   * 叠次id
   */
  foldid: string
  /**
   * 叠次名称
   */
  foldname: string
  /**
   * 叠次版本id
   */
  foldareaverid: string
  /**
   * 叠次版本名称
   */
  foldareavername: string
  /**
   * 计划id
   */
  foldpageplanid: string
  /**
   * 版面 foldpageplan.pagenum todo 如果是不指定版面的话，就是存放 非指定版 这四个字。
   */
  foldpageplanname?: number | string
  /**
   * 广告明细标题
   */
  saditemtitle: string
  /**
   * 见报日期
   */
  prestartdate: string
  /**
   * 版面类别id
   */
  pagesortid: string
  /**
   * 版面类别名称
   */
  pagesortname: string
  /**
   * 广告颜色id
   */
  adcolorid: string
  /**
   * 广告颜色名称
   */
  adcolorname: string
  /**
   * 广告形式id
   */
  addisplayformid: string
  /**
   * 广告形式id
   */
  addisplayformname: string
  /**
   * 广告规格id
   */
  adspecid: string
  /**
   * 广告规格名称
   */
  adspecname: string
  /**
   * 负责人意见
   */
  sopinion: string
  /**
   * 行业id
   */
  adindustyid: string
  /**
   * 行业名称
   */
  adindustryname: string
  /**
   * 广告类型id
   */
  adtypeid: string
  /**
   * 广告类型名称
   */
  adtypename: string
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 是否删除
   */
  bdelete: boolean
  /**
   * 预约审批状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  ipreapprovestatus: number
  /**
   * 审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
   */
  iapprovestatus: number
  /**
   * 是否特殊订单(0 否 1-是)
   */
  bspecial: number
  version: number
}

export const initPreOrder = (bookType: number): PreOrder => {
  return {
    id: '',
    sordernum: '',
    createtime: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss'),
    iorderkind: 0,
    ibooktype: bookType,
    createempid: useUserStore().user?.userid?.toString(),
    createempname: useUserStore().user?.username?.toString(),
    deptid: useUserStore().user?.deptid?.toString() || '',
    deptname: useUserStore().user?.deptname?.toString() || '',
    employid: useUserStore().user?.userid?.toString() || '',
    employname: useUserStore().user?.username?.toString() || '',
    employtype: 0,
    businessentityid: '',
    businessentityname: '',
    customerid: '',
    customername: '',
    agencytid: '',
    agencyname: '',
    agencyemployid: '',
    agencyemployname: '',
    agentid: '',
    agentname: '',
    agentemployid: '',
    agentemployname: '',
    sadtitle: '',
    // 下面对应订单明细表
    mediatypekey: '',
    mediatypename: '',
    mediaid: '',
    medianame: '',
    extId: '',
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    foldpageplanid: '',
    foldpageplanname: '',
    saditemtitle: '',
    prestartdate: dayjs(new Date()).add(1, 'day').format('YYYY-MM-DD'),
    pagesortid: '',
    pagesortname: '',
    adcolorid: '',
    adcolorname: '',
    addisplayformid: '',
    addisplayformname: '',
    adspecid: '',
    adspecname: '',
    // 以上为对应订单明细表
    sopinion: '',
    adindustyid: '',
    adindustryname: '',
    adtypeid: '',
    adtypename: '',
    buse: true,
    bdelete: false,
    ipreapprovestatus: 0,
    iapprovestatus: 0,
    bspecial: 0,
    version: 0
  }
}

/**
 * 点击修改广告预约订单时,把Tworder转为PreOrder
 * @param order 数据库中的Tworder
 * @param employtype 业务员归属类型
 * @returns PreOrder
 */
export const parseTworderToPreOrder = (
  order: Tworder,
  employtype: number,
  prestartdate: string,
  ibooktype: number
): PreOrder => {
  return {
    id: order.id,
    sordernum: order.sordernum || '',
    createtime: order.createtime || '',
    iorderkind: order.iorderkind || 0,
    ibooktype: order.ibooktype || ibooktype,
    createempid: order.createempid || '',
    createempname: order.createempname || '',
    deptid: order.deptid || '',
    deptname: order.deptname || '',
    employid: order.employid || '',
    employname: order.employname || '',
    employtype: employtype,
    businessentityid: order.businessentityid || '',
    businessentityname: order.businessentityname || '',
    customerid: order.customerid || '',
    customername: order.customername || '',
    agencytid: order.agencytid || '',
    agencyname: order.agencyname || '',
    agencyemployid: order.agencyemployid || '',
    agencyemployname: order.agencyemployname || '',
    agentid: order.agentid || '',
    agentname: order.agentname || '',
    agentemployid: order.agentemployid || '',
    agentemployname: order.agentemployname || '',
    sadtitle: order.sadtitle || '',
    // 下面对应订单明细表
    mediatypekey: '',
    mediatypename: '',
    mediaid: '',
    medianame: '',
    extId: '',
    foldid: '',
    foldname: '',
    foldareaverid: '',
    foldareavername: '',
    foldpageplanid: '',
    foldpageplanname: '',
    saditemtitle: '',
    prestartdate: prestartdate,
    pagesortid: '',
    pagesortname: '',
    adcolorid: '',
    adcolorname: '',
    addisplayformid: '',
    addisplayformname: '',
    adspecid: '',
    adspecname: '',
    // 以上为对应订单明细表
    sopinion: order.sopinion || '',
    adindustyid: order.adindustyid || '',
    adindustryname: order.adindustryname || '',
    adtypeid: order.adtypeid || '',
    adtypename: order.adtypename || '',
    buse: true,
    bdelete: false,
    ipreapprovestatus: order.ipreapprovestatus || 0,
    iapprovestatus: order.iapprovestatus || 0,
    bspecial: order.bspecial || 0,
    version: order.version || 0
  }
}

/**
 * 点击修改广告预约订单明细时,把Tworderitem转为PreOrder
 * @param preOrder 当前的PreOrder
 * @param orderItem 数据库中的Tworderitem
 */
export const parseTworderitemToPreOrder = (preOrder: PreOrder, orderItem: Tworderitem) => {
  preOrder.mediatypekey = orderItem.mediatypekey || ''
  preOrder.mediatypename = orderItem.mediatypename || ''
  preOrder.mediaid = orderItem.mediaid || ''
  preOrder.medianame = orderItem.medianame || ''
  preOrder.extId = getExtId(orderItem)
  preOrder.foldid = orderItem.foldid || ''
  preOrder.foldname = orderItem.foldname || ''
  preOrder.foldareaverid = orderItem.foldareaverid || ''
  preOrder.foldareavername = orderItem.foldareavername || ''
  preOrder.foldpageplanid = orderItem.foldpageplanid || ''
  preOrder.foldpageplanname = orderItem.foldpageplanname || ''
  preOrder.saditemtitle = orderItem.sadtitle || ''
  preOrder.prestartdate = orderItem.prestartdate || ''
  preOrder.pagesortid = orderItem.pagesortid || ''
  preOrder.pagesortname = orderItem.pagesortname || ''
  preOrder.adcolorid = orderItem.adcolorid || ''
  preOrder.adcolorname = orderItem.adcolorname || ''
  preOrder.addisplayformid = orderItem.addisplayformid || ''
  preOrder.addisplayformname = orderItem.addisplayformname || ''
  preOrder.adspecid = orderItem.adspecid || ''
  preOrder.adspecname = orderItem.adspecname || ''
}

export const getExtId = (orderItem: Tworderitem): string => {
  return orderItem.foldid || orderItem.mediaid || orderItem.mediatypekey || ''
}
