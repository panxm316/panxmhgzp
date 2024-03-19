<!-- eslint-disable vue/require-v-for-key -->
<!--
 * @Author: lhl
 * @Date: 2023-11-28
 * @LastEditTime: 2024-03-05 11:32:34
 * @LastEditors: wanghl
 * @Description: 报纸排期查看
-->
<template>
  <div class="search_box">
    <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
    <el-select
      v-model="floadId"
      placeholder="叠次"
      style="width: 120px"
      clearable
      @change="handleFlod"
    >
      <el-option v-for="item in foldList" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>
    <el-select
      v-model="floaAreaverdId"
      placeholder="叠次版本"
      clearable
      @change="handleFlodAreaver"
    >
      <el-option
        v-for="item in foldAreaverList"
        :key="item.id"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
    <el-checkbox v-model="hideplan">隐藏叠名</el-checkbox>
    <el-checkbox v-model="orderswitch" @change="checkorder()">隐藏预定订单窗口</el-checkbox>
  </div>
  <div class="page_box">
    <el-row :gutter="20">
      <el-col v-show="hideplan === false" :span="spanWidth(0)" class="page_box_col">
        <div class="page_box_title">叠名</div>
        <div class="page_box_plan">
          <el-table :data="planList" row-key="id" :border="true">
            <el-table-column prop="medianame" label="选择叠名查看排期">
              <template #default="scope">
                <el-button
                  type="text"
                  size="small"
                  authority-key="1804:BXDDSC"
                  style="color: #69696"
                  @click.stop="seekplan(scope.row)"
                  >{{ scope.row.medianame }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="spanWidth(1)" class="page_box_gray">
        <div v-show="hideorder === false" class="orderdetail_box">
          <el-table :data="dataList" highlight-current-row border>
            <el-table-column label="业务员" prop="employname" min-width="160" align="center" />
            <el-table-column
              label="广告规格"
              prop="adspecname"
              show-overflow-tooltip
              min-width="160"
              align="center"
            />
            <el-table-column
              label="版面色彩"
              prop="adcolorname"
              show-overflow-tooltip
              min-width="160"
            />
            <el-table-column
              label="版面类别"
              prop="pagesortname"
              show-overflow-tooltip
              min-width="160"
              align="center"
            />
            <el-table-column
              label="预定版面"
              prop="foldpageplanname"
              show-overflow-tooltip
              min-width="160"
              align="center"
            />
            <el-table-column
              label="预定状态"
              prop="ipublishstatus"
              show-overflow-tooltip
              min-width="160"
              align="center"
            >
              <template #default="scope">
                <span v-if="scope.row.ipublishstatus === 0">预约</span>
                <span v-if="scope.row.ipublishstatus === 1">预定</span>
                <span v-if="scope.row.ipublishstatus === 2">待发布</span>
                <span v-if="scope.row.ipublishstatus === 3">安排</span>
                <span v-if="scope.row.ipublishstatus === 4">见报</span>
                <span v-if="scope.row.ipublishstatus === 5">已发布</span>
                <span v-if="scope.row.ipublishstatus === 6">上架</span>
                <span v-if="scope.row.ipublishstatus === 7">下架</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="page_box_gray_bar">
          <div class="page_box_gray_bar_color"></div>
          <div class="page_box_gray_bar_text">彩色版</div>
          <div
            class="page_box_gray_bar_color"
            style="margin-left: 15px; background-color: #ffffff"
          ></div>
          <div class="page_box_gray_bar_text">黑白版</div>
          <div class="page_box_gray_bar_text" style="margin-left: 15px">{{ publishNum }}</div>
        </div>
        <div id="refPageBox" class="page_box_gray_content">
          <div v-for="item in pageList">
            <ul>
              <li class="pageli">
                <div :style="pageStyle(item)">
                  <div v-for="subitem in item.pageOrderItemVoList">
                    <div :style="pageItemStyle(subitem, item.ipagewidth)" class="adbox">
                      <p style="font-size: 8px">业务员：{{ subitem.employname }}</p>
                      <p style="font-size: 8px">规格色彩：{{ subitem.adspecname }}</p>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IFoldVO } from '@/types/views/media/fold'
import { getFoldComboApi } from '@/api/media/foldareaver'
import {
  getFoldAreaverComboApi,
  getFoldPagePlanListApi,
  getPageListtApi,
  queryOredrPredetermineListAPI
} from '@/api/schedule/seekschedule'
import { IAxios } from 'axios'
import { Tadindustry } from '@/types/components/hgindustry'
import dayjs from 'dayjs'
import { fa } from 'element-plus/es/locale'

/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '00',
  timeoption: [
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
  ],
  startTime: '',
  endTime: ''
})

const adindustry = reactive<Tadindustry>({
  adindustryid: '',
  adindustryname: ''
})
const props = defineProps<{ industry?: Tadindustry }>()
/** 媒体ID */
const mediaId = ref('')

/** 叠次ID */
const floadId = ref('')

