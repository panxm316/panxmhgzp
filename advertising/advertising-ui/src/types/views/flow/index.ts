/**
 * 审批流程节点
 */
export type TProcessNode = {
  id: string
  nodeName: string
  /** type 1是审批人 2是抄送人 4是条件分支 5是并行分支 3是条件 */
  type: number
  /** 1指定成员 2部门主管 3角色 4发起人自选 5发起人自己 7连续多级主管 8表单人员 9表单部门 10指定部门主管 11系统自动拒绝 */
  assignedType?: number
  childNode?: TProcessNode
  conditionList?: TGroupCondition[]
  conditionNodes?: TProcessNode[]
  /** 第几级部门负责人 */
  deptLeaderLevel?: number
  /** allUser:部门人员,leader:部门主管 */
  deptUserType?: string
  error?: boolean
  formPerms?: TMapString
  /** 人员选择 */
  formUserId?: string
  /** 审批人员部门名称 */
  formUserName?: string
  groupMode?: boolean
  headId?: string
  /** false 单选  true多选 */
  multiple?: boolean
  /** 多人审批的选项 1:会签(需要所有审批人同意) 2或签(一名审批人同意即可) 3 依次审批(按顺序依次审批) */
  multipleMode?: number
  nobody?: TNobody
  /** 多人审批的人员信息 */
  nodeUserList?: TNodeUser[]
  parentId?: string
  /** 节点描述 */
  placeHolder?: string
  refuse?: TRefuse
  tailId?: string
} | null

/**
 * 组条件
 *
 * GroupCondition
 */
export type TGroupCondition = {
  /** 条件表单 */
  conditionList?: TCondition[]
  /** 或false 且true */
  mode?: boolean
}

/**
 * 条件
 *
 * Condition
 */
export type TCondition = {
  /** 表单对应的表达式 例：in 属于；notin 不属于 */
  expression?: string
  /** 设置条件的条件key */
  key?: string
  /** 表单类型 例：SelectUser*/
  keyType?: string
  /** 条件表单设置的值 */
  value?: { [key: string]: any }
}

export type TMapString = {
  [key: string]: any
}

/**
 * 审批人为空时  TO_PASS:自动通过,TO_REFUSE:自动拒绝,TO_ADMIN:转交给管理员:TO_USER
 */
export type TNobody = {
  assignedUser?: TNodeUser[]
  handler?: string
}

/**
 * 节点用户对象
 *
 * NodeUser
 */
export type TNodeUser = {
  avatar?: string
  /**
   * 用户od
   */
  id?: string
  /**
   * 用户名称
   */
  name?: string
  /**
   * 选择
   */
  selected?: boolean
  /**
   * 类型
   */
  type?: string
}

/**
 * 审批被拒绝 TO_END:直接结束流程,TO_NODE:驳回到指定节点
 */
export type TRefuse = {
  handler?: string
  nodeId?: string
}

/**
 * 流程节点显示对象
 *
 * NodeVo
 */
export type TNodeVo = {
  /**
   * 分支列表
   */
  branch?: TNodeVo[]
  /**
   * 子级列表
   */
  children?: TNodeVo[]
  /**
   * nodeId
   */
  id: string
  /**
   * 是否多选
   */
  multiple?: boolean
  /**
   * 节点名称
   */
  name?: string
  /**
   * 显示
   */
  placeholder?: string
  /**
   * 发起人选择用户
   */
  selectUser?: boolean
  /**
   * 状态 1进行中2已完成
   */
  status?: number
  /**
   * 节点类型
   */
  type?: { [key: string]: any }
  /**
   * 用户列表
   */
  userVoList?: TUserVo[]
}

/**
 * com.hgzp.advertising.pagemodel.flow.UserVo
 *
 * UserVo
 */
export type TUserVo = {
  /**
   * 意见
   */
  approveDesc?: string
  avatar?: string
  /**
   * 用户od
   */
  id?: string
  /**
   * 用户名称
   */
  name?: string
  operType?: string
  showTime?: string
  /**
   * 状态 1进行中2已完成
   */
  status?: number
}
