/*
 * @Author: wanghl
 * @Date: 2023-10-25 11:17:07
 * @LastEditTime: 2024-03-07 14:12:36
 * @LastEditors: wanghl
 * @Description:客户类型管理
 */

import { IBaseQueryInfo, IPageRequest, TQueryInfo } from '@/types/common'
/**
 * 编辑客户
 */
export type TAdCustomer = {
  /**
   * 行业id
   */
  adindustryid?: string | undefined
  /**
   * 行业
   */
  adindustryname?: string
  /**
   * 是否删除
   */
  bdelete?: boolean | string
  /**
   * 是否个人
   */
  bindividual?: string | boolean
  /**
   * 是否启用
   */
  buse?: string | boolean
  /**
   * 是否大客户
   */
  bvip?: string | boolean
  customerProcessInstance?: []
  /**
   * 客户归属
   */
  customerbelong?: Twcustomerbelong[]
  /**
   * 客户资料
   */
  customerfiles?: Twcustomerfiles[]
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 主业务员
   */
  employname?: string
  /**
   * 审批状态
   */
  iapprovestatus?: string | number
  /**
   * 审批意见
   */
  sapprovestatus?: string
  /**
   * 编号(自增列)
   */
  icode?: number
  /**
   * 主键
   */
  id?: string | undefined
  /**
   * 序号
   */
  isort?: number
  /**
   * 父ID
   */
  parentid?: string
  /**
   * 父级名称
   */
  parentName?: string
  /**
   * 地址
   */
  saddress?: string
  /**
   * 银行及账号
   */
  sbankaccount?: string
  /**
   * 简码(拼音)
   */
  sbrevitycode?: string
  /**
   * 联系人
   */
  scontacts?: string
  /**
   * 税务登记号
   */
  screditcode?: string
  /**
   * 联系人电话
   */
  smobilephone?: string
  /**
   * 名称
   */
  sname?: string
  /**
   * 办公电话
   */
  sphone?: string
  /**
   * 邮编
   */
  spostcode?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 客户状态（活跃，非活跃。。。）
   */
  sstatus?: string | boolean
  /**
   * 类型(直接客户、代理公司、内容生产方)
   */
  itype?: number
  /**
   * 并发标志
   */
  version?: string
  /**
   * 最后操作员
   */
  lastoperator?: string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string
  /**
   * 流程Id
   */
  flowId?: string
  /**
   * 流程名称
   */
  flowName?: string
  /**
   * 品牌
   */
  sbrand?: string
  /**
   * 客户状态id
   */
  customerstatusid?: string
  /**
   * 客户状态名称
   */
  customerstatusname?: string
  /**
   * 客户分类id
   */
  customercategoryid?: string
  /**
   * 客户分类名称
   */
  customercategoryname?: string
  /**
   * 客户信誉度id
   */
  customercreditid?: string
  /**
   * 客户信誉度名称
   */
  customercreditname?: string
  /**
   * 客户来源
   */
  adfromid?: string
  /**
   * 客户来源名称
   */
  adfromname?: string
}
/**
 * 查询客户
 */
export type TQueryCustomer = {
  /**
   * 行业id
   */
  adindustryid?: string
  /**
   * 是否删除
   */
  bdelete?: string | boolean
  /**
   * 是否个人
   */
  bindividual?: string | boolean
  /**
   * 是否启用
   */
  buse?: string | boolean
  /**
   * 是否大客户
   */
  bvip?: string | boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 主业务员id
   */
  employid?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 审批状态
   */
  iapprovestatus?: string
  /**
   * 编号(自增列)
   */
  icode?: string
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
  /**
   * 客户状态（活跃，非活跃。。。）
   */
  sstatus?: string
  startTime?: string
  /**
   * 类型(直接客户、代理公司、内容生产方)
   */
  itype?: string | number
  /**
   * 客户分类id
   */
  customercategoryid?: string
  /**
   * 客户信誉度id
   */
  customercreditid?: string
  /**
   * 客户状态（活跃，非活跃。。。）
   */
  customerstatusid?: string
  /**
   * 仅我的客户
   */
  bmycustomer?: boolean
}
/**
 * <p>
 * 客户归属业务员
 * </p>
 *
 * Twcustomerbelong
 */
export interface Twcustomerbelong {
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 申请表ID
   */
  applicationid?: string
  /**
   * 是否主业务员
   */
  bprimary?: string | boolean
  /**
   * 客户d
   */
  customerid?: string
  /**
   * 创建日期
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
   * 审批状态
   */
  iapprovestatus?: string
  /**
   * 主键
   */
  id?: string
  // /**
  //  * 工作流申请表id
  //  */
  // applicationid?: string
  // /**
  //  * 申请表名称
  //  */
  // applicationName?: string
  // /**
  //  * 是否主业务员
  //  */
  // bprimary?: boolean | string
  // /**
  //  * 客户id
  //  */
  // customerid?: string
  // /**
  //  * 业务员名称
  //  */
  // customerName?: string
  // /**
  //  * 创建日期
  //  */
  // dcreatetime?: string
  // /**
  //  * 部门id
  //  */
  // deptid?: string
  // /**
  //  * 部门名称
  //  */
  // deptname?: string
  // /**
  //  * 业务员id
  //  */
  // employid?: string
  // /**
  //  * 审批状态
  //  */
  // iapprovestatus?: string
  // /**
  //  * 主键
  //  */
  // id?: string
}

/**
 * <p>
 * 客户文件表
 * </p>
 *
 * Twcustomerfiles
 */
export interface Twcustomerfiles {
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 创建人id
   */
  createempid?: string
  /**
   * 客户id
   */
  customerid?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
   */
  ifilecategory?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 文件描述
   */
  sdescription?: string
  /**
   * 文件分类(资质certificate、名片card、出版流程单pageprocessorder、认刊书contract)
   */
  sfilecategory?: string
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
   * 工作报告
   */
  workreportid?: string
  /**
   * 预览链接
   */
  durl?: string
}
/**
 * 申请审核
 */
export interface TCustomerApprove {
  // 流程id
  flowId: string
  // 业务id
  id: string
  iapproveType?: number
}
/**
 * 合并客户状态
 */
export interface TcombineCustomer {
  // 主客户id
  sMainId: string
  // 被合并客户id，多个需要用，隔开
  subIds: string
}

/** 客户归属查询对象 */
export type TCustomerBelongVo = ICustomerBelongVo & IBaseQueryInfo & IPageRequest
/**
 * 客户归属查询
 */
export interface ICustomerBelongVo {
  /**
   * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
   */
  bcurflag?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 人员id
   */
  employid?: string
  /**
   * 媒体ID
   */
  medidId?: string
  /**
   * 任务额度类别 0：年度 1：月度
   */
  sTaskCategory?: string
  /**
   * 任务额度类型 0：部门 1：人员
   */
  sTaskType?: string
}
/**
 * 客户账户表
 */
export interface Twcustomeraccounts {
  /**
   * 客户id
   */
  customerid?: string | null
  /**
   * 客户名称
   */
  customername?: null | string
  /**
   * 创建时间
   */
  dcreatetime?: null | string
  /**
   * 直接客户、代理公司、内容生产方
   */
  icusttype?: number | null
  /**
   * 主键
   */
  id?: string | null
  /**
   * 最后操作员
   */
  lastoperator?: null | string
  /**
   * 最后操作员id
   */
  lastoperatorid?: string | null
  /**
   * 账户余额
   */
  namountbalance?: number | null
  /**
   * 备注
   */
  sremark?: null | string
}
