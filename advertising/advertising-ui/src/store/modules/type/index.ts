export interface IWithoutAnimation {
  withoutAnimation: boolean
}
export type TSize = 'large' | 'default' | 'small'
export interface IChangeSetting {
  key: string
  value: any
}

export interface IUserInfo {
  username: string
  password: string
  code: string
  uuid: string
}

/**
 * LoginInfoVo
 */
export interface ILoginInfoVo {
  /**
   * 用户信息
   */
  loginuser: ILoginUser
  /**
   * 权限字符串（客户端）
   */
  permissions: string[]
  /**
   * 版本号
   */
  sysversion?: string
  /**
   *
   */
  roles: string[]
  /**
   * office预览地址
   */
  officePreviewUrl: string
}

/**
 * 用户信息
 *
 * LoginUser
 */
export interface ILoginUser {
  /**
   * token
   */
  accessToken: string
  /**
   * 管理员标志 0：一般人员；1：普通管理员（权限范围同部门领导，所在部门及以下）；2：超级管理员（权限范围同兼职超管：完整权限）; 3:兼职超管
   */
  adminflag?: number
  /**
   * 内部人部标志 false:通讯员 true：社内人员
   */
  binner?: boolean
  /**
   * 部门id
   */
  deptid?: number
  /**
   * 部门名称
   */
  deptname?: string
  /**
   * 一级部门id
   */
  firstdeptids?: string
  /**
   * 新消息提示音
   */
  imsgaudio?: number
  /**
   * ip
   */
  ip?: string
  /**
   * 登录帐号
   */
  loginname?: string
  /**
   * mac
   */
  mac?: string
  /**
   * 兼职部门id
   */
  parttimeDept?: string
  /**
   * 验证超期
   */
  passexpired?: boolean
  /**
   * 手机号
   */
  phone?: string
  /**
   * 头像
   */
  simg?: string
  /**
   * 语言
   */
  slanguage?: string
  /**
   * 用户id
   */
  userid: string
  /**
   * 用户名
   */
  username: string
  /**
   * 性别
   */
  bsex?: boolean
  /**
   * 邮箱
   */
  semail?: string
  /**
   * 是否负责人
   */
  blead: boolean
  /**
   * 是否业务员
   */
  bsalesman: boolean
}
/**
 * 消息状态
 */
export enum EMessageType {
  /** 心跳检查 */
  HEART_CHECK_MSG = -1,
  /** 系统消息 */
  SYSTEM_MSG = 0,
  /** 个人消息 */
  PERSON_MSG = 1,
  /** 错误消息 */
  ERROR_MSG = 2,
  /** 工作流待办消息 */
  FLOW_TodoTask_MSG = 3,
  /** 工作流通过消息 */
  FLOW_ApprovePass_MSG = 4,
  /** 工作流否决消息 */
  FLOW_ApproveReject_MSG = 5
}
/**
 * 消息类型
 */
export type TMessageDataType = {
  /** 消息id */
  messageId: string
  /** 消息发送者 */
  fromUser: string
  /** 消息接收者 */
  toUser: string
  /** 消息类型 */
  type: EMessageType
  /** 消息标题 */
  messageTitle: string
  /** 消息内容 */
  messageContent: string
  /** 发送日期 */
  date: number
  /** 未读消息条数 */
  messageCount: number
  /** 流程实例id */
  processInstanceId: number
  /** 流程创建日期 */
  processInstanceCreate: string
}

export type TMessageStoreType = {
  // 系统消息
  messageData: TMessageDataType
  noticeCount: number // 通知公告
  taskCount: number // 任务数
  /** 审批消息跳转参数 */
  params: {
    processInstanceId: number
    processInstanceCreate: string
    processInstanceType: EMessageType
  }
}
