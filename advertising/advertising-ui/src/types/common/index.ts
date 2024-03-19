/**
 * 统一分页传参类型
 */
export interface IPageRequest {
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
}
/**
 * 统一查询传参类型
 */
export interface IBaseQueryInfo {
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
}

/**
 * 查询合并类型
 */
export type TQueryInfo = IBaseQueryInfo & IPageRequest

/** ztree组件选中统一返回参数 */
export type TSelectZtree = {
  /** 节点ID */
  id: string
  /** 父节点ID 0代表没有父节点 */
  parentId: string
  /** 节点名称 */
  name: string
  /** 节点层级 */
  level: number
  /** 节点类型 */
  stype?: string
  parentName?: string
}

/**
 * 分布类型
 */
export type TPagination = {
  pageSizes: number[]
  total: number
}

export type TBaseDTO = {
  /** 业务对象缓存key */
  cacheDTOKey?: string
}
