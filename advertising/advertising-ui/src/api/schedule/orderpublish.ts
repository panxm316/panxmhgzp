/*
 * @Author: lhl
 * @Date: 2023-12-15
 * @Description: 广告刊发API
 */

import request from '@/utils/request'

/**
 * 查询日期范围内待处理刊发的广告明细目
 * @param mediaId,foldId,areaverId,stratTime,endTime,pageNum,pageSize,publiststatus
 * @returns
 */
export const getPendPublishOrderItemListApi = (data: {
  mediaId: string
  foldId: string
  areaverId: string
  stratTime: string
  endTime: string
  pageNum: number
  pageSize: number
  publishstatus: string
}) => {
  return request({
    url: '/schedule/orderitempublish/getPendPublishOrderItemList',
    method: 'get',
    params: data
  })
}

/**
 * 更新订单状态
 * @param param
 * @returns
 */
export const modifyOrderItemStatusApi = (data: { id: String; ipublishstatus: number }) => {
  return request({
    url: '/schedule/orderitempublish/modifyOrderItemStatus',
    method: 'post',
    data: data
  })
}

/**
 * 订单核查
 * @param param
 * @returns
 */
export const checkOrderItemApi = (data: {
  id: any
  ipublishcheckstatus: any
  spublishcheckcontent: any
}) => {
  return request({
    url: '/schedule/orderitempublish/checkOrderItem',
    method: 'post',
    data: data
  })
}

/**
 * 查询日期范围内待处理刊发的广告明细目
 * @param mediaId,foldId,areaverId,stratTime,endTime,pageNum,pageSize
 * @returns
 */
export const getCheckOrderListApi = (data: {
  mediaId: string
  stratTime: string
  endTime: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/schedule/orderitempublish/getCheckOrderList',
    method: 'get',
    params: data
  })
}

/**
 * 导出日期范围内待处理刊发的广告明细目
 * @param mediaId,foldId,areaverId,stratTime,endTime,pageNum,pageSize
 * @returns
 */
export const exportCheckOrderExcelApi = (data: {
  mediaId: string
  stratTime: string
  endTime: string
  pageNum: number
  pageSize: number
}) => {
  return request({
    url: '/schedule/orderitempublish/exportCheckOrderExcel',
    responseType: 'blob',
    method: 'get',
    params: data
  })
}
/**
 * 安排广告订单
 * @param param
 * @returns
 */
export const arrangeOrderItemApi = (data: {
  id: string
  mediaType: string
  arrangeData: string
  remark: string
  adURL: string
}) => {
  return request({
    url: '/schedule/orderitempublish/arrangeOrderItem',
    method: 'post',
    data: data
  })
}
/**
 * 查询媒体广告是否配置URL参数
 * @param
 * @returns
 */
export const getAdUrlParameterApi = (data: { mediaId: string }) => {
  return request({
    url: '/schedule/orderitempublish/getAdUrlParameter',
    method: 'get',
    params: data
  })
}
