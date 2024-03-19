import { IBaseQueryInfo, IPageRequest, TBaseDTO} from '@/types/common'


/**
 * 角色保存类型
 */
export type TParameterDTO = {
  id: string
  /**
   * 名称
   */
  sname: string
  /**
   * 菜单id
   */
  menuIds: string
  /**
   * 是否全部权限
   */
  ball: boolean
  /**
   * 是否单独赋权
   */
  bselfparameter: boolean
  /**
   * 是否有效
   */
  buse: boolean
  /**
   * 单独赋权时的人员id
   */
  employId: string
  /**
   * 角色类型：0-普通，1-子报管理员，2-超级管理员
   */
  iparametertype: null
  /**
   * 序号
   */
  isort: number
  /**
   * 说明
   */
  sdesc: string
  /**
   * key
   */
  parametertypekey: string
  /**
   * 编码
   */
  scode: string
  /**
   * 类型
   */
  parametertypename: string

} & TBaseDTO

export type IParameterQuery = IPageRequest & IBaseQueryInfo