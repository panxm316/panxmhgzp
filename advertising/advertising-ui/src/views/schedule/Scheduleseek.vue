<!--
 * @Author: lhl
 * @Date: 2023-11-27
 * @LastEditTime: 2023-12-12 14:54:38
 * @LastEditors: lhl
 * @Description:查看排期计划
-->
<template>
  <div class="app-container">
    <el-row :gutter="5">
      <el-col :xs="8" :sm="7" :md="7" :lg="5" :xl="5">
        <div class="table_box left_box">
          <div class="fenge-box">
            <el-icon color="#409EFC"><star-filled /></el-icon><span>广告媒体</span
            ><el-icon color="#409EFC"><star-filled /></el-icon>
          </div>
          <div>
            <HgSingleZtree
              :scopeflag="EHgSingleZtreeUrl.PRICE_GETMEDIAFOLDTREE"
              :treeheight="600"
              :expandall="true"
              :checkboxtype="true"
              :treeparams="{ mediaType: 'paper,app,wei', getType: 'Media' }"
              @change="onChange"
            >
            </HgSingleZtree>
          </div>
        </div>
      </el-col>
      <el-col :xs="16" :sm="17" :md="17" :lg="19" :xl="19">
        <div v-show="mediaType === 0">
          <ScheduleSeekPaper ref="childPpaerRef" :industry="mediadata"></ScheduleSeekPaper>
        </div>
        <div v-show="mediaType === 1">
          <ScheduleSeekSouthernPlus ref="childRef" :industry="mediadata"></ScheduleSeekSouthernPlus>
        </div>
        <ScheduleSeekDefault v-if="mediaType === 2"></ScheduleSeekDefault>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { EHgSingleZtreeUrl } from '@/types/common/enumindex'
import { Tadindustry } from '@/types/components/hgindustry'
import { TSelectZtree } from '@/types/common'

/** 媒体类型 *
 * 0:南方日报 1:南方+
 **/
const mediaType = ref(0)

/** 媒体id **/
const mediaId = ref('')

/** 初始化媒体 */
const mediadata = reactive<Tadindustry>({
  adindustryid: '',
  adindustryname: ''
})

/** 叠次树返回 叠次版本根据叠次查询，版面类别根据类型和叠次查询，规格根据媒体查询*/
const onChange = (data: ITreeNode) => {
  if (data === undefined) {
    return
  }
  if (data.stype === 'media') {
    if (data.name === '南方日报') {
      mediaType.value = 0
      mediadata.adindustryid = data.id
      onMediaPaperChange()
    } else if (data.name === '南方晚报') {
      mediaType.value = 0
      mediadata.adindustryid = data.id
      onMediaPaperChange()
    } else if (data.name === '南方+') {
      mediaType.value = 1
      mediadata.adindustryid = data.id
      onMediaChange()
    } else if (data.name === '日报微信') {
      mediaType.value = 1
      mediadata.adindustryid = data.id
      onMediaChange()
    } else if (data.name === '日报微博') {
      mediaType.value = 1
      mediadata.adindustryid = data.id
      onMediaChange()
    } else {
      mediaType.value = 2
    }
  }
}

const childRef = ref<any>()

const childPpaerRef = ref<any>()

const onMediaChange = () => {
  console.log('父组件')
  childRef.value.mediaChange(mediadata.adindustryid)
}
const onMediaPaperChange = () => {
  console.log('父组件')
  childPpaerRef.value.paperMediaChange(mediadata.adindustryid)
}
</script>
<style scoped></style>
