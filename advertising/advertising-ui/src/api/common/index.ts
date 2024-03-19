import { number } from 'echarts'
/*
 * @Author: suny
 * @Date: 2023-10-30 16:03:34
 * @LastEditTime: 2023-10-31 08:56:48
 * @LastEditors: suny
 * @Description: 公共API
 */

import { IDataCombo } from '@/types/common/DataCombo'

/**
 * 根据传入的枚举，转换为DataCombo下拉对象
 * @param Objeenum
 * @returns
 */
export const getEnumCombo = (enumObj: any) => {
  const result: IDataCombo[] = []

  for (const key in enumObj) {
    if (isNaN(Number(key))) {
      result.push({ id: enumObj[key], name: key } as IDataCombo)
    }
  }

  return result
}
