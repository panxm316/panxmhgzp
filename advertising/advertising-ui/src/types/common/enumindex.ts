/*
 * @Author: caogd
 * @Date: 2023-08-29 17:07:52
 * @LastEditTime: 2024-03-16 08:50:01
 * @LastEditors: songly
 * @Description: 公共枚举
 */

/**
 * 部门树URL标志枚举 命名规则url进行拼接
 */
export enum EHgDeptZtreeUrl {
  /** 获取部门树 */
  DEPT_GETDEPTTREE,
  /** 媒体信息树 */
  MEDIA_GETSYSMEDIATREE,
  /** 获取部门人员树 */
  EMP_GETDEPTEMPLOY,
  /** 获取部门业务员树 */
  EMP_GETDEPTEMPLOYPERSON,
  /** 获取行业树 */
  EMP_GETDEPTEMPLOYINDUSTRY,
  /** 媒体叠次树 */
  MEDIA_GETSYSMEDIAFLOADTREE,
  /** 媒体叠次版本树 */
  MEDIA_GETSYSMEDIAFLOADAREAVERTREE,
  /** 报表配置部门树 */
  DEPT_GETREPORTDEPTTREE,
  /** 获取客户来源树 */
  CUSTOMER_GETCUSTOMERSOURCETREE
}
/**
 * 单选树url标志枚举 命名规则url进行拼接
 */
export enum EHgSingleZtreeUrl {
  /** 获取叠次树 */
  PRICE_GETMEDIAFOLDTREE,
  /** 获取星期信息 */
  PRICE_GETWEEKSETTINGTREE,
  /** 根据媒体叠次获取叠次版本 */
  PRICE_GETFOLDAREAVERTREELIST,
  /** 根据媒体类型、叠次获取版面类别 */
  PRICE_GETPAGESORTTREELIST,
  /** 根据媒体类型获取色彩 */
  PRICE_GETADCOLORTREELIST,
  /** 根据媒体类型获取广告形式 */
  PRICE_GETADDISPLAYFORMTREELIST,
  /** 根据媒体id获取广告规格 */
  PRICE_GETADSPECTREELIST,
  /** 根据预刊登日期获取版面计划 */
  PAGEPLANE_GETFOLDPAGEPLANETREELIST,
  /** 获取有效的媒体，版面，叠次树 */
  PRICE_GETMEDIAFOLDAREAVERTREE,
  /** 获取有效媒体的版面计划树（含有非报刊媒体） */
  PAGEPLANE_GETFOLDPAGEPLANETREELISTAll,
  /** 获取常用菜单权限 */
  PAGEPLANE_GETMYMENU
}

/** 审批状态 */
export enum EApproveStatus {
  待审,
  在审,
  通过,
  否决,
  撤销,
  无效
}
/** 工作报告类型 */
export enum EWorkReportType {
  日报,
  周报,
  月报,
  年报,
  客户服务
}
/** 工作类型 */
export enum EWorkMode {
  电话,
  微信,
  上门,
  综合,
  其他
}
/** 到账核对分配状态 */
export enum EStatus {
  待提交,
  已提交,
  已确认,
  已退回,
  已核销
}

/**
 * Description：任务设置类型枚举
 * Author：lhl
 * Date：2023-11-07
 * */
export enum ETaskType {
  部门,
  人员
}

/**
 * Description：任务设置类别枚举
 * Author：lhl
 * Date：2023-11-07
 * */
