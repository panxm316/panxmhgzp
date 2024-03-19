<!--
 * @Author: suny
 * @Date: 2023-08-21 14:00:51
 * @LastEditTime: 2023-10-14 11:04:05
 * @LastEditors: suny
 * @Description:
-->
<template>
  <div class="user-info-head" @click="editCropper()">
    <img :src="options.img" title="点击上传头像" class="img-circle img-lg" />
    <el-dialog
      v-model="open"
      :title="title"
      width="800px"
      append-to-body
      @opened="modalOpened"
      @close="closeDialog"
    >
      <el-row>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <vue-cropper
            v-if="visible"
            ref="cropper"
            :img="options.img"
            :info="true"
            :full="true"
            :output-size="0.1"
            :auto-crop="options.autoCrop"
            :auto-crop-width="options.autoCropWidth"
            :auto-crop-height="options.autoCropHeight"
            :fixed-box="options.fixedBox"
            :output-type="options.outputType"
            @realTime="realTime"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="options.previews.url" :style="options.previews.img" />
          </div>
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :md="2">
          <el-upload action="#" :show-file-list="false" :before-upload="beforeUpload">
            <el-button>
              选择
              <el-icon class="el-icon--right"><Upload /></el-icon>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{ span: 1, offset: 2 }" :md="2">
          <el-button icon="Plus" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="Minus" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="RefreshLeft" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :md="2">
          <el-button icon="RefreshRight" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{ span: 2, offset: 6 }" :md="2">
          <el-button type="primary" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, ref, reactive } from 'vue'
import { VueCropper } from 'vue-cropper'
import 'vue-cropper/dist/index.css'
// import { uploadAvatar } from '@/api/system/user'
import useUserStore from '@/store/modules/user'
import { updateEmployImgApi } from '@/api/system/user'
import { IAxios } from 'axios'
const userStore = useUserStore()
const { proxy } = getCurrentInstance() as any

/** 打开dialog窗口标识 */
const open = ref(false)
/** 图片容器显示状态 */
const visible = ref(false)
/** 窗口标题 */
const title = ref('修改头像')

/** 图片裁剪数据 */
const options: any = reactive({
  /** 裁剪图片的地址 */
  img: userStore.avatar,
  /** 是否默认生成截图框 */
  autoCrop: true,
  /** 默认生成截图框宽度 */
  autoCropWidth: 30,
  /** 默认生成截图框高度 */
  autoCropHeight: 30,
  /** 固定截图框大小 不允许改变 */
  fixedBox: true,
  /** 默认生成截图为PNG格式 */
  outputType: 'png',
  /** 预览数据 */
  previews: {}
})

/**
 * 编辑头像
 */
const editCropper = () => {
  open.value = true
}
/** 打开弹出层结束时的回调 */
const modalOpened = () => {
  visible.value = true
}
/** 覆盖默认上传行为 */
/** 向左旋转 */
const rotateLeft = () => {
  proxy.$refs.cropper.rotateLeft()
}
/** 向右旋转 */
const rotateRight = () => {
  proxy.$refs.cropper.rotateRight()
}
/** 图片缩放 */
const changeScale = (num: any) => {
  num = num || 1
  proxy.$refs.cropper.changeScale(num)
}
/** 上传预处理 */
const beforeUpload = (file: any) => {
  if (file.type.indexOf('image/') === -1) {
    proxy.$modal.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。')
    return false
  } else {
    if (file.size > 1 * 1024 * 1024) {
      ElMessage.warning('图片大小不超过1M')
      return false
    }
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
      options.img = reader.result
    }
    return false
  }
}
/** 上传图片 */
const uploadImg = () => {
  proxy.$refs.cropper.getCropData((data: string) => {
    canvasDataURL(data, 1)
    console.log(data)
  })
}
/**
 * 保存头像到数据库
 * @param data
 */
const saveImg = (data: string) => {
  // updateEmployImgApi(data).then((res: IAxios) => {
  //   if (res.success) {
  //     open.value = false
  //     options.img = data
  //     userStore.avatar = data
  //     ElMessage.success('修改成功')
  //     visible.value = false
  //   }
  // })
}
/**
 * 压缩图片
 * @param base64
 * @param quality
 */
const canvasDataURL = (base64: any, quality: any) => {
  var img = new Image()
  img.src = base64
  var that: any = this
  img.onload = function () {
    // 默认按照比例压缩
    var scale = img.width / img.height
    // 规定压缩后的大小
    var canvasWidth = 30
    var canvasHeight = 30 // canvasWidth / scale
    // 生成canvas
    var canvas = document.createElement('canvas')
    var ctx: any = canvas.getContext('2d')
    // 创建节点属性
    canvas.width = canvasWidth
    canvas.height = canvasHeight
    var anw: any = document.createAttribute('width')
    anw.nodeValue = canvasWidth
    var anh: any = document.createAttribute('height')
    anh.nodeValue = canvasHeight
    canvas.setAttributeNode(anw)
    canvas.setAttributeNode(anh)
    ctx.drawImage(this, 0, 0, canvasWidth, canvasHeight)
    // quality 图像质量,值越小，所绘制出的图像越模糊
    var base64String = canvas.toDataURL('img/jpeg', quality)
    options.img = base64String
    console.log(base64String)
    saveImg(base64String)
  }
}

/** 实时预览 */
const realTime = (data: any) => {
  options.previews = data
}
/** 关闭窗口 */
const closeDialog = () => {
  options.img = userStore.avatar
  options.visible = false
}
</script>

<style lang="scss" scoped>
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
