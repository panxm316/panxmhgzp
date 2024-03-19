import request from '@/utils/request'
import qs from 'qs'
import { TAdspec, TAdspecDateModelDTO, TQAdspec } from '@/types/views/media/adspec'

export const getAdspecPageListApi = (data: TQAdspec) => {
  return request({
    url: '/media/adspec/getAdspecPageList',
    method: 'get',
    params: data
  })
}
export const saveAdspecApi = (data: TAdspec) => {
  return request({
    url: '/media/adspec/saveAdspec',
    method: 'post',
    data: data
  })
}
export const updateAdspecApi = (data: TAdspec) => {
  return request({
    url: '/media/adspec/updateAdspec',
    method: 'post',
    data: data
  })
}
export const deleteAdspecByIdApi = (data: string) => {
  return request({
    url: '/media/adspec/deleteAdspecById',
    method: 'post',
    data: qs.stringify({ ids: data })
  })
}
/**
 * 获取最大序号
 * @returns
 */
export const getMaxSortApi = () => {
  return request({
    url: '/media/adspec/getMaxSort',
    method: 'get'
  })
}
export const updateAdspecDateApi = (data: TAdspecDateModelDTO) => {
  return request({
    url: '/media/adspec/updateAdspecDate',
    method: 'post',
    data: data
  })
}

/**
 * 根据媒体id获取可用的广告规格列表,已排序
 */
export const listUsableAdSpecApi = (mediaId: string) => {
  return request({
    url: '/media/adspec/listUsableAdSpec',
    method: 'get',
    params: { mediaId }
  })
}
