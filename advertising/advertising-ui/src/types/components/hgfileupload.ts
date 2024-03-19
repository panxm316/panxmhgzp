/*
 * @Author: suny
 * @Date: 2023-09-04 10:52:45
 * @LastEditTime: 2023-11-07 09:05:48
 * @LastEditors: suny
 * @Description:
 */
/**
 * 上传组件参数对象
 */
export type THgFileUploadParam = {
  /** 接口调用地址 */
  server: string
  /** {Arroy} [可选] [默认值：null] 指定接受哪些类型的文件 */
  accept?: TAccept
  /** 上传个数,验证文件总数量, 超出则不允许加入队列 */
  filenumlimit?: number
  /** 是否支持多选 */
  multiple: boolean
  /** 是否限定文件类型,有accept或者storytypes时不起作用，为空或者false时只支持图片格式 */
  typelimit?: boolean
  /** 支持的文件类型数组 */
  storytypes: string[]
}
/**
 * 可接受的文件类型参数
 */
export type TAccept = {
  /** {String} 文字描述 */
  title: string
  /** {String} 允许的文件后缀，不带点，多个用逗号分割 */
  extensions?: string
  /** {String} 多个用逗号分割 */
  mimeTypes: string
}
/**
 * 上传后获取到的文件对象
 */
export type TUpFile = {
  /** 文件统一文件预览地址 */
  url?: string
  /** 文件下载url  视频转码播放地址 */
  durl?: string
  /** 文件后缀扩展名 */
  sfileformat: string
  /** 统一文件编号SHA1 */
  sfileid: string
  /** 文件大小 */
  sfilesize: string | number
  /** 文件正文 */
  sfilecomment: string
  /**
   * 文件格式类型(Photo、Video、Audio、Office、Application)
   */
  sfiletype?: string
  /** 源文件名 */
  soriginalfile: string
  /** 图片文件的长*宽 */
  sphotoheightandwidth?: string
}
