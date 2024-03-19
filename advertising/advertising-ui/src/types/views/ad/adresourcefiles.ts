/*
 * @Author: suny
 * @Date: 2023-11-13 08:49:54
 * @LastEditTime: 2023-11-13 13:42:20
 * @LastEditors: suny
 * @Description: 广告资源文件表DTO
 */
export interface IAdResourceFilesDTO {
  /**
   * 资源申请id
   */
  adresourceapplicationid?: string
  /**
   * 是否删除
   */
  bdelete?: boolean
  /**
   * 业务对象缓存key
   */
  cacheDTOKey?: string
  /**
   * 创建人id
   */
  createempid?: string
  /**
   * 创建人名称
   */
  createempname?: string
  /**
   * 创建日期
   */
  dcreatedate?: string
  /**
   * 创建日期
   */
  dcreatetime?: string
  /**
   * 文件下载url  视频转码播放地址
   */
  durl?: string
  /**
   * 审批状态
   */
  iapprovestatus?: number
  /**
   * 主键
   */
  id?: string
  /**
   * 1-客户合同协议、8-广告资源 9-证明
   */
  ifilecategory?: number
  /**
   * 媒体id
   */
  mediaid?: string
  /**
   * 媒体名称(附件用途，可选)
   */
  medianame?: string
  /**
   * 文件描述
   */
  sdescription?: string
  /**
   * 文件格式
   */
  sfileformat?: string
  /**
   * 统一文件ID
   */
  sfileid?: string
  /**
   * 文件大小
   */
  sfilesize?: string
  /**
   * 文件格式类型(Photo、Video、Audio、Office、Application)
   */
  sfiletype?: string
  /**
   * 源文件名
   */
  soriginalfile?: string
  /**
   * 文件统一文件地址
   */
  url?: string
}