/** 起始日期 */
const startTime = ref('')

/** 结束日期 */
const endTime = ref('')

/** 叠次版本ID */
const floaAreaverdId = ref('')

/** 叠次列表 */
const foldList = ref<any>([])

/** 版面数量 */
const publishNum = ref('')

/** 隐藏叠名 */
const hideplan = ref(false)
/** 隐藏订单 */
const hideorder = ref(true)
/** 隐藏预定订单 */
const orderswitch = ref(false)

/** 显示预定订单 */
const showorder = ref(true)

/** 计划请求对象 */
const planvo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  startTome: '',
  endTime: ''
})

/** 版面请求对象 */
const pagevo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  publishTime: ''
})

/** 预定广告订单请求对象 */
const ordervo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  strartTime: '',
  endTime: ''
})

/** 叠次版本列表 */
const foldAreaverList = ref<any>([])

/** 计划列表 */
const planList = ref<any>([])

/** 版面列表 */
const pageList = ref<any>([])

/** 版面宽度 */
const pageWidth = ref(300)

/** 版面宽度 */
const pageHeight = ref(300)
/** 列表 */
const dataList = ref<any>([])
/** 标志 */
const bshow = ref(false)

watch(
  () => props.industry,
  () => {
    initmediaid()
  },
  { deep: true }
)

onMounted(() => {
  getStartTime()
  getEndTime()
})

/**
 * 获取叠次列表
 */
const getFoldPageList = () => {
  getFoldComboApi(mediaId.value).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldList.value = res.obj
      console.log(foldList.value)
    }
  })
}

/**
 * 获取叠次版本列表
 */
const getFoldAreaverList = () => {
  getFoldAreaverComboApi(floadId.value).then((res: IAxios<IFoldVO[]>) => {
    if (res.success) {
      foldAreaverList.value = res.obj
      console.log(foldAreaverList.value)
    }
  })
}

/**
 * 获取计划列表
 */
const getPlanList = () => {
  getFoldPagePlanListApi(planvo.value).then((res) => {
    if (res.success) {
      planList.value = res.obj
    }
  })
}

/**
 * 获取版面列表
 */
const getPageList = () => {
  getPageListtApi(pagevo.value).then((res) => {
    if (res.success) {
      pageList.value = res.obj
      publishNum.value = '版面数量: ' + pageList.value.length
      console.log('版面数据')
      console.log(pageList.value)
    }
  })
}

/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  startTime.value = val.startTime
  endTime.value = val.endTime
  queryPlan()
}

/**
 * 叠次选择事件
 */
const handleFlod = () => {
  floaAreaverdId.value = ''
  getFoldAreaverList()
  queryPlan()
}

/**
 * 叠次版本选择事件
 */
const handleFlodAreaver = () => {
  queryPlan()
}

/**
 * 查询出版计划列表
 */
const queryPlan = () => {
  if (mediaId.value === '') {
    // ElMessage.info('请选择广告媒体')
    return
  }

  /* if (floadId.value === '' || floaAreaverdId.value === '') {
    ElMessage.info('请选择叠次和叠次版本查看排期计划')
    return
  }*/
  if (startTime.value === '') {
    ElMessage.info('请选择起始时间!')
    return
  }
  if (endTime.value === '') {
    ElMessage.info('请选择结束时间!')
    return
  }
  planvo.value.mediaId = mediaId.value
  planvo.value.foldId = floadId.value
  planvo.value.areaverId = floaAreaverdId.value
  planvo.value.startTome = startTime.value
  planvo.value.endTime = endTime.value
  getPlanList()
}

/**
 * 叠名选择事件
 */
const seekplan = (row: any) => {
  pagevo.value.mediaId = mediaId.value
  pagevo.value.foldId = row.foldid
  pagevo.value.areaverId = row.foldareaverid
  pagevo.value.publishTime = row.publishdate
  getPageList()
  getOredrPredetermineList(row)
}

/**
 * 计算span宽度
 */
const spanWidth = (index: any) => {
  if (index === 0) return 6
  if (index === 1) {
    if (hideplan.value === false) return 18
    else {
      return 24
    }
  }
}

/**
 * 计算版面显示属性
 */
const pageStyle = (item: any) => {
  var obj = document.getElementById('refPageBox')
  var allwidth = obj?.offsetWidth
  var meanWidth = allwidth! / 2 - 60
  var tempPageWidth = mmTOpx(item.ipagewidth)
  var ratio = meanWidth / tempPageWidth
  tempPageWidth = meanWidth
  var tempPageHeight = mmTOpx(item.ipageheight) * ratio
  var color = ''
  if (item.adcolorname === '彩色') {
    color = '#00bfff'
  } else {
    color = '#ffffff'
  }
  return {
    width: tempPageWidth + 'px',
    height: tempPageHeight + 'px',
    backgroundColor: color
  }
}

/**
 * 计算广告尺寸属性
 */
