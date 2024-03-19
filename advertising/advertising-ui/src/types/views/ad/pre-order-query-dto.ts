/*
 * @Author: songly
 * @Date: 2024-01-09 15:07:50
 * @LastEditTime: 2024-03-04 16:07:50
 * @LastEditors: songly
 * @Description:
 */
import useUserStore from '@/store/modules/user'

export interface PreOrderQueryDTO {
  // PageRequest 属性
  /**
   * asc/desc
   */
  order?: string
  /**
   * 当前页数
   */
  page?: number
  /**
   * 每页显示记录数
   */
  pageSize?: number
  /**
   * 排序字段
   */
  sort?: string
  // BaseQueryInfo 属性
  /**
   * 查询开始时间
   */
  startTime?: string
  /**
   * 查询结束时间
   */
  endTime?: string
  /**
   * 查询关键字
   */
  queryKey?: string
  /**
   * 当前登录用户id
   */
  loginUserId?: string
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  // 新增属性
  /**
   * 录入方式 1-广告预约 2-快速预约
   */
  bookType: number
}

export const initPreOrderQueryDTO = (bookType: number): PreOrderQueryDTO => {
  return {
    order: 'desc',
    page: 1,
    pageSize: 20,
    sort: 'createtime',
    startTime: '',
    endTime: '',
    queryKey: '',
    loginUserId: useUserStore().user?.userid.toString(),
    cacheDTOKey: '',
    bookType: bookType
  }
}
