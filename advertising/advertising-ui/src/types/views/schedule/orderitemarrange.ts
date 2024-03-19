/*
 * @Author: songly
 * @Date: 2023-12-11 16:50:57
 * @LastEditTime: 2024-03-06 18:42:33
 * @LastEditors: songly
 * @Description:订单明细安排类型
 */

export type TOrderitemarrange = {
  /** 主键   */
  id?: string
  /** 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊     */
  ibooktype: number
  /** 合同号    */
  scontractnum: string
  /** 客户id   */
  customerid?: string
  /** 客户名称   */
  customername: string
  /** 广告类型id */
  adtypeid?: string
  /**  广告类型名称   */
  adtypename: string
  /** 预刊发叠次id     */
  prefoldid?: string
  /** 预刊发叠次名称   */
  prefoldname: string
  /** 预刊发叠次版本id   */
  prefoldareaverid?: string
  /** 预刊发叠次版本名称   */
  prefoldareavername: string
  /** 预刊发版面id   */
  prefoldpageplanid?: string
  /** 预刊发版面名称   */
  prefoldpageplanname: number | string
  /** 业务id */
  employid?: string
  /** 业务员名称   */
  employname: string
  /**    创建者id     */
  createempid?: string
  /** 创建者     */
  createempname: string
  /**     * 创建日期     */
  dcreatetime: Date | string

  /**     * 订单表id     */
  orderid?: string

  /**     * 订单明细id     */
  orderitemid?: string

  /**     * 媒体类型id     */
  mediatypekey: string

  /**     * 媒体类型名称     */
  mediatypename: string
  /**     * 媒体id     */
  mediaid: string

  /**     * 媒体名称     */
  medianame: string

  /**     * 刊发日期     */
  dpublishdate: Date | string

  /**     * 刊发结束日期     */
  dpublishenddate: Date | string
  /** 发布状态     */
  ipublishstatus: number

  /** 发布状态名称     */
  spublishstatusName: string

  /**     * 叠次id     */
  foldid: string

  /**     * 叠次名称     */
  foldname: string

  /**     * 叠次版本id     */
  foldareaverid: string

  /**     * 叠次版本名称     */
  foldareavername: string

  /**     * 广告形式id     */
  addisplayformid: string

  /**     * 广告形式名称     */
  addisplayformname: string

  /**     * 版面类别id     */
  pagesortid: string

  /**     * 版面类别名称     */
  pagesortname: string

  /**     * 色彩id     */
  adcolorid: string

  /**     * 色彩名称     */
  adcolorname: string

  /**  * 规格id     */
  adspecid: string

  /**     * 规格名称     */
  adspecname: string

  /**     * 宽(cm)     */
  nwidth: number

  /**    * 高(cm)     */
  nheight: number

  /**     * 版面id     */
  foldpageplanid: string

  /**     * 版面名称     */
  foldpageplanname: string

  /**     * 刊期     */
  publishnum: number

  /**     * 广告标题     */
  sadtitle: string

  /**     * 广告内容     */
  sadcontent: string

  /**     * 安排备注     */
  sremark: string

  /**     * 指定编辑人员id     */
  editorid: string

  /**     * 指定编辑人员名称     */
  editorname: string

  /**     * x坐标     */
  nleftx: number

  /**     * y坐标     */
  ntopy: number

  /**     * 最后修改者id     */
  lastoperatorid: string

  /**     * 最后修改才     */
  lastoperator: string

  /**     * 最后修改时间     */
  dlastmodifydate: Date | string

  /**     * 并发标记     */
  version: number
  /**
   * 备注
   */
  presremark?: string
  /**
   * 预刊版面列表
   */
  pageNumList?: []
}
export type TOrderitemarrangedata = {
  /** 安排备注 */
  sremark?: string
  /* * 指定编辑人员id
   */
  editorid?: string
  /**
   * 指定编辑人员名称
   */
  editorname?: string
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
  foldpageplanname?: string
  /**
   * 订单明细id(多个以逗号隔开)
   */
  orderitemids?: TOrderitemarrangeId[]
}
export type TOrderitemarrangeId = {
  /**
   * Id
   */
  id?: string
  /**
   * 订单表id
   */
  orderid: string

  /**
   * 订单明细id
   */
  orderitemid: string
  /**
   * 并发标记
   */
  version?: number
  /**
   * x坐标
   */
  nleftx: number
  /**
   * y坐标
   */
  ntopy: number
}
export type TOrderitemarrangeSearch = {
  /** asc/desc*/
  order?: string
  /** 当前页*/
  page?: number
  /** 每页显示记录数*/
  pageSize?: number
  /** 排序字段*/
  sort?: string
  /** 编辑人员id*/
  editorid?: string
  /** 刊发日期*/
  dpublishdate: Date | string
  /** 媒体id*/
  mediaid?: string
  /** 叠次id*/
  foldid?: string
  /** 叠次版本id*/
  foldareaverid?: string
  /** 广告类型id*/
  adtypeid?: string
  /** 查询关键字*/
  // queryKey: string
}
export type TOrderItemArrangeInfo = {
  /**
   * 广告类型id
   */
  adtypeid?: string
  /**
   * 刊发日期
   */
  dpublishdate?: string
  /**
   * 编辑人员id
   */
  editorid?: string
  /**
   * 叠次版本id
   */
  foldareaverid?: string
  /**
   * 叠次id
   */
  foldid?: string
  /**
   * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
   */
  ibooktype?: string
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体类型key
   */
  mediatypekey?: string
  /**
   * 版号
   */
  pagenum?: string
  /**
   * 查询关键字
   */
  queryKey?: string
}
/**
 * @description: 更新订单安排坐标
 */
export type TupdateOrderitemarrangePos = {
  /**
   * 安排Id
   */
  id?: string
  /**
   * x坐标
   */
  nleftx?: string
  /**
   * y坐标
   */
  ntopy?: string
}
/**
 * @description: 修改订单明细发布状态
 */
export type TupdateOrderitemPublishStatus = {
  /**
   * 安排Id
   */
  id?: string
  bPublish?: boolean
}
