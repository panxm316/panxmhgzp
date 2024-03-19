/*
 * @Author: songly
 * @Date: 2023-09-01 09:53:44
 * @LastEditTime: 2024-03-18 19:28:18
 * @LastEditors: wanghl
 * @Description:广告项目
 */
export type TAdProject = {
  /** 项目流程    */
  flowId?: string
  /** 项目文件列表   */
  projectfiles?: TAdProjectFile[]
  /** 主键   */
  id?: string

  /** 名称(标题)   */
  sname: string
  /** 项目编码   */
  projectcode: string

  /** 开始日期   */
  dstartdate: Date | string

  /** 结束日期    */
  denddate: Date | string
  /** 创建人id */
  createempid: string
  /** 创建人名称    */
  screatename: string
  /** 创建时间    */
  dcreatedate: Date | string
  /** 项目说明     */
  sprojectcontent: string
  /** 项目预算     */
  nprojectbudget: number
  /** 项目成本     */
  nprojectcost: number
  /** 结项标记  */
  bprojectcomplete: number
  /** 审核状态(0待审1在审2通过3否决4撤销5无效) */
  iapprovestatus: number
  /** 序号 */
  isort: number

  /** 备注 */
  sremark: string

  /**   * 申请人id   */
  employid?: string

  /** * 申请人名称   */
  employname?: string

  /**   * 部门id   */
  deptid?: string
  /**   * 部门名称   */
  deptname?: string
  /**   *  经营主体id   */
  businessentityid?: string
  /**   * 经营主体名称   */
  businessentityname: string

  /**   *  是否付款开票   */
  bpayed?: boolean

  /**
   * 合同类型(销售合同、采购合同、互换合同、框架合同)
   */
  icontracttype: number

  /**
   * 销售合同类型(常规合同、认刊书)
   */
  isalecontracttype: number

  /**   * 合同编号   */
  scontractnum: string
  /**
   * 业绩归属人员id
   */
  newbelongid: string

  /**   * 业绩归属人员名称   */
  newbelongname: string

  /**   * 合同名称   */
  scontractname: string

  /**
   * 用章类型(多选)(公章、合同专用章、经营合同专用章、法人章等)
   */
  istamptype: number

  /**
   * 是否委托授权人
   */
  bauthorizer?: boolean

  /**   * 授权人名称   */
  authorizername: string
  /**
   * 开票总金额
   */
  invoiceamount: number
  /**
   * 支付总金额
   */
  payedamount: number
  /**
   * 我方交易资源
   */
  myreource: string

  /**
   * 我方物品估值
   */
  myresourceworth: string

  /**
   * 对方交易资源
   */
  sideresource: string

  /**
   * 对方物品估值
   */
  sireresourceworth: string

  /**
   * 配备资源
   */
  equipresource: string

  /**
   * 资源赠送或返点赠送
   */
  giveresource: string
  /**     * 合同类型(销售合同、采购合同、互换合同、框架合同)     */
  contracttypename: string
  /**
   * 销售合同类型(常规合同、认刊书)
   */
  salecontracttypename: string

  /**
   * 用章类型(多选)(公章、合同专用章、经营合同专用章、法人章等)
   */
  stamptypename: string
  /**
   * 项目成本盈余(项目成本-订单明细中的成本之和)
   */
  nprojectcostresidue: number
}
/**
 * 文件类型
 */
export type TAdProjectFile = {
  /**
   * 广告项目id
   */
  adprojectid?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 创建者ID
   */
  employid?: string
  /**
   * 主键
   */
  id?: string
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
   * 文件路径
   */
  sdurl?: string
}
