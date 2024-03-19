/**
 * @Author: lhl
 * @Date: 2023-12-19
 * @Description: 广告刊发常量设置
 */

export default {
  // 核查状态
  OrderCheckType: [
    {
      id: '0',
      value: '正常'
    },
    {
      id: '1',
      value: '未刊发'
    },
    {
      id: '2',
      value: '刊发错误'
    },
    {
      id: '3',
      value: '已解决'
    }
  ],

  // 核查状态
  OrderPublishType: [
    {
      id: '2',
      value: '待发布'
    },
    {
      id: '3',
      value: '安排'
    },
    {
      id: '4',
      value: '见报'
    },
    {
      id: '5',
      value: '已发布'
    }
  ]
}

/**
 * 核查请求对象
 */
export interface IOrderCheckDTO {
  /**
   * id
   */
  id: string

  /**
   * 核查状态
   */
  ipublishcheckstatus: string

  /**
   * 核查报告
   */
  spublishcheckcontent: string
}
