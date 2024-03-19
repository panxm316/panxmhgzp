<!--
 * @Author: wanghl
 * @Date: 2023-10-28 09:32:06
 * @LastEditTime: 2023-10-30 15:59:31
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <el-select
    v-if="adindustry.show === '1'"
    v-model="adindustry.adindustryid"
    placeholder="请选择行业"
    clearable
    filterable
    style="width: 160px; margin-right: 5px"
    @change="onChangeIndustryshow"
  >
    <el-option v-for="t in adindustrylist" :key="t.id" :label="t.name" :value="t.id"></el-option>
  </el-select>
  <el-select
    v-if="adindustry.show === '2'"
    v-model="adindustry.adindustryid"
    placeholder="请选择行业2"
    filterable
    style="width: 160px; margin-right: 5px"
    @change="onChangeIndustry"
  >
    <el-option v-for="t in adindustrylist" :key="t.id" :label="t.name" :value="t.id"></el-option>
  </el-select>
</template>
<script setup lang="ts">
import { ref, watch, defineComponent, reactive, onMounted } from 'vue'
import { getAdIndustryTreeApi } from '@/api/system/Industry'
import { Tadindustrylist, Tadindustry } from '@/types/components/hgindustry'
import modal from '@/plugins/modal'
import type { IAxios } from 'axios'
defineOptions({
  name: 'HgIndustry'
})
const props = defineProps<{ industry?: Tadindustry }>()
const emit = defineEmits<{ (e: 'onresponse', adindustry: Tadindustry): void }>()

const adindustry = reactive<Tadindustry>({
  adindustryid: '',
  adindustryname: '',
  show: props.industry?.show
})
const adindustrylist = ref<Tadindustrylist[]>([])
onMounted(() => {
  loaddata() // 初始化下拉值
  console.log(adindustry.show)
})
watch(
  () => props.industry,
  () => {
    adindustry.adindustryid = props.industry?.adindustryid
    adindustry.adindustryname = props.industry?.adindustryname
    adindustry.show = props.industry?.show
  },
  { deep: true }
)
/**
 * 获取客户列表
 */
const loaddata = () => {
  getAdIndustryTreeApi().then(({ obj, total }: IAxios<Tadindustrylist[]>) => {
    console.log(obj)
    adindustrylist.value = obj
  })
}
/**
 * 选择时候传值
 */
const onChangeIndustry = (e: string) => {
  const resultArr = adindustrylist.value.filter((item) => {
    return item.id === e
  })
  adindustry.adindustryname = resultArr[0].name
  console.log(adindustry)
  emit('onresponse', adindustry)
}
/**
 * 选择时候传值
 */
const onChangeIndustryshow = (e: string) => {
  emit('onresponse', adindustry)
}
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
