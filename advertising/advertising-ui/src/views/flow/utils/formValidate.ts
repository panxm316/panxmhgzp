import { isBlank, isNotBlank, getNumberRadixNum } from '@/utils/index'

export function inputValidate(configValue: any, proxy: any) {
  const minLength = configValue.props.minLength
  const maxLength = configValue.props.maxLength
  const value = configValue.props.value
  const regex = configValue.props.regex
  const regexDesc = configValue.props.regexDesc

  if (minLength && maxLength && maxLength < minLength) {
    return {
      valid: false,
      msg: configValue.name + ':长度设置错误'
    }
  }
  if (isNotBlank(regex)) {
    if (!isNotBlank(regexDesc)) {
      return {
        valid: false,
        msg: configValue.name + ':请填写正则表达式提示语'
      }
    }
  }

  if (isNotBlank(value)) {
    if (minLength) {
      if (value.length < minLength) {
        return {
          valid: false,
          msg: configValue.name + ':默认值长度不能小于' + minLength
        }
      }
    }
    if (maxLength) {
      if (value.length > maxLength) {
        return {
          valid: false,
          msg: configValue.name + ':默认值长度不能大于' + maxLength
        }
      }
    }
    if (isNotBlank(regex)) {
      var reg = new RegExp(regex, 'g')
      if (!reg.test(value)) {
        return {
          valid: false,
          msg: configValue.name + ':默认值不符合正则表达式'
        }
      }
    }
  }
  return {
    valid: true
  }
}

export function descriptionValidate(configValue: any, proxy: any) {
  const placeHolder = configValue.placeholder
  if (isBlank(placeHolder)) {
    return {
      valid: false,
      msg: configValue.name + ':请设置提示'
    }
  }
  return {
    valid: true
  }
}
export function numberValidate(configValue: any, proxy: any) {
  const min = configValue.props.min
  const max = configValue.props.max
  const defaultValue = configValue.props.value
  const radixNum = configValue.props.radixNum

  if (min && max && max < min) {
    return {
      valid: false,
      msg: configValue.name + ':值范围设置错误'
    }
  }

  if (radixNum) {
    if (min) {
      const num = getNumberRadixNum(min)
      if (num > radixNum) {
        return {
          valid: false,
          msg: configValue.name + ':最小值小数点位数不能超过' + radixNum
        }
      }
    }
    if (max) {
      const num = getNumberRadixNum(max)
      if (num > radixNum) {
        return {
          valid: false,
          msg: configValue.name + ':最大值小数点位数不能超过' + radixNum
        }
      }
    }
  }

  if (defaultValue) {
    if (radixNum) {
      const num = getNumberRadixNum(defaultValue)
      if (num > radixNum) {
        return {
          valid: false,
          msg: configValue.name + ':默认值小数点位数不能超过' + radixNum
        }
      }
    }
    if (min) {
      if (defaultValue < min) {
        return {
          valid: false,
          msg: configValue.name + ':默认值不能小于' + min
        }
      }
    }
    if (max) {
      if (defaultValue > max) {
        return {
          valid: false,
          msg: configValue.name + ':默认值不能大于' + max
        }
      }
    }
  }

  return {
    valid: true
  }
}
export function layoutValidate(configValue: any, proxy: any) {
  const min = configValue.props.min
  const max = configValue.props.max

  if (min && max && max < min) {
    return {
      valid: false,
      msg: configValue.name + ':数量范围设置错误'
    }
  }
  const value = configValue.props.value
  if (value.length === 0) {
    return {
      valid: false,
      msg: configValue.name + ':内部表单不能为空'
    }
  }
  for (var item of value) {
    const formValidateDictElement = formValidateDict[item.type as keyof typeof formValidateDict]
    if (formValidateDictElement) {
      const result = formValidateDictElement(item, proxy) as any

      if (!result.valid) {
        return {
          valid: false,
          msg: configValue.name + ':' + result.msg
        }
      }
    }
  }

  return {
    valid: true
  }
}
export function selectValidate(configValue: any, proxy: any) {
  let keyList
  var options = configValue.props.options
  if (!options) {
    options = []
  }

  if (options.length < 1) {
    return {
      valid: false,
      msg: configValue.name + ':请设置选项'
    }
  }

  {
    keyList = options.map((w: any) => w.key)
    const newList = Array.from(new Set(keyList))
    if (keyList.length > newList.length) {
      return {
        valid: false,
        msg: configValue.name + ':选项值不能重复'
      }
    }
  }
  {
    const length = options.filter((res: any) => isBlank(res.key) || isBlank(res.value)).length
    if (length > 0) {
      return {
        valid: false,
        msg: configValue.name + ':选项不能为空'
      }
    }
  }

  {
    keyList = options.map((w: any) => w.value)
    const newList = Array.from(new Set(keyList))
    if (keyList.length > newList.length) {
      return {
        valid: false,
        msg: configValue.name + ':选项标签不能重复'
      }
    }
  }
  return {
    valid: true
  }
}
export function fileValidate(configValue: any, proxy: any) {
  const min = configValue.props.min
  const max = configValue.props.max

  if (min && max && max < min) {
    return {
      valid: false,
      msg: configValue.name + ':数量设置错误'
    }
  }

  return {
    valid: true
  }
}

export const formValidateDict = {
  Input: inputValidate,
  Textarea: inputValidate,
  Number: numberValidate,
  Money: numberValidate,
  Description: descriptionValidate,
  SingleSelect: selectValidate,
  MultiSelect: selectValidate,
  UploadFile: fileValidate,
  Layout: layoutValidate,
  UploadImage: fileValidate
}
