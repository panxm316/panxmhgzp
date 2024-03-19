<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-02-21 15:15:12
 * @LastEditors: wanghl
 * @Description: 媒体选择组件,选择媒体，叠次，叠次版本
-->
<template>
  <div>
    <el-select
      v-model="queryInfo.mediaid"
      placeholder="选择媒体"
      clearable
      style="width: 100px"
      @change="selsctmedia()"
    >
      <el-option v-for="item in mediaCombo" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>
    <el-select
      v-model="queryInfo.foldid"
      placeholder="叠次"
      style="width: 100px"
      clearable
      @change="handleFlod()"
    >
      <el-option v-for="item in foldList" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>
    <el-select
      v-model="queryInfo.foldareaverid"
      placeholder="叠次版本"
      style="width: 100px"
      clearable
      @change="handleFlodAreaver()"
    >
      <el-option
        v-for="item in foldAreaverList"
        :key="item.id"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
  </div>
</template>

<script setup lang="ts">
import type { TAdCustomer, TQueryCustomer, Twcustomerfiles } from '@/types/views/customer'
import type {
  TAFoldPagePlan,
  TQFoldPagePlan,
  TAFoldPagePlanOne
} from '@/types/views/schedule/foldpageplan'
import {
  getFoldAreaverComboApi,
  getFoldPagePlanListApi,
  getPageListtApi,
  queryOredrPredetermineListAPI
} from '@/api/schedule/seekschedule'
import { IFoldVO } from '@/types/views/media/fold'
import { getFoldComboApi } from '@/api/media/foldareaver'

import { getCustomerByIdApi } from '@/api/customer/index'
import type { IAxios } from 'axios'
import { formatDate } from '@/utils/index'
import useUserStore from '@/store/modules/user'
const userStore = useUserStore()
//  loginUserId: userStore.user?.userid.toString(), // 登录id
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboByTypeApi } from '@/api/media/media'
defineOptions({
  name: 'HgSelectMedFoldFoldare'
})
const emit = defineEmits<{
  selectiondata: [data: TQFoldPagePlan]
}>()
/**
 * 媒体列表
 */
const mediaCombo = ref<IDataCombo[]>([])
/** 叠次列表 */
const foldList = ref<any>([])
/** 叠次版本列表 */
const foldAreaverList = ref<any>([])
/** 查询全部 */
const queryInfo = reactive<TQFoldPagePlan>({
  /**
   * 叠次版本id
   */
  foldareaverid: '',
  /**
   * 叠次版本名称
   */
  foldareavername: '',
  /**
   * 叠次ID
   */
  foldid: '',
  /**
   * 叠次名称
   */
  foldname: '',
  /**
   * 媒体id
   */
  mediaid: '',
  /**
   * 媒体名称
   */
  medianame: '',
  /**
   * 媒体类型key
   */
  mediatypekey: 'paper'
})
onMounted(() => {
  getMediaDataCombo()
})
/**
 * @description: 获取媒体下拉
 * @return {*}
 */
const getMediaDataCombo = () => {
  getMediaDataComboByTypeApi({ type: 'paper' }).then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 媒体传值
 */
const selsctmedia = () => {
  queryInfo.foldid = ''
  queryInfo.foldareaverid = ''
  getFoldPageList()
  /**媒体名称赋值 */
  queryInfo.medianame = mediaCombo.value.find((item) => item.id === queryInfo.mediaid)?.name
  emit('selectiondata', queryInfo as TQFoldPagePlan)
}
/**
 * 获取叠次列表
 */
const getFoldPageList = () => {
  getFoldComboApi(queryInfo.mediaid as string).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldList.value = res.obj
      console.log(foldList.value)
    }
  })
}
/**
 * 叠次选择事件
 */
const handleFlod = () => {
  queryInfo.foldareaverid = ''
  getFoldAreaverList()
  /**叠次名称赋值 */
  queryInfo.foldname = foldList.value.find((item: any) => item.id === queryInfo.foldid)?.name
  emit('selectiondata', queryInfo as TQFoldPagePlan)
}

/**
 * 获取叠次版本列表
 */
const getFoldAreaverList = () => {
  getFoldAreaverComboApi(queryInfo.foldid as string).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldAreaverList.value = res.obj
      console.log(foldAreaverList.value)
    }
  })
}
/**
 * 叠次版本选择事件
 */
const handleFlodAreaver = () => {
  /**叠次版本名称赋值 */
  queryInfo.foldareavername = foldAreaverList.value.find(
    (item: any) => item.id === queryInfo.foldareaverid
  )?.name
  emit('selectiondata', queryInfo as TQFoldPagePlan)
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
:deep(.el-form-item__label) {
  font-weight: bold !important;
}
.el-form-item {
  margin-bottom: 15px;
}
</style>
