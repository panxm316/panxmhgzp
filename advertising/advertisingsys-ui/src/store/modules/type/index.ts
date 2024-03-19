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
   * 管理员标志
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
  simg: string
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
   * 管理部门列表
   */
  smanagedepts?: string
}
