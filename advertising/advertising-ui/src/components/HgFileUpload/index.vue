<!--
 * @Author: suny
 * @Date: 2023-09-04 10:41:31
 * @LastEditTime: 2023-11-01 10:57:27
 * @LastEditors: suny
 * @Description:
-->
<template>
  <div>
    <el-progress v-show="progressUploading" :percentage="percentage"></el-progress>
    <div
      v-show="!progressUploading"
      id="selectfile"
      class="choosefile"
      style="border: none; float: left"
    >
      <el-icon><UploadFilled /></el-icon>
      选择文件
    </div>
    <div id="fileuploader"></div>
  </div>
</template>

<script lang="ts" setup>
import { getResourceTypeFormatListApi } from '@/api/system/resourcetype'
import { THgFileUploadParam, TUpFile } from '@/types/components/hgfileupload'
import { IResourceType } from '@/types/views/system/resourcetype'
import { IAxios } from 'axios'
import { nextTick, onMounted, ref } from 'vue'

defineOptions({
  name: 'HgFileUpload'
})
const props = defineProps<THgFileUploadParam>()
// 进度条显示
const progressUploading = ref(false)
// 文件上传显示进度条
const percentage = ref(0)
/** 资源类型支持的格式列表 */
const resourceTypes = ref<IResourceType[]>([])
const emit = defineEmits<{
  /** 获取上传后返回对象 */
  getupfile: [data: IAxios<TUpFile>]
}>()

onMounted(() => {
  $('.progress-bar').attr('style', 'width:' + percentage.value + '%')
  nextTick(() => {
    $('#fileuploader').refreshWebUpload()
    if (props.storytypes.length > 0) {
      webuploadinit()
    } else {
      getStoryType()
    }
  })
  /** 文件上传进度显示状态 */
  window.emitter.on('uploading', function (v) {
    progressUploading.value = v
  })
  /** 文件上传进度 */
  window.emitter.on('uploadProgress', function (value: number) {
    percentage.value = parseInt(value.toFixed(0))
  })
  window.emitter.on('uploadSuccess', function (res: IAxios<TUpFile>) {
    console.log(res.success)
    console.log(res.obj)
    emit('getupfile', res)
    percentage.value = 0
  })
})
/**
 * 上传文件类型格式获取
 */
const getStoryType = () => {
  getResourceTypeFormatListApi().then(({ success, obj }: IAxios<IResourceType[]>) => {
    if (success) {
      resourceTypes.value = obj
      webuploadinit()
    }
  })
}

/**
 * 初始化上传组件
 */
const webuploadinit = () => {
  $('#fileuploader').destroyWebUpload()
  $('#fileuploader').powerWebUpload(
    {
      server: props.server,
      auto: true,
      accept: props.accept,
      uploading: true,
      fileNumLimit: props.filenumlimit,
      multiple: props.multiple,
      typelimit: props.typelimit ? props.typelimit : true,
      btnid: 'selectfile'
    },
    props.storytypes.length > 0 ? props.storytypes : resourceTypes.value
  )
}
</script>

<style lang="scss" scoped></style>
