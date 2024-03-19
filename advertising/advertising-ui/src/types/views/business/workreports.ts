/*
 * @Author: suny
 * @Date: 2023-10-30 10:03:46
 * @LastEditTime: 2024-03-07 17:56:37
 * @LastEditors: wanghl
 * @Description: 工作报告相关对象
 */
import { IBaseQueryInfo, IPageRequest } from '@/types/common'

/**
 * 工作报告对象
 */
export interface IWorkReportsDTO {
  /**
   * 是否启用
   */
  buse?: boolean
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
   * 审批日期
   */
  dcheckdate?: string
  /**
   * 创建日期
   */
  dcreatedate?: string
  /**
   * 结束日期
   */
  denddate?: string
  /**
   * 部门id
   */
  deptid?: string
  /**
   * 部门
   */
  deptname?: string
  /**
   * 开始日期
   */
  dstartdate?: string
  /**
   * 人员ID
   */
  employid?: string
  /**
   * 人员名称
   */
  employname?: string
  /**
   * 上传的附件
   */
  filelist?: IWorkReportFilesDTO[]
  /**
   * 审批状态
   */
  iapprovestatus?: number | string
  /**
   * 审批人id
   */
  icheckerid?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
   */
  iworktype?: number | string
  /**
   * 审批人名称
   */
  scheckername?: string
  /**
   * 审批意见
   */
  scheckopinions?: string
  /**
   * 服务反馈
   */
  sfeedback?: string
  /**
   * 工作计划
   */
  splan?: string
  /**
   * 工作难点
   */
  squestions?: string
  /**
   * 备注
   */
  sremark?: string
  /**
   * 工作内容
   */
  sworkcontent?: string
  /**
   * 工作方式(电话、微信、上门、综合、其他)
   */
  sworkmode?: string
}

/** 工作报告查询对象 */
// export type TWorkReportQuery = IWorkReportsDTO & IPageRequest & IBaseQueryInfo
export type TWorkReportQuery = IWorkReportsDTO & IPageRequest & IBaseQueryInfo

/**
 * 工作报告相关附件对象
 */
export interface IWorkReportFilesDTO {
  /**
   * 文件统一文件地址
   */
  url?: string
  /**
   * 文件下载url  视频转码播放地址
   */
  durl?: string
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
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 主键
   */
  id?: string
  /**
   * 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
   */
  ifilecategory?: number
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
   * 工作报告
   */
  workreportid?: string
}
/** 客户查询工作报告 */
export interface TpICustomerWorkReportQuery {
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 客户ID
   */
  customerid?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
   */
  iworktype?: string
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
}
