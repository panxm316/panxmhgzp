<!-- eslint-disable vue/valid-v-for -->
<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<!-- eslint-disable vue/require-v-for-key -->
<!--
 * @Author: lhl
 * @Date: 2023-12-01
 * @Description: 南方+排期查看
-->
<template>
  <div class="search_box">
    <el-date-picker
      v-model="scheduleTime"
      :editable="false"
      type="month"
      placeholder="广告排期"
      :clearable="false"
      style="width: 130px; margin-right: 5px"
      @change="changetime"
    >
    </el-date-picker>

    <el-select
      v-model="floadId"
      clearable
      placeholder="频道"
      style="width: 120px"
      @change="handleFlod"
    >
      <el-option v-for="item in foldList" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>
    <el-select v-model="floaAreaverdId" clearable placeholder="栏目" @change="handleFlodAreaver">
      <el-option
        v-for="item in foldAreaverList"
        :key="item.id"
        :label="item.name"
        :value="item.id"
      />
    </el-select>
    <div style="float: right; margin-top: 0px">
      <el-radio-group v-model="showtype" @change="changeTheme">
        <el-radio-button label="表格"></el-radio-button>
        <el-radio-button label="日历"></el-radio-button>
      </el-radio-group>
    </div>
  </div>
  <div class="page_box">
    <div class="page_box_gray_bar">
      <div class="page_box_gray_bar_color"></div>
      <div class="page_box_gray_bar_text">已锁定</div>
      <div
        class="page_box_gray_bar_color"
        style="margin-left: 15px; background-color: #f00889"
      ></div>
      <div class="page_box_gray_bar_text">已发布</div>
      <div class="page_box_gray_bar_color" style="margin-left: 15px; background-color: #e8e8"></div>
      <div class="page_box_gray_bar_text">待发布</div>
    </div>
    <div v-if="showflag === 0" id="refPageBox" class="page_box_gray_content">
      <div class="public-box">
        <ul class="week-title flex">
          <li v-for="(v, i) in weekList" :key="i">{{ v }}</li>
        </ul>
        <ul
          v-for="(arr, index) in advdatalist"
          :key="index + 'a'"
          class="date-list flex"
          :class="index % 2 === 0 ? 'odd' : ''"
        >
          <li
            v-for="(obj, i) in arr"
            :key="i"
            :class="obj.class + ' ' + (i === arr.length - 1 ? '' : 'border-right')"
          >
            <div v-show="obj.current === 1">
              <div v-for="item in orderlist">
                <div
                  v-if="item.day === obj.d"
                  :style="advItemStyle(item)"
                  class="advitem"
                  @click="handleOrder(item)"
                >
                  <p>业务员：{{ item.employname }}</p>
                  <p style="margin: 0; line-height: 100%">规格：{{ item.adspecname }}</p>
                </div>
              </div>
              {{ obj.d }}
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div v-if="showflag === 1" id="refPageBox" class="page_box_gray_content">
      <div class="table">
        <table style="width: 100%" border="1" cellspacing="0" cellpadding="10">
          <tr align="center" style="height: 45px">
            <div style="width: 160px"><td style="width: 160px">广告形式</td></div>
            <td v-for="item in advdDaylist[0].advOfDayVoList" :key="item.id" align="center">
              <div style="width: 60px">{{ item.day }}号</div>
            </td>
          </tr>
          <tr v-for="item in advdDaylist" :key="item.id" align="center">
            <td style="width: 160px">{{ item.name }}</td>
            <td v-for="dayitem in item.advOfDayVoList" :key="item.id" align="center">
              <div
                v-for="advtime in dayitem.pageOrderItemVoList"
                :style="advItemStyle(advtime)"
                class="coltext"
                @click="handleOrder(advtime)"
              >
                <p>业务员：{{ advtime.employname }}</p>
                <p style="margin: 0; line-height: 100%">规格：{{ advtime.adspecname }}</p>
              </div>
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
  <!--
      新增部门任务窗口
  -->
  <el-dialog
    v-model="dialogOrderVisible"
    title="订单详情"
    :width="800"
    append-to-body
    :destroy-on-close="true"
  >
  </el-dialog>
