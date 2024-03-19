/*
 * @Author: suny
 * @Date: 2023-10-12 15:04:00
 * @LastEditTime: 2023-10-14 12:46:00
 * @LastEditors: suny
 * @Description:
 */
export interface FormGroupVO {
  name: string
  formList: Partial<FormVO>[]
}
export interface FormVO {
  name: string | ''
  id: String | ''
  type: string | ''
  typeName: string | ''
  placeholder: string
  required: boolean
  icon: String
  props: Partial<FormConfigVO>
}
export interface FormConfigVO {
  minLength: Number
  maxLength: Number

  value: any | ''
  regex: string | ''
  regexDesc: string | ''
  min: Number
  max: Number
  radixNum: Number
  showChinese: boolean
  self: boolean
  multi: boolean
  showThousandSymbol: boolean
  fileList: any[]
  unit: string
  options: FormConfigOptionVO[]
  maxSize: Number
  suffixArray: String[]
}

export interface FormConfigOptionVO {
  key: String
  value: String
}
export interface FormConfigUserVO {
  id: string
  name: string
  type: string
  // avatar: string
}
