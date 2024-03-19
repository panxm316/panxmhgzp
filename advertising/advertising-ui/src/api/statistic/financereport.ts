/*
 * @Author: lhl
 * @Date: 2023-01-16
 * @Description: 财务报表API
 */
import { ISynQueryVO } from '@/types/views/statistic/reportforms'
import request from '@/utils/request'

/**
 * 广告实收明细表按部门汇总
 * @param
 * @returns
 */
export const exportAdvertisingIncomeDepApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingIncomeDep',
    method: 'get'
  })
}
/**
 * 广告实收明细表按部门汇总导出EXCEL
 * @param
 * @returns
 */
export const exportAdvertisingRealIncomeExcelApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingRealIncomeExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 广告实收明细表按年度、业务汇总
 * @param
 * @returns
 */
export const exportAdvertisingYearMediaApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingYearMedia',
    method: 'get'
  })
}

/**
 * 广告实收明细表按业务、年份汇总导出EXCEL
 * @param
 * @returns
 */
export const exportAdvertisingYearMediaExcelApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingYearMediaExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 广告实收明细表按部门、业务汇总
 * @param
 * @returns
 */
export const exportAdvertisingDeptMediaApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingDeptMedia',
    method: 'get'
  })
}
/**
 * 广告实收明细表按部门、业务汇总导出EXCEL
 * @param
 * @returns
 */
export const exportAdvertisingDeptMediaExcelApi = () => {
  return request({
    url: '/statistic/financestatistic/exportAdvertisingDeptMediaExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 区域实收明细表（广告+经营）
 * @param
 * @returns
 */
export const exportAreaIncomeCombinApi = () => {
  return request({
    url: '/statistic/areareceived/exportAreaIncomeCombin',
    method: 'get'
  })
}
/**
 * 区域实收明细表导出EXCEL
 * @param
 * @returns
 */
export const exportAreaIncomeOpExcelApi = (data: { reportFormId: string }) => {
  return request({
    url: '/statistic/areareceived/exportAreaIncomeOpExcel',
    responseType: 'blob',
    method: 'get',
    params: data
  })
}
/**
 * 区域实收明细表（日报）
 * @param
 * @returns
 */
export const exportAreaIncomeOpApi = (data: { reportFormId: string }) => {
  return request({
    url: '/statistic/areareceived/exportAreaIncomeOp',
    method: 'get',
    params: data
  })
}
/**
 * 部门完成统计表
 * @param
 * @returns
 */
export const exportDeptReceivableApi = () => {
  return request({
    url: '/statistic/depReceivable/exportDeptReceivable',
    method: 'get'
  })
}
/**
 * 部门完成统计表导出EXCEL
 * @param
 * @returns
 */
export const exportDeptReceivableExcelApi = () => {
  return request({
    url: '/statistic/depReceivable/exportDeptReceivableExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 区域完成统计表
 * @param
 * @returns
 */
export const exportAreaReceivableApi = () => {
  return request({
    url: '/statistic/depReceivable/exportAreaReceivable',
    method: 'get'
  })
}
/**
 * 区域完成统计表导出EXCEL
 * @param
 * @returns
 */
export const exportAreaReceivableExcelApi = () => {
  return request({
    url: '/statistic/depReceivable/exportAreaReceivableExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 区域应收统计表
 * @param
 * @returns
 */
export const exportAreaIncomeReceivableApi = () => {
  return request({
    url: '/statistic/depReceivable/exportAreaIncomeReceivable',
    method: 'get'
  })
}
/**
 * 区域应收统计表导出EXCEL
 * @param
 * @returns
 */
export const exportAreaIncomeReceivableExcelApi = () => {
  return request({
    url: '/statistic/depReceivable/exportAreaIncomeReceivableExcel',
    responseType: 'blob',
    method: 'get'
  })
}
/**
 * 广告应收分析
 * @param
 * @returns
 */
export const exportReceivableAnalysisApi = (data: { mediaId: string; dateTime: string }) => {
  return request({
    url: '/statistic/depReceivable/exportReceivableAnalysis',
    method: 'get',
    params: data
  })
}
/**
 * 广告应收分析导出EXCEL
 * @param
 * @returns
 */
export const exportReceivableAnalysisExcelApi = (data: { mediaId: string; dateTime: string }) => {
  return request({
    url: '/statistic/depReceivable/exportReceivableAnalysisExcel',
    responseType: 'blob',
    method: 'get',
    params: data
  })
}
/**
 * 查询订单变动
 * @param
 * @returns
 */
export const queryChangeOrderApi = (data: {
  dateTime: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/statistic/depReceivable/queryChangeOrder',
    method: 'get',
    params: data
  })
}

/**
 * 查询订单变动导出EXCEL
 * @param
 * @returns
 */
export const exportChangeOrderExcelApi = (data: {
  dateTime: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/statistic/depReceivable/exportChangeOrderExcel',
    responseType: 'blob',
    method: 'get',
    params: data
  })
}

/**
 * 综合查询
 * @param param
 * @returns
 */
export const synQueryApi = (data: ISynQueryVO) => {
  return request({
    url: '/statistic/depReceivable/synQuery',
    method: 'post',
    data: data
  })
}

/**
 * 客户来源汇总查询
 * @param
 * @returns
 */
export const customerResourceQueryApi = (data: {
  level: string
  startTime: string
  endTime: string
}) => {
  return request({
    url: '/statistic/depReceivable/customerResourceQuery',
    method: 'get',
    params: data
  })
}

/**
 * 客户来源明细查询
 * @param
 * @returns
 */
export const getCustomerDetailApi = (data: { id: string; startTime: string; endTime: string }) => {
  return request({
    url: '/statistic/depReceivable/getCustomerDetail',
    method: 'get',
    params: data
  })
}
