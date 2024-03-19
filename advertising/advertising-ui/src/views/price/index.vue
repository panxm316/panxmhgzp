<!--
 * @Author: suny
 * @Date: 2023-11-07 16:10:30
 * @LastEditTime: 2023-11-23 16:45:18
 * @LastEditors: wanghl
 * @Description: 价格参数管理tabs
-->
<template>
  <el-tabs v-model="activeName" class="demo-tabs">
    <el-tab-pane v-if="foldName !== ''" :label="foldName" name="fold">
      <Fold
        v-if="activeName === 'fold'"
        :type="props.foldtype || type"
        :typename="foldName"
        :tableheight="tableHeight"
      ></Fold>
    </el-tab-pane>
    <el-tab-pane v-if="foldareaverName !== ''" :label="foldareaverName" name="foldareaver">
      <FoldAreaver
        v-if="activeName === 'foldareaver'"
        :type="props.foldtype || type"
        :typename="foldName"
        :tableheight="tableHeight"
      ></FoldAreaver>
    </el-tab-pane>
    <el-tab-pane
      v-if="addisplayformName !== '' && props.foldtype === undefined"
      :label="addisplayformName"
      name="addisplayform"
    >
      <AdDisplayForm
        v-if="activeName === 'addisplayform'"
        :type="type"
        :typename="foldName"
        :tableheight="tableHeight"
      ></AdDisplayForm>
    </el-tab-pane>
    <el-tab-pane
      v-if="pagetypeName !== '' && props.foldtype === undefined"
      :label="pagetypeName"
      name="pagetype"
    >
      <PageSort
        v-if="activeName === 'pagetype'"
        :type="type"
        :typename="foldName"
        :tableheight="tableHeight"
      ></PageSort>
    </el-tab-pane>
    <el-tab-pane
      v-if="adspecName !== '' && props.foldtype === undefined"
      :label="adspecName"
      name="adspec"
    >
      <Adspec
        v-if="activeName === 'adspec'"
        :type="type"
        :typename="adspecName"
        :tableheight="tableHeight"
      ></Adspec>
    </el-tab-pane>
    <el-tab-pane
      v-if="adcolorName !== '' && props.foldtype === undefined"
      :label="adcolorName"
      name="adcolor"
    >
      <Adcolor
        v-if="activeName === 'adcolor'"
        :type="type"
        :typename="foldName"
        :tableheight="tableHeight"
      ></Adcolor>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import Fold from './Fold.vue'
import FoldAreaver from './FoldAreaver.vue'
import PageSort from './PageSort.vue'
import Adspec from './Adspec.vue'
import AdDisplayForm from './AdDisplayForm.vue'
import Adcolor from './Adcolor.vue'
import { useRoute } from 'vue-router'
import { computTableHeight } from '@/utils/index'
import { EAppType, EMultiType, EPaperType, EWeiType } from '@/types/common/enumindex'
const route = useRoute()
const activeName = ref('fold')
const type = route.fullPath.substring(route.fullPath.lastIndexOf('/') + 1)
const foldName = ref('叠次')
const foldareaverName = ref('叠次版本')
const pagetypeName = ref('版面类别')
const adspecName = ref('广告规格')
const addisplayformName = ref('广告形式')
const adcolorName = ref('色彩')
const tableHeight = ref(0)
const props = defineProps<{ foldtype?: string }>()
if (type === 'paper' || props.foldtype === 'paper') {
  foldName.value = EPaperType.foldName
  foldareaverName.value = EPaperType.foldareaverName
  pagetypeName.value = EPaperType.pagetypeName
  adspecName.value = EPaperType.adspecName
  addisplayformName.value = EPaperType.addisplayformName
  adcolorName.value = EPaperType.adcolorName
} else if (type === 'app') {
  foldName.value = EAppType.foldName
  foldareaverName.value = EAppType.foldareaverName
  pagetypeName.value = EAppType.pagetypeName
  adspecName.value = EAppType.adspecName
  addisplayformName.value = EAppType.addisplayformName
  adcolorName.value = EAppType.adcolorName
} else if (type === 'wei') {
  activeName.value = 'addisplayform'
  foldName.value = EWeiType.foldName
  foldareaverName.value = EWeiType.foldareaverName
  pagetypeName.value = EWeiType.pagetypeName
  adspecName.value = EWeiType.adspecName
  addisplayformName.value = EWeiType.addisplayformName
  adcolorName.value = EWeiType.adcolorName
} else if (type === 'multi') {
  foldName.value = EMultiType.foldName
  foldareaverName.value = EMultiType.foldareaverName
  pagetypeName.value = EMultiType.pagetypeName
  adspecName.value = EMultiType.adspecName
  addisplayformName.value = EMultiType.addisplayformName
  adcolorName.value = EMultiType.adcolorName
}
onMounted(() => {
  /**
   * 计算表格高度
   */
  if (props.foldtype === 'paper') {
    tableHeight.value = computTableHeight() - 200
  } else {
    tableHeight.value = computTableHeight() - 50
  }
})
console.log(type)
</script>

<style scoped></style>
