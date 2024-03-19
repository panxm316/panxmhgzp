<!--
 * @Author: lhl
 * @Date: 2023-11-07
 * @LastEditTime: 2024-03-06 17:49:08
 * @LastEditors: lhl
 * @Description: 年度范围选择组件
-->

<template>
  <el-date-picker
    v-model="startTime"
    :editable="false"
    type="year"
    placeholder="选择年度"
    :clearable="false"
    style="width: 130px; margin-right: 5px"
    @change="onresponse"
    @blur="onfcus"
  >
  </el-date-picker>
  <span :class="Cookies.get('size') == 'small' ? 'zhi' : 'zhi_b'">至</span>
  <el-date-picker
    v-model="endTime"
    :editable="false"
    type="year"
    placeholder="选择年度"
    :clearable="false"
    style="width: 130px; margin-left: 5px"
    @change="onresponse"
    @blur="onfcus"
  >
  </el-date-picker>
</template>
<script setup lang="ts">
import { ref, watch, defineComponent, reactive, onMounted } from 'vue'
import { TDateTimeType, TDateType, TTimeOptionType } from '@/types/components/hgdateindex'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import Cookies from 'js-cookie'
defineOptions({
  name: 'HgDateIndex'
})
const props = defineProps<{ initdate?: TDateTimeType; typeflag?: string }>()
const timetype = ref('2')
const startTime = ref<Date | string>('')
const endTime = ref<Date | string>('')
const stime = ref<Date | string>('')
const etime = ref<Date | string>('')
const emit = defineEmits<{ (e: 'onresponse', timedata: TDateTimeType): void }>()
const timedata = reactive<TDateTimeType>({
  startTime: '',
  endTime: ''
})

watch(
  () => props.initdate,
  () => {
    inittime()
  },
  { deep: true }
)

onMounted(() => {
  onChangeTime() // 快捷键下拉操作
  inittime() // 时间初始赋值
})

/**
 * 时间快捷键
 */
const onChangeTime = () => {
  const end = new Date()
  const start = new Date()
}
/**
 * 日期回调
 */
const onresponse = () => {
  const curDate = new Date(dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  const curendTime = new Date(dayjs(endTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  var year = new Date(startTime.value).getFullYear()
  timedata.startTime = dayjs(new Date(year + '-01-01')).format('YYYY-MM-DD')
  year = new Date(endTime.value).getFullYear()
  timedata.endTime = dayjs(new Date(year + '-12-31')).format('YYYY-MM-DD')
  emit('onresponse', timedata)
}
/**
 * 时间初始化赋值
 */
const inittime = () => {
  if (props.initdate) {
    if (props.initdate.startTime !== '' && props.initdate.startTime !== undefined) {
      if (typeof props.initdate.startTime === 'string') {
        startTime.value = new Date(props.initdate.startTime)
      } else {
        startTime.value = props.initdate.startTime
      }
    }
    if (props.initdate.endTime !== '' && props.initdate.endTime !== undefined) {
      if (typeof props.initdate.endTime === 'string') {
        endTime.value = new Date(props.initdate.endTime)
      } else {
        endTime.value = props.initdate.endTime
      }
    }
  }
}
const onfcus = () => {
  timetype.value = '00'
}

defineExpose({ inittime, onresponse })
</script>

<style scoped lang="scss">
.zhi {
  line-height: 24px;
  margin-right: 10px;
  font-size: 10px;
}
.zhi_b {
  line-height: 32px;
  margin-right: 10px;
}
</style>
