/**
 * @Author: lhl
 * @Date: 2024-01-17
 * @Description: 报表配置常量
 */
export default {
  // 支持的报表
  ReportForms: [
    {
      id: '0',
      value: '广告实收明细表(按部门汇总)'
    },
    {
      id: '1',
      value: '广告实收明细表(按业务汇总)'
    },
    {
      id: '2',
      value: '广告实收明细表(按部门/业务汇总)'
    },
    {
      id: '3',
      value: '区域实收明细表(广告+经营)'
    },
    {
      id: '4',
      value: '区域实收明细表(日报经营)'
    },
    {
      id: '5',
      value: '区域实收明细表(广告公司)'
    }
  ],
  // 汇总类型
  ReportFormType: [
    {
      id: '0',
      value: '部门汇总'
    },
    {
      id: '1',
      value: '业务汇总'
    },
    {
      id: '2',
      value: '部门/业务汇总'
    },
    {
      id: '3',
      value: '年度/业务汇总'
    }
  ],
  // 汇总项类型
  ReportObjectType: [
    {
      id: '0',
      value: '部门'
    },
    {
      id: '1',
      value: '业务'
    },
    {
      id: '2',
      value: '年度'
    },
    {
      id: '3',
      value: '公司'
    }
  ],
  // 业务类型
  MediaType: [
    {
      id: '0',
      value: '媒体'
    },
    {
      id: '1',
      value: '叠次'
    },
    {
      id: '2',
      value: '叠次版本'
    }
  ],

  // 筛选字段
  SynField: [
    {
      id: '合同号',
      value: 'scontractnum'
    },
    {
      id: '预定日期',
      value: 'createtime'
    },
    {
      id: '见报日期',
      value: 'prestartdate'
    },
    {
      id: '广告标题',
      value: 'sadtitle'
    },
    {
      id: '代理名称',
      value: 'agencyname'
    },
    {
      id: '客户名称',
      value: 'customername'
    },
    {
      id: '广告类型',
      value: 'adtypename'
    },
    {
      id: '广告行业',
      value: 'adindustryname'
    },
    {
      id: '应收金额',
      value: 'amountreceivable'
    },
    {
      id: '欠款金额',
      value: 'amountarrearage'
    },
    {
      id: '已付金额',
      value: 'amountreceived'
    },
    {
      id: '部门',
      value: 'deptname'
    },
    {
      id: '业务员',
      value: 'employname'
    },
    {
      id: '发票号',
      value: 'invoice'
    },
    {
      id: '发票金额',
      value: 'namount'
    },
    {
      id: '开票日期',
      value: 'dcreatetime'
    },
    {
      id: '分摊日期',
      value: 'dapportiondate'
    },
    {
      id: '分摊金额',
      value: 'namountapportion'
    },
    {
      id: '订单备注',
      value: 'sremark'
    }
  ],

  // 条件
  SynCondition: [
    {
      id: '等于',
      value: '='
    },
    {
      id: '大于',
      value: '>'
    },
    {
      id: '小于',
      value: '<'
    },
    {
      id: '不等于',
      value: '!='
    },
    {
      id: '大于等于',
      value: '>='
    },
    {
      id: '小于等于',
      value: '<='
    },
    {
      id: '介于',
      value: '<='
    },
    {
      id: '包含',
      value: '<='
    }
  ],

  // 结果范围
  SynRange: [
    {
      id: '0',
      value: '全部'
    },
    {
      id: '1',
      value: '仅见报'
    },
    {
      id: '2',
      value: '仅收费'
    },
    {
      id: '3',
      value: '见报且收费'
    }
  ],
  // 来源级别
  sourceLevel: [
    {
      id: '1',
      value: '1级'
    },
    {
      id: '2',
      value: '2级'
    },
    {
      id: '3',
      value: '3级'
    }
  ]
}

/**
 * 报表配置组对象
 */
export interface IReportFormGroupDTO {
  /**
   * id
   */
  id: string
  /**
   * 创建者ID
   */
  createempid?: string
  /**
   * 创建者名称
   */
  createempname?: string
  /**
   * 创建者名称
   */
  dcreatetime?: Date
  /**
   * 是否启用
   */
  buse?: boolean
  /**
   * 配置名称
   */
  reportname?: string
  /**
   * 报表唯一标识
   */
  applyto: string
  /**
   * 配置说明
   */
  sdescription?: string
  /**
   * 汇总类型
   */
  reporttype?: string
}

/**
 * 报表配置明细对象
 */
export interface IReportmodelItemDTO {
  /**
   * id
   */
  id: string
  /**
   * 报表模板组id
   */
  reportmodelgroupid?: string
  /**
   * 汇总项名称
   */
  sheadername?: string
  /**
   * 类型(部门,业务)
   */
  reporttype: string
  /**
   * 部门类型 0：部门 1：区域
   */
  idepttype: number
  /**
   * 字段名
   */
  scolumnname: string
  /**
   * 级别
   */
  ilevel: number
  /**
   * 数据范围名称
   */
  snames: string
  /**
   * 部门或媒体字符串
   */
  sids: string
  /**
   * 汇总类型
   */
  isort: number
}

/**
 * 汇总对象
 */
export interface IReportSumDTO {
  /**
   * 汇总记录ID
   */
  reportGroupItemId: string
  /**
   * 汇总类型（dept,media）
   */
  reporttype?: string
  /**
   * 部门/区域  0：部门 1：区域
   */
  idepttype: number
  /**
   * 数据范围ID串
   */
  sids?: string
  /**
   * 数据范围名称，用于业务类型标识 0：媒体 1：叠次 2：版本
   */
  snames: string
}

/**
 * 汇总查询请求对象
 */
export interface ISynQueryVO {
  /**
   * 过滤重复 0：不过滤  1：过滤
   */
  filterRepeat: number
  /**
   * 筛选范围 0：全部 1：仅见报 1：仅收费  2：见报且收费
   */
  range: number
  /**
   * 排序字段中文名称
   */
  sortName: string
  /**
   * 排序字段英文名称
   */
  enSortName: string
  /**
   * 字段列表
   */
  synQueryItemList: any
}

/**
 * 汇总查询字段
 */
export interface ISynQueryItem {
  /**
   * 字段中文名称
   */
  fieldname: string
  /**
   * 字段英文名称
   */
  enName: string
  /**
   * 条件操作符
   */
  conditionOp: string
  /**
   * 英文条件操作符
   */
  enConditionOp: string
  /**
   * 值
   */
  value: string
  /**
   * 值
   */
  othervalue: string
  /**
   * 条件
   */
  condition: string
  /**
   * 字段表前缀英文名称
   */
  enPreName: string
}
