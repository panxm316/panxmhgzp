/*
 * @Author: caogd
 * @Date: 2023-08-23 11:07:31
 * @LastEditTime: 2023-10-31 10:36:51
 * @LastEditors: caogd
 * @Description: 经营主体类型文件
 */
/**
 * 经营主体返回提交主类
 */
export interface IBusinessentity {
  /**
   * id
   */
  id?: string
  /**
   * 名称
   */
  sname: string
  /**
   * 是否启用
   */
  buse: boolean
  /**
   * 序号
   */
  isort: number
  /**
   * 收款方地址
   */
  saddress?: string
  /**
   * 收款方银行账号
   */
  sbankaccount?: string
  /**
   * 收款方电话
   */
  sphone?: string
  /**
   * 税目
   */
  taxitems?: string
  /**
   * 税率
   */
  taxrate?: number
  /**
   * 是否默认
   */
  bdefault: boolean
}
