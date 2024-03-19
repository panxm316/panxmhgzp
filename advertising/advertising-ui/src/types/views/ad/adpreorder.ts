import useUserStore from '@/store/modules/user'
import { PreOrder } from '@/types/views/ad/pre-order'
import { Tworderitem } from '@/types/views/ad/adtworder'
import { parseOrderItemToBelong } from '@/types/views/ad/pre-order-item'
import { Mode } from '@/types/views/ad/mode'

export const parsePreOrderToTworderitem = (
  preOrder: PreOrder,
  mode: Mode,
  orderItem?: Tworderitem
): Tworderitem => {
  const userId = useUserStore().user?.userid?.toString()
  const username = useUserStore().user?.username?.toString()
  const belong = orderItem?.orderitembelong ? orderItem.orderitembelong[0] : undefined
  return {
    id: mode === Mode.Edit ? orderItem?.id : '',
    orderid: mode === Mode.Edit ? orderItem?.orderid : '',
    sordernum: mode === Mode.Edit ? orderItem?.sordernum : '',
    scontractnum: mode === Mode.Edit ? orderItem?.scontractnum : '',
    createempid: mode === Mode.Edit ? orderItem?.createempid : userId,
    createempname: mode === Mode.Edit ? orderItem?.createempname : username,
    createtime: mode === Mode.Edit ? orderItem?.createtime : '',
    ibooktype: 1,
    mediatypekey: preOrder.mediatypekey,
    mediatypename: preOrder.mediatypename,
    mediaid: preOrder.mediaid,
    medianame: preOrder.medianame,
    prestartdate: preOrder.prestartdate,
    preenddate: mode === Mode.Edit ? orderItem?.preenddate : '',
    foldid: preOrder.foldid,
    foldname: preOrder.foldname,
    foldareaverid: preOrder.foldareaverid,
    foldareavername: preOrder.foldareavername,
    addisplayformid: preOrder.addisplayformid,
    addisplayformname: preOrder.addisplayformname,
    pagesortid: preOrder.pagesortid,
    pagesortname: preOrder.pagesortname,
    adcolorid: preOrder.adcolorid,
    adcolorname: preOrder.adcolorname,
    adspecid: preOrder.adspecid,
    adspecname: preOrder.adspecname,
    nwidth: mode === Mode.Edit ? orderItem?.nwidth : undefined,
    nheight: mode === Mode.Edit ? orderItem?.nheight : undefined,
    weeksettingid: mode === Mode.Edit ? orderItem?.weeksettingid : '',
    weeksettingname: mode === Mode.Edit ? orderItem?.weeksettingname : '',
    foldpageplanid: preOrder.foldpageplanid,
    sadtitle: preOrder.saditemtitle,
    sadcontent: mode === Mode.Edit ? orderItem?.sadcontent : '',
    foldpageplanname: preOrder.foldpageplanname,
    baseprice: 0,
    priceitemid: '',
    normalprice: 0,
    amountreceivable: 0,
    amountreceived: 0,
    amountarrearage: 0,
    ndiscountrate: 0,
    amountachievement: 0,
    dachievementdate: '',
    namountcost: 0,
    iaddapprovestatus: 0,
    ichangeapprovestatus: 0,
    istopapprovestatus: 0,
    idiscountapprovestatus: 0,
    iapprovestatus: 0,
    ipublishstatus: 0,
    buse: true,
    bdelete: false,
    breportreason: undefined,
    sremark: mode === Mode.Edit ? orderItem?.sremark : '',
    iitemcode: mode === Mode.Edit ? orderItem?.iitemcode : 0,
    ipublishcheckstatus: undefined,
    spublishcheckcontent: mode === Mode.Edit ? orderItem?.spublishcheckcontent : '',
    version: mode === Mode.Edit ? orderItem?.version : 0,
    orderitembelong: [parseOrderItemToBelong(preOrder, mode, belong)]
  }
}

export interface ElTreeNode {
  value: string
  label: string
  children: []
  disabled: boolean
  /**
   * 扩展对象
   */
  extObj?: any
}

export interface ElTreeNodeVO extends ElTreeNode {
  /**
   * 扩展对象
   */
  extObj?: PreOrderExtVO
}

export class PreOrderExtVO {
  mediatypekey?: string
  mediatypename?: string
  mediaid?: string
  medianame?: string
  foldid?: string
  foldname?: string
}

export const mediaTypeFieldLabels = {
  paper: {
    medianame: '媒体',
    foldname: '叠次',
    foldareavername: '叠次版本',
    addisplayformname: '广告形式',
    pagesortname: '版面类别',
    adcolorname: '色彩',
    adspecname: '规格',
    weeksettingname: '星期',
    // pagenum
    foldpageplanname: '版面'
  },
  app: {
    medianame: '媒体',
    foldname: '位置',
    foldareavername: '频道/分类',
    addisplayformname: '广告形式',
    pagesortname: '内容/曝光率',
    adcolorname: '广告样式',
    adspecname: '尺寸',
    weeksettingname: '',
    foldpageplanname: ''
  },
  website: {
    medianame: '媒体',
    foldname: '项目分类',
    foldareavername: '频道/分类',
    addisplayformname: '广告形式',
    pagesortname: '内容/曝光率',
    adcolorname: '广告样式',
    adspecname: '尺寸',
    weeksettingname: '',
    pagenum: ''
  },
  wei: {
    medianame: '',
    foldname: '',
    foldareavername: '',
    addisplayformname: '广告形式',
    pagesortname: '位置',
    adcolorname: '广告样式',
    adspecname: '尺寸',
    weeksettingname: '',
    foldpageplanname: ''
  },
  multi: {
    medianame: '',
    foldname: '项目分类',
    foldareavername: '项目名称',
    addisplayformname: '',
    pagesortname: '',
    adcolorname: '',
    adspecname: '',
    weeksettingname: '',
    foldpageplanname: ''
  }
}