export enum ETaskCategory {
  年度,
  月度
}
/** 客户服务类型 */
export enum ECustomerService {
  客户投诉,
  客户建议,
  客户调查
}
/** 报刊类型 */
export enum EPaperType {
  foldName = '叠次',
  foldareaverName = '叠次版本',
  pagetypeName = '版面类别',
  adspecName = '广告规格',
  addisplayformName = '广告形式',
  adcolorName = '色彩'
}
/** App类型 */
export enum EAppType {
  foldName = '位置',
  foldareaverName = '频道|分类',
  pagetypeName = '内容|曝光率',
  adspecName = '尺寸|周期',
  addisplayformName = '广告形式',
  adcolorName = '广告样式'
}
/** 两微类型 */
export enum EWeiType {
  foldName = '媒体',
  foldareaverName = '',
  pagetypeName = '广告位置',
  adspecName = '尺寸',
  addisplayformName = '广告形式',
  adcolorName = ''
}
/** 多元化类型 */
export enum EMultiType {
  foldName = '项目分类',
  foldareaverName = '项目名称',
  pagetypeName = '',
  adspecName = '',
  addisplayformName = '',
  adcolorName = ''
}
/** 媒体类型对应值 */
export const EMediaType: any = {
  paper: EPaperType,
  app: EAppType,
  wei: EWeiType,
  multi: EMultiType
}

/** 客户类型 */
export enum ECustomerType {
  直接客户,
  代理公司,
  内容生产方
}
/** 文件类型 */
export enum EFileCategory {
  广告合同 = 1,
  广告资源 = 8,
  证明 = 9
}
/** 审批流程类型 */
export enum EFlowType {
  客户挂牌审批流程 = 'flowcustomer',
  资源审批流程 = 'flowadsource',
  口头预定审批流程 = 'flowadbooking',
  电子认刊书审批流程 = 'flowcontract',
  订单审批流程 = 'floworder',
  预开发票审批流程 = 'flowpreinvoice',
  项目审批流程 = 'flowadproject',
  改加停刊审批流程 = 'floworderchange'
}
/** 审批流程类型 */
export enum EPageColor {
  黑白 = 0,
  彩色 = 1,
  套红 = 2
}
/** 开票样式 */
export enum EPreinvoiceStyle {
  专用发票 = 0,
  普通发票 = 2,
  电子发票 = 51,
  数电专票 = 81,
  数电普票 = 82
}
/** 预开发票类型 */
export enum EPreinvoiceApplyType {
  手开 = 0,
  预开 = 1,
  挂开 = 2,
  冲红 = 3,
  预收费 = 4,
  预开完成 = 5
}
/** 收款类型 */
export enum ESType {
  预收款,
  直接收款,
  银行流水
}
/** 发票状态 */
export enum EInvoiceStatus {
  无效,
  有效,
  冲红退回
}
/** 订单类别 */
export enum EOrderkind {
  本部广告,
  记者站广告,
  编辑广告,
  上门广告
}
/** 发布状态 */
// export enum EPublishstatus {
//   预约,
//   预定,
//   待发布,
//   安排,
//   见报,
//   已发布,
//   上架,
//   下架
// }
export enum EPublishstatus {
  预约 = 0,
  预定 = 1,
  // 待发布 = 2,
  安排 = 3,
  // 见报 = 4,
  已发布 = 5
  // 上架 = 6,
  // 下架 = 7
}

/** 计提状态 */
export enum ECommissionStatus {
  计算,
  确认,
  发放,
  撤销,
  待提交
}
/** 扎帐汇总类型 */
export enum EDatabackupGroupDataType {
  广告明细 = 'orderitem',
  核销明细 = 'apportiondetail'
}
/** 订单录入方式 */
export enum EOrderibooktype {
  正常,
  广告预约,
  快速预约,
  补刊
}
/** 刊发检测状态 */
export enum EIPublishCheckStatus {
  正常,
  未刊发,
  刊发错误
}
/** 业务员类型 */
export enum EEmployeeType {
  直客业务员,
  代理业务员,
  内容生产方业务员
}
/** 合同类型 */
export enum ContractType {
  销售合同 = 0,
  采购合同 = 1,
  互换合同 = 2,
  框架合同 = 3
}
/** 销售合同类型 */
export enum SalesContractType {
  常规合同 = 0,
  认刊书 = 1
}
/** 用章类型： */
export enum StampType {
  公章 = 0,
  合同专用章 = 1,
  经营合同专用章 = 2,
  法人章 = 3
}
