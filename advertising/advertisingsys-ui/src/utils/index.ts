import { parseTime } from './ruoyi'
import { InternalRuleItem } from 'async-validator'
import variables from '@/assets/styles/variables.module.scss'
import dayjs from 'dayjs'
/**
 * 表格时间格式化
 */
export function formatDate(cellValue: string | number | Date) {
  if (cellValue == null || cellValue === '') return ''
  var date = new Date(cellValue)
  var year = date.getFullYear()
  var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time: any, option: any) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d: any = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function getQueryObject(url: string | null) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {} as any
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * @param {string} input value
 * @returns {number} output value
 */
export function byteLength(str: string) {
  // returns the byte length of an utf8 string
  let s = str.length
  for (var i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xdc00 && code <= 0xdfff) i--
  }
  return s
}

/**
 * @param {Array} actual
 * @returns {Array}
 */
export function cleanArray(actual: string | any[]): any[] {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

/**
 * @param {Object} json
 * @returns {Array}
 */
export function param(json: { [x: string]: string | number | boolean }): any {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map((key) => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url: string): object {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {} as any
  const searchArr = search.split('&')
  searchArr.forEach((v) => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}

/**
 * @param {string} val
 * @returns {string}
 */
export function html2Text(val: string): string {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

/**
 * Merges two objects, giving the last one precedence
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function objectMerge(target: { [x: string]: any }, source: string | any[]): object {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach((property) => {
    // @ts-ignore
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

/**
 * @param {HTMLElement} element
 * @param {string} className
 */
export function toggleClass(element: { className: any }, className: string | any[]) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

/**
 * @param {string} type
 * @returns {Date}
 */
export function getTime(type: string) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export function debounce(func: any, wait: number, immediate: boolean) {
  let timeout: number | null, args: null, context: null | undefined, timestamp: number, result: any

  const later = function () {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function (this: any, ...args: any) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone(source: any): object {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments')
  }
  const targetObj: any = source.constructor === Array ? [] : {}
  Object.keys(source).forEach((keys) => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

/**
 * @param {Array} arr
 * @returns {Array}
 */
export function uniqueArr(arr: Iterable<unknown> | null | undefined) {
  return Array.from(new Set(arr))
}

/**
 * @returns {string}
 */
export function createUniqueString() {
  const timestamp = +new Date() + ''
  const randomNum = (1 + Math.random()) * 65536 + ''
  return (+(randomNum + timestamp)).toString(32)
}

/**
 * Check if an element has a class
 * @param {HTMLElement} elm
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: HTMLElement, cls: string) {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function addClass(ele: HTMLElement, cls: string) {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * Remove class from element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function removeClass(ele: HTMLElement, cls: string) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

export function makeMap(str: string, expectsLowerCase: any) {
  const map = Object.create(null)
  const list = str.split(',')
  for (let i = 0; i < list.length; i++) {
    map[list[i]] = true
  }
  return expectsLowerCase
    ? (val: string) => map[val.toLowerCase()]
    : (val: string | number) => map[val]
}

export const exportDefault = 'export default '

export const beautifierConf = {
  html: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'separate',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: false,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  },
  js: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'normal',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: true,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  }
}

// 首字母大小
export function titleCase(str: string) {
  return str.replace(/( |^)[a-z]/g, (L) => L.toUpperCase())
}

// 下划转驼峰
export function camelCase(str: string) {
  return str.replace(/_[a-z]/g, (str1) => str1.substr(-1).toUpperCase())
}

// export function isNumberStr(str: string) {
//   return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
// }
/**
 * 非数字验证
 * @param rule
 * @param value
 * @param callback
 */
export const isNumberStr = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (!value || value === '') {
    callback()
  } else {
    const bflag = /^[+-]?(0|([1-9]\d*))(\.\d+)?$/
    if (value === '' || !bflag.test(value)) {
      callback(new Error('请输入数字'))
    } else {
      callback()
    }
    callback()
  }
}
/**
 * assets图片地址
 * @param url
 * @returns
 */
export const getAssetsImage = (url: string) => {
  return new URL(`../assets/images/${url}`, import.meta.url).href
}
/**
 * 表单字符串非空判断
 * @param rule
 * @param value
 * @param callback
 */
export const required = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (!value || value.trim() === '') {
    callback(new Error(rule.message as string))
  } else {
    callback()
  }
}

/**
 * @description: 表单验证手机号
 * @return {*}
 */
export const validatePhone = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (!value || value.trim() === '') {
    // callback(new Error('请输入手机号'))
    callback()
  } else {
    const bflag = /^(?:13\d|14\d|15\d|16\d|17\d|18\d|19\d)\d{5}(\d{3}|\*{3})$/
    if (value.trim() === '' || !bflag.test(value)) {
      callback(new Error('请输入正确手机号'))
    } else {
      callback()
    }
  }
}
/**
 * @description: 密码验证
 * @return {*}
 */
export const passwordvalidator = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (
    !/(?=^.{8,}$)(?=(?:.*?\d){1})(?=(?:.*?[a-zA-Z]){1})(?!.*\s)[0-9a-zA-Z!@#$%*~()_+^&-]*$/.test(
      value
    )
  ) {
    callback(new Error(rule.message as string))
  } else {
    callback()
  }
}
/**
 *  页面中table高度计算
 * @param isPaginationBoxHeight：是否存在分页(存在分页不传值，不存在分页传false)
 * @return tableheight
 */
export const computTableHeight = (isPaginationBoxHeight: boolean = true) => {
  const navbarHeight = document.querySelector('.navbar')?.clientHeight ?? 0
  const searchBoxHeight = document.querySelector('.search_box')?.clientHeight ?? 0
  const paginationBoxHeight = document.querySelector('.pagination')?.clientHeight ?? 0
  if (isPaginationBoxHeight) {
    const tableheight =
      window.innerHeight -
      navbarHeight -
      searchBoxHeight -
      paginationBoxHeight -
      parseInt(variables.tableHeight)
    return tableheight
  } else {
    const tableheight =
      window.innerHeight - navbarHeight - searchBoxHeight - parseInt(variables.tableHeight) + 5
    return tableheight
  }
}
/**
 * 树结构区域高度计算
 * @returns treeheight + 'px'
 */
export const computTreeHeight = () => {
  const treeheight = window.innerHeight - parseInt(variables.treeHeight)
  return treeheight + 'px'
}
/**
 * 树结构右侧编辑区域高度
 * @returns treeheight + 'px'
 */
export const computEditHeight = () => {
  const navbarHeight = document.querySelector('.navbar')?.clientHeight ?? 0
  const searchBoxHeight = document.querySelector('.search_box')?.clientHeight ?? 0
  const treeheight = window.innerHeight - navbarHeight - searchBoxHeight - 20
  return treeheight + 'px'
}
// 根据data返回的每个单元格的数据判断,再修改单个表头单元格的颜色
export const tableHeaderColor = () => {
  return { background: 'linear-gradient(0deg, #409eff, #409eff)', color: '#fff' }
}
/**
 * @description: 格式化日期函数 例：YYYY-MM-DD HH:mm:ss  详细参考dayjs
 * @param {Date} value
 * @param {string} stype
 * @return {*}
 */
export const hgFormatDate = (value: Date, stype: string) => {
  if (value) {
    return dayjs(value).format(stype)
  } else {
    return value
  }
}
/**
 * @description: 邮箱格式校验
 * @return {*}
 */
export const validateSemail = (
  rule: InternalRuleItem,
  value: string,
  callback: (param?: Error) => void
) => {
  if (!value || value.trim() === '') {
    callback()
  } else {
    const bflag = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
    if (value.trim() === '' || !bflag.test(value)) {
      callback(new Error(rule.message as string))
    } else {
      callback()
    }
  }
}
/**
 *
 * @param val 输入字符串长度，汉字为2，不是汉字为1
 * @returns
 */
export const toBreak = (val: string) => {
  var str = val
  var bytesCount = 0 // 定义一个变量记录字符串总字节长度
  var pattern = new RegExp('[\u4E00-\u9FA5]+') // 汉字的正则表达式
  for (var i = 0; i < str.length; i++) {
    // 遍历原字符串
    var c = str.charAt(i) // 拿到每一个下标对应的值
    // 统计字符串的字符长度
    if (pattern.test(c)) {
      bytesCount += 2 // 如果是汉字长度+2
    } else {
      bytesCount += 1 // 不是汉字长度+1
    }
  }
  return bytesCount
}
/**
 *
 * @returns 获取屏幕分辨率宽度
 */
export const fenbianlv = () => {
  var str = window.innerWidth
  return str
}
/**
 *
 * @param json josn对象格式化方法
 * @returns 返回格式化数据
 */
export const syntaxHighlight = (json: any) => {
  if (typeof json !== 'string') {
    json = JSON.stringify(json, undefined, 2)
  }
  json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>')
  return json.replace(
    /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
    function (match: any) {
      var cls = 'number'
      if (/^"/.test(match)) {
        if (/:$/.test(match)) {
          cls = 'key'
        } else {
          cls = 'string'
        }
      } else if (/true|false/.test(match)) {
        cls = 'boolean'
      } else if (/null/.test(match)) {
        cls = 'null'
      }
      return '<span class="' + cls + '">' + match + '</span>'
    }
  )
}

export const isBlank = (s?: String) => {
  if (s === undefined || s === null || (typeof s === 'string' && s.trim().length === 0)) {
    return true
  }
  return false
}
export const isNotBlank = (s?: String) => {
  return !isBlank(s)
}
export const isNode = (obj: any) => {
  return isNotBlank(obj) && isNotBlank(obj.id)
}
export const getRandomId = () => {
  return `hgzp_${new Date().getTime().toString().substring(5)}${Math.round(
    Math.random() * 9000 + 1000
  )}`
}

// 获取数字的小数点位数
export const getNumberRadixNum = (s: Number) => {
  const strings = s.toString().split('.')
  if (strings.length <= 1) {
    return 0
  }
  return strings[1].toString().length
}
// 深拷贝
export const deepCopy = (s: any) => {
  return JSON.parse(JSON.stringify(s))
}
