/*
 * @Author: caogd
 * @Date: 2023-09-07 13:59:30
 * @LastEditTime: 2023-09-11 10:01:36
 * @LastEditors: caogd
 * @Description: 媒体刊期接口
 */
import {
  TMediaPublicNumDto,
  TMediapublicnumQueryInfo,
  Tmediapublicnum
} from '@/types/views/media/mediapublicnum'
import request from '@/utils/request'
import qs from 'qs'
/**
 * @description: 分页查询媒体刊期
 * @param {TMediapublicnumQueryInfo} param
 * @return {*}
 */
export const getMediaPublicNumPageListApi = (param: TMediapublicnumQueryInfo) => {
  return request({
    url: '/media/mediapublicnum/getMediaPublicNumPageList',
    method: 'get',
    params: param
  })
}
/**
 * @description:  单个新增媒体刊期
 * @param {Tmediapublicnum} data
 * @return {*}
 */
export const saveMediaPublicNumApi = (data: Tmediapublicnum) => {
  return request({
    url: '/media/mediapublicnum/saveMediaPublicNum',
    method: 'post',
    data: data
  })
}
/**
 * @description: 修改媒体刊期
 * @param {Tmediapublicnum} data
 * @return {*}
 */
export const updateMediaPublicNumApi = (data: Tmediapublicnum) => {
  return request({
    url: '/media/mediapublicnum/updateMediaPublicNum',
    method: 'post',
    data: data
  })
}
/**
 * @description:  删除媒体刊期多个支持","分割
 * @param {string} ids
 * @return {*}
 */
export const deleteMediaPublicNumApi = (ids: string) => {
  return request({
    url: '/media/mediapublicnum/deleteMediaPublicNum',
    method: 'post',
    data: qs.stringify({ ids: ids })
  })
}
/**
 * @description: 批量新增
 * @param {TMediaPublicNumDto} data
 * @return {*}
 */
export const saveBatchMediaPublicNumApi = (data: TMediaPublicNumDto) => {
  return request({
    url: '/media/mediapublicnum/saveBatchMediaPublicNum',
    method: 'post',
    data: data
  })
}
