/*
 * @Author: wanghl
 * @Date: 2023-08-16 15:11:03
 * @LastEditTime: 2023-10-30 15:55:36
 * @LastEditors: wanghl
 * @Description:
 */
import request from '@/utils/request'
/**
 * 方法功能:获取有效行业下拉数据
 */
export const getAdIndustryTreeApi = () => {
  return request({
    url: '/system/adindustry/getAdIndustryTree',
    method: 'get'
  })
}
