/*
 * @Author: suny
 * @Date: 2023-12-28 13:42:19
 * @LastEditTime: 2024-01-02 09:35:15
 * @LastEditors: suny
 * @Description: 发票相关接口API
 */

import { IInvoiceDTO, IInvoiceFilesDTO, TInvoiceQuery } from '@/types/views/finance/invoice'
import request from '@/utils/request'
import qs from 'qs'

/**
 * 获取发票查询分页列表
 * @param data
 * @returns
 */
export const getInvoicePageListApi = (data: TInvoiceQuery) => {
  return request({
    url: '/finance/invoice/getInvoicePageList',
    method: 'get',
    params: data
  })
}
/**
 * 更新发票信息接口
 * @param data
 * @returns
 */
export const updateInvoiceByInvoiceDTOApi = (data: IInvoiceDTO) => {
  return request({
    url: '/finance/invoice/updateInvoiceByInvoiceDTO',
    method: 'post',
    data: data
  })
}
/**
 * 根据id删除发票文件接口
 * @param data
 * @returns
 */
export const deleteInvoiceFilesByIdsApi = (data: { ids: string }) => {
  return request({
    url: '/finance/invoice/deleteInvoiceFilesByIds',
    method: 'post',
    data: qs.stringify(data)
  })
}
/**
 * 批量发票文件上传保存接口
 * @param data
 * @returns
 */
export const saveInfoiceFilesApi = (data: IInvoiceFilesDTO[]) => {
  return request({
    url: '/finance/invoice/saveInfoiceFiles',
    method: 'post',
    data: data
  })
}