const pageItemStyle = (item: any, ipagewidth: any) => {
  var obj = document.getElementById('refPageBox')
  var allwidth = obj?.offsetWidth
  var meanWidth = allwidth! / 2 - 60
  var tempPageWidth = mmTOpx(ipagewidth)
  var ratio = meanWidth / tempPageWidth
  var left = mmTOpx(item.nleftx) * ratio
  var top = mmTOpx(item.ntopy) * ratio
  var width = mmTOpx(item.nwidth * 10) * ratio
  var height = mmTOpx(item.nheight * 10) * ratio
  console.log(left + ':' + top + ':' + width + ':' + height)
  return {
    top: top + 'px',
    left: left + 'px',
    width: width + 'px',
    height: height + 'px',
    backgroundColor: '#ff0000'
  }
}

/**
 * 毫米转换为像素
 */
const mmTOpx = (value: any) => {
  var dpi = getDeviceDPI()
  return value * (dpi / 25.4)
}

/**
 * 媒体ID初始化赋值
 */
const initmediaid = () => {
  if (props.industry) {
    mediaId.value = props.industry.adindustryid!
    console.log('媒体ID')
    console.log(mediaId.value)
  }
}

const getStartTime = () => {
  var base = new Date().getTime()
  var oneDay = 24 * 3600 * 1000
  var starttime = new Date((base += oneDay))
  var month = starttime.getMonth() + 1
  timedata.startTime = starttime.getFullYear() + '-' + month + '-' + starttime.getDate()
  startTime.value = timedata.startTime
}

const getEndTime = () => {
  var base = new Date().getTime()
  var oneDay = 24 * 3600 * 1000
  var starttime = new Date((base += oneDay * 7))
  var month = starttime.getMonth() + 1
  timedata.endTime = starttime.getFullYear() + '-' + month + '-' + starttime.getDate()
  endTime.value = timedata.endTime
}

/**
 * 媒体变化被父组件调研
 */
const paperMediaChange = (data: any) => {
  foldList.value = []
  foldAreaverList.value = []
  mediaId.value = data
  console.log('媒体ID')
  console.log(mediaId.value)
  floaAreaverdId.value = ''
  floadId.value = ''
  getFoldPageList()
  getFoldAreaverList()
  queryPlan()
}
/**
 * 获取预定广告订单
 */
const getOredrPredetermineList = (row: any) => {
  ordervo.value.mediaId = mediaId.value
  ordervo.value.foldId = row.foldid
  ordervo.value.areaverId = row.foldareaverid
  ordervo.value.strartTime = dayjs(row.publishdate).format('YYYY-MM-DD')
  ordervo.value.endTime = ordervo.value.strartTime
  queryOredrPredetermineListAPI(ordervo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      if (dataList.length > 0) {
        hideorder.value = true
        bshow.value = true
        orderswitch.value = true
      } else {
        hideorder.value = false
        bshow.value = false
        orderswitch.value = false
      }
    }
  })
}

/**
 * 检查事件
 */
const checkorder = () => {
  if (hideorder.value === false) {
    hideorder.value = true
  } else {
    hideorder.value = false
  }
}
const getDeviceDPI = () => {
  // 默认值 96dpi
  var dpi = 96
  if (window.screen && window.screen.deviceXDPI && window.screen.logicalXDPI) {
    dpi = (window.screen.deviceXDPI / window.screen.logicalXDPI) * dpi
  } else if (window.devicePixelRatio) {
    dpi = dpi * window.devicePixelRatio
  }
  return dpi
}

defineExpose({
  paperMediaChange
})
</script>
<style scoped>
.page_box {
  min-height: 635px;
  width: 98%;
  margin: 0 auto;
}
.page_box_title {
  width: 100%;
}
.page_box_plan {
  width: 100%;
  margin-top: 15px;
}
.page_box_col {
  background-color: #ffffff;
  width: 100%;
  padding: 15px;
  border-radius: 4px;
}
.page_box_gray {
  background-color: #e8e8e8;
  width: 95%;
  padding: 15px;
  border-radius: 4px;
  margin-left: 0px;
}
.page_box_gray_bar {
  width: 100%;
  height: 35px;
}
.page_box_gray_bar_color {
  width: 18px;
  height: 18px;
  background-color: deepskyblue;
  float: left;
}

.page_box_gray_bar_text {
  width: auto;
  height: 35px;
  line-height: 35px;
  float: left;
  margin-left: 5px;
  margin-top: -8px;
  font-size: 14px;
}

.page_box_gray_content {
  width: 100%;
  height: auto;
  overflow-y: hidden;
}

.ul {
  margin: 0;
  padding: 0;
}

.pageli {
  float: left;
  margin-left: 20px;
  margin-top: 20px;
}
.orderdetail_box {
  max-height: 200px;
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
  background-color: #ffffff;
  margin-bottom: 15px;
}
.adbox {
  position: relative;
  overflow: hidden;
  background-color: #ff0000;
  color: #ffffff;
}
</style>