</template>
<script setup lang="ts">
import { getFoldComboApi } from '@/api/media/foldareaver'
import { IFoldVO } from '@/types/views/media/fold'
import moment from 'moment'
import 'moment/locale/zh-cn'
import {
  getFoldAreaverComboApi,
  getAdvOrderList,
  getAdvOrderofDay
} from '@/api/schedule/seekschedule'
import { IAxios } from 'axios'
import { Tadindustry } from '@/types/components/hgindustry'
moment.locale('zh-cn')
const props = defineProps<{ industry?: Tadindustry }>()
/** 媒体ID */
const mediaId = ref('')
/** 叠次ID */
const floadId = ref('')
/** 叠次版本ID */
const floaAreaverdId = ref('')
/** 叠次列表 */
const foldList = ref<any>([])
/** 叠次版本列表 */
const foldAreaverList = ref<any>([])
/** 广告排期 */
const scheduleTime = ref<Date | string>('')
/** 表格/日历 */
const showtype = ref('日历')
/** 周定义 */
const weekList = ref<any>(['周一', '周二', '周三', '周四', '周五', '周六', '周日'])
/** 日历/广告 */
const advdatalist = ref<any>([])
/** 表格/广告 */
const advdDaylist = ref<any>([])
/** 广告订单 */
const orderlist = ref<any>([])
/** 广告订单 */
const advOfDayVoList = ref<any>([])
/** 订单详情窗口显示状态 */
const dialogOrderVisible = ref(false)
/** 表格/日历 */
const showflag = ref(0)
/** 日历广告项目 */
const advitemlist = ref<any>([
  { title: '金融广告', day: 1, advstatus: 0 },
  { title: '汽车广告', day: 1, advstatus: 1 },
  { title: '金融广告', day: 2, advstatus: 0 }
])

watch(
  () => props.industry,
  () => {
    initmediaid()
  },
  { deep: true }
)

