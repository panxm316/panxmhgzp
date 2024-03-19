<template>
  <el-select
    v-model="timetype"
    placeholder="请选择"
    style="width: 100px; margin-right: 5px"
    @change="onChangeTime"
  >
    <el-option v-for="t in timeoption" :key="t.label" :label="t.label" :value="t.value"></el-option>
  </el-select>
  <el-date-picker
    id="startTime"
    v-model="startTime"
    :editable="false"
    type="date"
    placeholder="选择日期"
    :clearable="false"
    style="width: 130px; margin-right: 5px"
    @change="onresponse"
    @blur="onfcus"
  >
  </el-date-picker>
  <span :class="Cookies.get('size') == 'small' ? 'zhi' : 'zhi_b'">至</span>
  <el-date-picker
    id="endTime"
    v-model="endTime"
    :editable="false"
    type="date"
    placeholder="选择日期"
    :clearable="false"
    style="width: 130px; margin-left: 5px"
    @change="onresponse"
    @blur="onfcus"
  >
  </el-date-picker>
</template>
<script setup lang="ts">
import { ref, watch, defineComponent, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { TDateTimeType, TDateType, TTimeOptionType } from '@/types/components/hgdateindex'
import modal from '@/plugins/modal'
import dayjs from 'dayjs'
import Cookies from 'js-cookie'
defineOptions({
  name: 'HgDateIndex'
})
const props = defineProps<{ initdate?: TDateType; typeflag?: string }>()
const emit = defineEmits<{ (e: 'onresponse', timedata: TDateTimeType): void }>()
const timetype = ref('2')
const startTime = ref<Date | string>('')
const endTime = ref<Date | string>('')
const timedata = reactive<TDateTimeType>({
  startTime: '',
  endTime: ''
})
const timeoption = ref<TTimeOptionType>([
  {
    label: '今天',
    value: '0'
  },
  {
    label: '三天内',
    value: '2'
  },
  {
    label: '一周内',
    value: '6'
  },
  {
    label: '二周内',
    value: '13'
  },
  {
    label: '一月内',
    value: '30'
  },
  {
    label: '自定义',
    value: '00'
  }
])
const stime = ref<Date | string>('')
const etime = ref<Date | string>('')

onMounted(() => {
  accdate() // 初始化下拉值
  onChangeTime() // 快捷键下拉操作
  inittime() // 时间初始赋值
})

watch(
  () => props.initdate,
  () => {
    inittime()
  },
  { deep: true }
)

/**
 * 时间快捷键
 */
const onChangeTime = () => {
  const end = new Date()
  const start = new Date()
  // 签发页面要求展示明天的内容
  if (props.typeflag && props.typeflag === 'publish') {
    if (timetype.value === '0') {
      start.setTime(start.getTime() - 3600 * 1000 * 24 * parseInt(timetype.value))
    } else {
      end.setTime(start.getTime() + 3600 * 1000 * 24)
      const timeNum = +timetype.value - 1
      start.setTime(start.getTime() - 3600 * 1000 * 24 * timeNum)
    }
  } else {
    start.setTime(start.getTime() - 3600 * 1000 * 24 * parseInt(timetype.value))
  }

  startTime.value = start
  endTime.value = end
  onresponse()
}
/**
 * 日期回调
 */
const onresponse = () => {
  const curDate = new Date(dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  const curendTime = new Date(dayjs(endTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  if (curDate > curendTime) {
    modal.msgWarning('开始日期不能大于结束日期')
    endTime.value = etime.value
    startTime.value = stime.value
    return false
  }
  timedata.startTime = dayjs(startTime.value).format('YYYY-MM-DD')
  timedata.endTime = dayjs(endTime.value).format('YYYY-MM-DD')
  emit('onresponse', timedata)
  // 恢复上次选择时间
  stime.value = startTime.value
  etime.value = endTime.value
}
/**
 * 初始化下拉值
 */
const accdate = () => {
  if (props.initdate) {
    if (props.initdate.timetype !== '' && props.initdate.timetype !== undefined) {
      timetype.value = props.initdate.timetype
    }
    if (props.initdate.timeoption.length > 0 && props.initdate.timeoption !== undefined) {
      timeoption.value = props.initdate.timeoption
    }
  }
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
      timetype.value = '00'
    }
    if (props.initdate.endTime !== '' && props.initdate.endTime !== undefined) {
      if (typeof props.initdate.endTime === 'string') {
        endTime.value = new Date(props.initdate.endTime)
      } else {
        endTime.value = props.initdate.endTime
      }
      timetype.value = '00'
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
