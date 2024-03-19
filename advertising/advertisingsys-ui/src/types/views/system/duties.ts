/*
 * @Author: yanz
 * @Date: 2023-08-28 16:25:00
 * @LastEditors: yanz
 * @LastEditTime: 2023-08-31 17:16:49
 * @Description: 人员职务
 */
export interface IDuties {
  /**
   * 主键
   */
  id?: number
  /**
   * 序号
   */
  isort: number
  /**
   * 职务名称
   */
  sname: string
  /**
   * 启用标志
   */
  buse: boolean
}