onMounted(() => {
  initDate()
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
 * 叠次选择事件
 */
const handleFlod = () => {
  floaAreaverdId.value = ''
  getFoldAreaverList()
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
 * 叠次版本选择事件
 */
const handleFlodAreaver = () => {
  getAdvOrdeList()
  getAdvOrdeDayList()
}

/**
 * 获取月份天数
 */
const getMonthDays = (momentObj: any) => {
  return momentObj.daysInMonth()
}

/**
 * 获取周天数
 */
const getWeekDays = (momentObj: any) => {
  // 星期几，0-6, 星期天为0
  return momentObj.startOf('month').weekday() - 1
}

/**
 * 时间选择事件
 */
const changetime = () => {
  if (showflag.value === 0) {
    InitAdvDataList()
    getAdvOrdeList()
  } else {
    getAdvOrdeDayList()
  }
}

/** 广告订单请求对象 */
const ordervo = ref({
  mediaId: '',
  foldId: '',
  areaverId: '',
  strartTime: '',
  endTime: ''
})

/**
 * 初始化时间
 */
const initDate = () => {
  scheduleTime.value = new Date().toDateString()
}

/**
 * 设置日历
 */
const InitAdvDataList = () => {
  const m = moment(scheduleTime.value)
  moment.locale('zh-cn')
  // 获得当前月的天数  和 第一天的星期数
  const curDays = getMonthDays(m) // 当前天数
  const curWeek = getWeekDays(m.clone()) // 当前月第一天的星期(索引值)
  let upDays = getMonthDays(m.clone().subtract(1, 'month')) // 上月的天数
  console.log(curWeek)
  const currentM = moment(m).format('YYYY-MM') // 当前月
  const beforeM = moment(m).subtract(1, 'months').format('YYYY-MM') // 上个月
  const afterM = moment(m).add(1, 'months').format('YYYY-MM') // 下个月
  // 生成的结构
  const strDate = []
  // 下个月的起始日期
  let nextFirstDate = 0
  // 日历最多有 6行 6*7
  const allNum = 42
  for (let i = 0; i < allNum; i++) {
    // 1. 当前月的上一个月 需要显示的日期
    // 返回的索引值刚好是上月在当月显示的天数
    if (i < curWeek) {
      strDate.unshift({
        class: 'special',
        d: upDays,
        m: beforeM,
        current: 0,
        advitem: advitemlist
      })
      upDays-- // 倒叙显示   30 31
    } else if (i >= curDays + curWeek) {
      // 去除掉当月天数+上月天数就是下月天数
      // 2. 当前月的下一个月：除去当月最后一天+上月的几天剩余的是下月开始计算
      // curWeek 返回值刚好是上月占用的天数
      nextFirstDate++

      strDate.push({
        class: 'special',
        d: nextFirstDate,
        m: afterM,
        current: 0,
        advitem: advitemlist
      })
    } else {
      // 3. 当前月
      // i-curWeek+1 为当前月的天数
      // date()获取日期号
      // m.date() == i - curWeek + 1说明这一天是当月当天，添加样式
      const currentClass = moment().date() === i - curWeek + 1 ? 'current-day' : 'current'

      strDate.push({
        class: currentClass,
        d: i - curWeek + 1,
        m: currentM,
        current: 1,
        advitem: advitemlist
      })
    }
  }
  // strDate
  const times = allNum / 7
  const dateList = []
  for (let k = 0; k < times; k++) {
    const arr = []
    for (let i = 0; i < 7; i++) {
      arr.push(strDate[i + k * 7])
    }
    dateList.push(arr)
  }
  if (dateList[times - 1][0]['m'] === afterM) {
    dateList.pop()
  }
  advdatalist.value = dateList
}

/**
 * 计算广告显示属性
 */
const advItemStyle = (item: any) => {
  var fillcolor = '#f00889'
  var textcolor = '#ffffff'
  if (item.ipublishstatus === 0) {
    fillcolor = 'deepskyblue'
  } else if (item.ipublishstatus === 1) {
    fillcolor = '#f00889'
  } else if (item.ipublishstatus === 2) {
    fillcolor = '#e8e8'
  }
  return {
    color: textcolor,
    backgroundColor: fillcolor,
    padding: '10px'
  }
}
/**
 * 获取广告订单
 */
const getAdvOrdeList = () => {
  ordervo.value.mediaId = mediaId.value
  ordervo.value.foldId = floadId.value
  ordervo.value.areaverId = floaAreaverdId.value
  var year = new Date(scheduleTime.value).getFullYear()
  var month = new Date(scheduleTime.value).getMonth() + 1
  if (month < 9) {
    ordervo.value.strartTime = year.toString() + '-0' + month.toString() + '-01'
  } else {
    ordervo.value.strartTime = year.toString() + '-' + month.toString() + '-01'
  }

  const m = moment(ordervo.value.strartTime)
  const curDays = getMonthDays(m) // 当前天数
  if (month < 9) {
    ordervo.value.endTime = year.toString() + '-0' + month.toString() + '-' + curDays.toString()
  } else {
    ordervo.value.endTime = year.toString() + '-' + month.toString() + '-' + curDays.toString()
  }
  console.log(ordervo)
  getAdvOrderList(ordervo.value).then((res) => {
    if (res.success) {
      orderlist.value = res.obj
      console.log(orderlist.value)
    }
  })
}

/**
 * 获取广告形式订单
 */
const getAdvOrdeDayList = () => {
  ordervo.value.mediaId = mediaId.value
  ordervo.value.foldId = floadId.value
  ordervo.value.areaverId = floaAreaverdId.value
  var year = new Date(scheduleTime.value).getFullYear()
  var month = new Date(scheduleTime.value).getMonth() + 1
  if (month < 9) {
    ordervo.value.strartTime = year.toString() + '-0' + month.toString() + '-01'
  } else {
    ordervo.value.strartTime = year.toString() + '-' + month.toString() + '-01'
  }

  const m = moment(ordervo.value.strartTime)
  const curDays = getMonthDays(m) // 当前天数
  if (month < 9) {
    ordervo.value.endTime = year.toString() + '-0' + month.toString() + '-' + curDays.toString()
  } else {
    ordervo.value.endTime = year.toString() + '-' + month.toString() + '-' + curDays.toString()
  }
  console.log(ordervo)
  getAdvOrderofDay(ordervo.value).then((res) => {
    if (res.success) {
      advdDaylist.value = res.obj
      console.log('广告形式')
      console.log(advdDaylist.value)
    }
  })
}

/**
 * 媒体ID初始化赋值
 */
const initmediaid = () => {
  /* if (props.industry) {
    foldList.value = []
    mediaId.value = props.industry.adindustryid!
    console.log('媒体ID')
    console.log(mediaId.value)
    floaAreaverdId.value = ''
    floadId.value = ''
    getFoldPageList()
    getFoldAreaverList()
    InitAdvDataList()
    getAdvOrdeDayList()
  }*/
}

/**
 * 日历/表格变换
 */
const changeTheme = (val: any) => {
  if (val === '日历') {
    showflag.value = 0
  } else {
    showflag.value = 1
  }
  console.log(val)
}

/**
 * 媒体变化被父组件调研
 */
const mediaChange = (data: any) => {
  foldList.value = []
  foldAreaverList.value = []
  mediaId.value = data
  floaAreaverdId.value = ''
  floadId.value = ''
  getFoldPageList()
  getFoldAreaverList()
  InitAdvDataList()
  getAdvOrdeDayList()
}

/**
 * 显示订单详情
 */
const handleOrder = (row: any) => {
  dialogOrderVisible.value = true
}

defineExpose({
  mediaChange
})
</script>

<style lang="scss" scoped>
.page_box {
  padding: 15px;
  min-height: 635px;
  width: 100%;
  margin: 0 auto;
  background-color: #ffffff;
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
  overflow-y: scroll;
}
.public-box {
  /*height: 290px;*/
  padding: 20px;
  border: 1px solid #dfdfdf;

  > p {
    color: #374256;
    font-weight: 500;
    text-align: center;
    margin-bottom: 10px;
  }

  .week-title {
    width: 100%;
    /*margin-bottom: 10px;*/
    /*border-bottom: 1px solid #dfdfdf;*/
    li {
      text-align: center;
      line-height: 60px;
      font-size: 12px;
      flex: 1;
      background-color: #eaecf3;
      /*width: 30px;*/
      /*margin: 0 8px;*/
    }
  }

  .date-list {
    width: 100%;
    margin-bottom: 3px;

    li {
      text-align: center;
      line-height: 80px;
      color: #475369;
      /*width: 30px;*/
      flex: 1;
      /*margin: 0 8px;*/
      /*border-radius: 15px;*/
      /*cursor: pointer;*/
    }

    .special {
      color: #b1b8c5;
    }
  }

  .odd {
    background-color: #f5f7fd;
  }

  .border-right {
    border-right: 1px solid #d9e1eb;
  }

  .advitem {
    width: 100%;
    margin: 5px;
    padding-top: 0px;
    line-height: 100%;
    font-size: 12px;
    cursor: pointer;
  }
  .advitembox {
    background-color: deepskyblue;
    color: #ffffff;
  }
  .advitem p {
    margin: 0;
  }
}
.table {
  margin: 10px;
  padding: 20px;
  background-color: #ffffff;
  white-space: nowrap;
}

.coltext {
  font-size: 12px;
  margin-top: 0px;
  margin: 5px;
  cursor: pointer;
}
.coltext p {
  margin: 0;
}
</style>
